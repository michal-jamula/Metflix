package com.metflix.service;


import com.metflix.model.User;
import com.metflix.model.modelEnum.UserStatusEnum;
import com.metflix.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    //TODO: Pretty sure this method is meant to be part of a DTO.

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

        } else if (emailExists(user.getEmail())){
            return List.of("email", "Email already exists");

        } else if (!checkPasswordsMatch(user.getPassword(), password2)) {
            return List.of("password", "Passwords do not match");
        }
        return List.of("success", "true");
    }

    /** Returns true when email is already in a database */
    public boolean emailExists (String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    //TODO: Currently the dob only gets checked if empty or not, it doesn't check the format of the string
    /** returns <"success", "true"> when fields are completed, otherwise returns a <field, error message> both as String */
    public List<String> checkUserContainsEmptyFields (User user) {
        try{
            HashMap<String, String> fieldsToCheck = new HashMap<String, String>();
            fieldsToCheck.put("name", user.getName());
            fieldsToCheck.put("surname", user.getSurname());
            fieldsToCheck.put("email", user.getEmail());
            fieldsToCheck.put("dob", user.getDob().toString());
            fieldsToCheck.put("phoneNr", user.getPhoneNr());
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

        if (user.getRegDate() == null) {
            user.setRegDate(LocalDate.now());
        }

        if(user.getStatus() == null) {
            user.setStatus(UserStatusEnum.unsubscribed);
        }

        userRepository.save(user);
        System.out.println("user added to database successfully");
    }


    public User updateUserWithId(User user, Integer userId) {

        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()) {
            System.err.println("Tried to find user with ID, but no user with id: " + userId + " exists!");
            return null;
        }

        User userDb = userOptional.get();

        if (!user.getName().isBlank()) {
            userDb.setName(user.getName());
        }

        if (!user.getSurname().isBlank()) {
            userDb.setSurname((user.getSurname()));
        }

        if (!user.getEmail().isBlank()) {
            userDb.setEmail(user.getEmail());
        }

        if (!user.getDob().equals(null)) {
            userDb.setDob(user.getDob());
        }

        if (!user.getPhoneNr().isBlank()) {
            userDb.setPhoneNr(user.getPhoneNr());
        }

        if (!user.getRegDate().equals(null)) {
            userDb.setRegDate(user.getRegDate());
        }

        if (user.getStatus() != null) {
            userDb.setStatus(user.getStatus());
        }


        return userRepository.save(userDb);
    }
}
