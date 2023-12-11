package com.metflix.service;


import com.metflix.model.Authority;
import com.metflix.model.Enums.AuthoritiesEnum;
import com.metflix.model.User;
import com.metflix.repositories.AuthorityRepository;
import com.metflix.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
//TODO: Currently this class isn't being used. Either delete this or change everything so the controllers dont have access to repositories directly

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Tried to find {%s} email but didn't find in the database", username));
        } else {
            return userOptional.get();
        }
    }




    public Page<User> findPaginated(final int pageNumber, final int pageSize,
                                        final String sortField, final String sortDirection) {


        Sort sort;
        if( sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
             sort = Sort.by(sortField).ascending();
        } else {
            sort = Sort.by(sortField).descending();
        }


        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return userRepository.findAll(pageable);
    }







    /**
        This method validates the user registration, it calls other methods,
        Currently it calls 3 methods:
            1. checks if email already exists
            2. checks if there's any empty fields passed through
            3. checks if the passwords match

        In the future this will probably get replaced by spring-boot-validator
     */
    public List<String> validateUser (User user, String password2) {

        List<String> fieldCheckAnswer = checkUserContainsEmptyFields(user);

        if (!fieldCheckAnswer.get(0).equals("success")) {
            return fieldCheckAnswer;

        } else if (emailExists(user.getUsername())){
            return List.of("email", "Email already exists");

        } else if (!checkPasswordsMatch(user.getPassword(), password2)) {
            return List.of("password", "Passwords do not match");
        }
        return List.of("success", "true");
    }

    /** Returns true when email is already in a database */
    public boolean emailExists (String email) {
        return userRepository.findByUsername(email).isPresent();
    }

    //TODO: Currently the dateOfBirth only gets checked if empty or not, it doesn't check the format of the string
    /** returns <"success", "true"> when fields are completed, otherwise returns a <field, error message> both as String */
    public List<String> checkUserContainsEmptyFields (User user) {
        try{
            HashMap<String, String> fieldsToCheck = new HashMap<String, String>();
            fieldsToCheck.put("name", user.getName());
            fieldsToCheck.put("surname", user.getSurname());
            fieldsToCheck.put("email", user.getUsername());
            fieldsToCheck.put("dateOfBirth", user.getDateOfBirth().toString());
            fieldsToCheck.put("phoneNumber", user.getPhoneNumber());
            fieldsToCheck.put("password", user.getPassword());


            for (Map.Entry<String, String> set: fieldsToCheck.entrySet()) {
                if (set.getValue().trim().isEmpty()) {
                    System.out.println(set.getKey() + " field is empty. Caught in UserService field checker");
                    return List.of(set.getKey(), "Field is empty");
                }
            }
            return List.of("success", "true");

        } catch (Exception e) {
            System.err.println(e + "\nUnknown error thrown during field check, please fix asap");
            return List.of("success", "true");
        }
    }

    /** Returns true if passwords match */
    public boolean checkPasswordsMatch (String pass1, String pass2) {
        return pass1.equals(pass2);
    }


    /** Saves a new user, if the status and registration date are empty, it gives them default ones */
    public void save (User user) {

        if (user.getRegistrationDate() == null) {
            user. setRegistrationDate(LocalDate.now());
        }

//        if (user.getAuthorities().isEmpty()) {
//            user.addAuthority(new Authority(Integer.toUnsignedLong(user.getId()), 22, "MEMBER"));
//        }

        userRepository.save(user);

        var userDbOptional = userRepository.findByUsername(user.getUsername());

        if (userDbOptional.isPresent()) {
            var userDb = userDbOptional.get();
            if (userDb.getAuthorities() == null) {
                userDb.addAuthority(new Authority(1, AuthoritiesEnum.ROLE_MEMBER));
            }
            this.updateUserWithId(userDb, userDb.getId());
        }

        System.out.println("user added to database successfully");
    }


    //TODO: Currently the system accepts spaces as valid, create better validation for received info
    public void updateUserWithId(User user, Integer userId) {

        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) {
            System.err.println("Tried to find user with ID, but no user with id: " + userId + " exists!");
            return;
        }

        User userDb = userOptional.get();

        if (!user.getName().isBlank()) {
            userDb.setName(user.getName());
        }

        if (!user.getSurname().isBlank()) {
            userDb.setSurname((user.getSurname()));
        }

        if (!user.getUsername().isBlank()) {
            userDb.setUsername(user.getUsername());
        }

        if (!user.getDateOfBirth().equals(null)) {
            userDb.setDateOfBirth(user.getDateOfBirth());
        }

        if (!user.getPhoneNumber().isBlank()) {
            userDb.setPhoneNumber(user.getPhoneNumber());
        }

        if (!user.getRegistrationDate().equals(null)) {
            userDb. setRegistrationDate(user.getRegistrationDate());
        }

        if (user.getAuthorities().isEmpty() || user.getAuthorities() == null) {
            userDb.addAuthority(new Authority(1, AuthoritiesEnum.ROLE_MEMBER));
        } else {
            userDb.setAuthorities((List<Authority>)user.getAuthorities());
        }


        userRepository.save(userDb);
        System.out.println("User updated successfully");
    }


    public void addAuthority(Authority authority) {
        //authorityRepository.addAuthority(authority.getUserId(), authority.getAuthority());
        authorityRepository.save(authority);
    }

    public List<Authority> getUserAuthorities (User user) {
        return List.of((Authority) user.getAuthorities());
    }

    public Optional<User> getUserById(int id) {
        return this.userRepository.findById(id);
    }

    public void addAuthorityToUser(String username, String authorityName) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Authority authority = authorityRepository.findByAuthority(authorityName);

            if (authority != null) {
                user.addAuthority(authority);
                userRepository.save(user);
            }
        }

    }




}
