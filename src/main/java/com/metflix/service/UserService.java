package com.metflix.service;


import com.metflix.exceptions.EmailAlreadyExistsException;
import com.metflix.exceptions.PasswordsDontMatchException;
import com.metflix.exceptions.RegistrationFieldEmptyException;
import com.metflix.model.User;
import com.metflix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: try and see if I can tidy up the validateUser and associated methods

    /*
        This method validates the user registration, currently it does 3 things:
            1. checks if email already exists
            2. checks if there's any empty fields passed through
            3. checks if the passwords match
     */
    public void validateUser (User user, String password2) throws Exception {

        if (!emailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException("User validation failure, entered email already exists");

        } else if (checkUserContainsEmptyFields(user)){
            throw new RegistrationFieldEmptyException("User validation failure, one of the checked fields is empty");

        } else if (!checkPasswordsMatch(user.getPassword(), password2)) {
            throw new PasswordsDontMatchException("User validation failure, passwords dont match ");
        }

    }

    /** Returns true when email is already in a database */
    public boolean emailExists (String email) {
        return userRepository.findByEmail(email).isEmpty();
    }


    /** returns true when at least one field is empty */
    public boolean checkUserContainsEmptyFields (User user) {
        try{
            String[] fieldsToCheck = new String[]{
                    user.getName(),
                    user.getSurname(),
                    user.getEmail(),
                    user.getDob().toString(),
                    user.getPhoneNr(),
                    user.getPassword()
            };

            for (String field : fieldsToCheck) {
                if (field.trim().length() == 0) {
                    return true;
                }
            }
            return false;

        } catch (NullPointerException e) {
            return true;
        }

    }

    /** Returns true if passwords match */
    public boolean checkPasswordsMatch (String pass1, String pass2) {
        return pass1.equals(pass2);
    }



    public void save (User user) {
        userRepository.save(user);
        System.out.println("user added to database successfully");
    }
}
