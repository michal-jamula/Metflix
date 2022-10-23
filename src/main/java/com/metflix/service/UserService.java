package com.metflix.service;


import com.metflix.exceptions.EmailAlreadyExistsException;
import com.metflix.exceptions.PasswordsDontMatchException;
import com.metflix.exceptions.RegistrationFieldEmptyException;
import com.metflix.model.User;
import com.metflix.model.UserStatus;
import com.metflix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }





    /**
        This method validates the user registration, currently it does 3 things:
            1. checks if email already exists
            2. checks if there's any empty fields passed through
            3. checks if the passwords match

        In the future this will probably get replaced by spring-boot-validator
     */
    public List<String> validateUser (User user, String password2) {

        HashMap<String, String> map = new HashMap<>();
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
    /** returns "success" when fields are completed, otherwise returns a <field, error message> both as String */
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
            user.setStatus(UserStatus.unsubscribed);
        }

        userRepository.save(user);
        System.out.println("user added to database successfully");
    }
}
