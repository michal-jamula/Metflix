package com.metflix.service;


import com.metflix.exceptions.UserNotFoundException;
import com.metflix.model.Authority;
import com.metflix.model.Enums.AuthoritiesEnum;
import com.metflix.model.User;
import com.metflix.repositories.AuthorityRepository;
import com.metflix.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     *Locates a {@link User} based on a username
     * @param username the username identifying the user whose data is required.
     * @return {@link User}
     * @throws UsernameNotFoundException
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Tried to find {%s} email but didn't find in the database.", username));
        } else {
            return userOptional.get();
        }
    }


    /**
     * Returns Paginated users
     * @param pageNumber
     * @param pageSize
     * @param sortField
     * @param sortDirection
     * @return {@link Page}&lt{@link User}&gt
     */
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

    //TODO: this should throw an exception and then exception should be handled in the controller
    /**
     * Validates a {@link User} by checking for empty fields, existing email and matching password
     * @param user
     * @param password2
     * @return List("success", "true") if valid, or <br> List("email", "Email already exists")
     * List("password", "... do not match") <br>
     * List(field, "Field is Empty")
     */
    public List<String> validateUser (User user, String password2) {

        List<String> emptyFields = checkUserContainsEmptyFields(user);

        if (!emptyFields.get(0).equals("success")) {
            return emptyFields;

        } else if (emailExists(user.getUsername())){
            return List.of("email", "Email already exists");

        } else if (!checkPasswordsMatch(user.getPassword(), password2)) {
            return List.of("password", "Passwords do not match");
        }

        return List.of("success", "true");
    }

    /**
     * Returns {@code true} when email is already in a database
     * @param email
     * @return boolean
     */
    public boolean emailExists (String email) {
        return userRepository.findByUsername(email).isPresent();
    }

    //TODO: Currently the dateOfBirth only gets checked if empty or not, it doesn't check the format of the string

    /**
     * Checks if {@link User} contains empty fields, excludes ID. returns List(field, "Field is empty") or List("success", "true")
     * @param user
     * @return List("success", "true") if true <br>
     * List(field, "Field is empty")
     */
    private List<String> checkUserContainsEmptyFields(User user) {
        try{
            HashMap<String, String> fieldsToCheck = new HashMap<String, String>();
            fieldsToCheck.put("name", user.getName());
            fieldsToCheck.put("surname", user.getSurname());
            fieldsToCheck.put("email", user.getUsername());
            fieldsToCheck.put("phoneNumber", user.getPhoneNumber());
            fieldsToCheck.put("password", user.getPassword());


            for (Map.Entry<String, String> set: fieldsToCheck.entrySet()) {
                if (StringUtils.isBlank(set.getValue())) {
                    return List.of(set.getKey(), "Field is empty");
                }
            }

            if (user.getDateOfBirth() == null) {
                return List.of("dob", "Field is empty");
            } else if (user.getRegistrationDate() == null) {
                return List.of("regDate", "Field is empty");
            }

            return List.of("success", "true");

        } catch (Exception e) {
            System.err.println(e + "\nUnknown error thrown during field check, please fix asap");
            return List.of("success", "true");
        }
    }

    /**
     * Returns {@code true} if passwords (or 2 strings) match
     * @param pass1
     * @param pass2
     * @return boolean
     */
    public boolean checkPasswordsMatch (String pass1, String pass2) {
        return pass1.equals(pass2);
    }


    /**
     * Saves a user to the repository. Also assigns a role of {@link AuthoritiesEnum#ROLE_MEMBER}
     * @param user
     * @return {@link User}
     */
    public User save (User user) {

        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDate.now());
        }

        User updatedUser = userRepository.save(user);

        if (updatedUser.getAuthorities() == null) {
            updatedUser.addAuthority(new Authority(1, AuthoritiesEnum.ROLE_MEMBER));
        }

        return userRepository.save(updatedUser);
    }

    /**
     * Goes through every field (excluding ID and password) and updates user information.
     * Ignores fields which have to be updated to null or empty
     * @param user
     * @throws Exception
     */
    public void updateUser(User user) throws Exception{

        try {
            int userId = user.getId();
        } catch (Exception e) {
            throw new UserNotFoundException("Tried to update a user who doesn't exist in the database. Save the user first");
        }


        Optional<User> userOptional = userRepository.findById(user.getId());

        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("Can't find user in the database");
        }

        User userDb = userOptional.get();

        if (StringUtils.isNotBlank(user.getName())) {
            userDb.setName(user.getName());
        }

        if (StringUtils.isNotBlank(user.getSurname())) {
            userDb.setSurname((user.getSurname()));
        }

        if (StringUtils.isNotBlank(user.getUsername())) {
            userDb.setUsername(user.getUsername());
        }

        if (user.getDateOfBirth() != null) {
            userDb.setDateOfBirth(user.getDateOfBirth());
        }

        if (StringUtils.isNotBlank(user.getPhoneNumber())) {
            userDb.setPhoneNumber(user.getPhoneNumber());
        }

        if (user.getRegistrationDate() != null) {
            userDb.setRegistrationDate(user.getRegistrationDate());
        }

        //TODO: Add functionality to ensure that values are unique
        //Adding user authority if both are null
        if (user.getAuthorities() == null && userDb.getAuthorities() == null) {
            userDb.setAuthorities(List.of(new Authority(1, AuthoritiesEnum.ROLE_MEMBER)));
            userRepository.save(userDb);
            return;
        }

        if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
            userDb.setAuthorities((List<Authority>)user.getAuthorities());
        }

        userRepository.save(userDb);
    }

    /**
     * Returns a user based on given ID
     * @param id
     * @return {@link Optional}&lt{@link User}&gt
     */
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }
}
