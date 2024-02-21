package com.metflix.service;


import com.metflix.exceptions.EmailAlreadyExistsException;
import com.metflix.exceptions.PasswordsDontMatchException;
import com.metflix.exceptions.UserFieldIsEmptyException;
import com.metflix.model.Authority;
import com.metflix.model.Enums.AuthoritiesEnum;
import com.metflix.model.User;
import com.metflix.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Locates a {@link User} based on a username
     *
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
     *
     * @param pageNumber
     * @param pageSize
     * @param sortField
     * @param sortDirection
     * @return {@link Page}&lt{@link User}&gt
     */
    public Page<User> findPaginated(final int pageNumber, final int pageSize,
                                    final String sortField, final String sortDirection) {

        Sort sort;
        if (sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            sort = Sort.by(sortField).ascending();
        } else {
            sort = Sort.by(sortField).descending();
        }

        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return userRepository.findAll(pageable);
    }

    /**
     * Validates a {@link User} by checking for empty fields, existing email and matching password.<br>
     */
    public void validateUser(User user, String password2) throws EmailAlreadyExistsException, PasswordsDontMatchException, UserFieldIsEmptyException {

        if (emailExists(user.getUsername())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (!checkPasswordsMatch(user.getPassword(), password2)) {
            throw new PasswordsDontMatchException("Password does not match");
        }

        try {
            checkUserContainsEmptyFields(user);
        } catch (UserFieldIsEmptyException e) {
            if (e.getMessage().equals("Registration date is empty")) {
                user.setRegistrationDate(LocalDate.now());
            } else {
                throw e;
            }

        }
    }

    /**
     * Returns {@code true} when email is already in a database
     *
     * @param email
     * @return boolean
     */
    public boolean emailExists(String email) {
        return userRepository.findByUsername(email).isPresent();
    }

    //TODO: Currently the dateOfBirth only gets checked if empty or not, it doesn't check the format of the string

    /**
     * Checks if {@link User} contains empty fields, excludes ID. Throws UserRegistrationException if a field is wrong
     */
    private void checkUserContainsEmptyFields(User user) throws UserFieldIsEmptyException {
        HashMap<String, String> fieldsToCheck = new HashMap<>();
        fieldsToCheck.put("name", user.getName());
        fieldsToCheck.put("surname", user.getSurname());
        fieldsToCheck.put("email", user.getUsername());
        fieldsToCheck.put("phone", user.getPhoneNumber());
        fieldsToCheck.put("password", user.getPassword());


        for (Map.Entry<String, String> set : fieldsToCheck.entrySet()) {
            if (StringUtils.isBlank(set.getValue())) {
                throw new UserFieldIsEmptyException(String.format("%s field is empty", StringUtils.capitalize(set.getKey())));
            }
        }

        if (user.getDateOfBirth() == null) {
            throw new UserFieldIsEmptyException("Date of birth is empty");
        } else if (user.getRegistrationDate() == null) {
            throw new UserFieldIsEmptyException("Registration date is empty");
        }
    }

    /**
     * Returns {@code true} if passwords (or 2 strings) match
     *
     * @param pass1
     * @param pass2
     * @return boolean
     */
    public boolean checkPasswordsMatch(String pass1, String pass2) {
        return pass1.equals(pass2);
    }


    /**
     * Saves a user to the repository. Also assigns a role of {@link AuthoritiesEnum#ROLE_MEMBER}
     *
     * @param user
     * @return {@link User}
     */
    public User save(User user) {

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
     *
     * @param user
     * @throws Exception
     */
    public void updateUser(User user) {

        try {
            int userId = user.getId();
        } catch (Exception e) {
            System.out.println("Tried to update a user who doesn't exist in the database. Save the user first");
            return;
        }


        Optional<User> userOptional = userRepository.findById(user.getId());

        if (userOptional.isEmpty()) {
            System.out.println("Tried to update a user who doesn't exist in the database. Save the user first");
            return;
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
            userDb.setAuthorities((List<Authority>) user.getAuthorities());
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

    /**
     * Pulls user's data from the database and updates his Session info
     *
     * @param userId user's ID
     */
    public void refreshUserSessionDetails(int userId) {

        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            System.out.println("Tried to refresh wrong user's information!");
            return;
        }

        User user = userOptional.get();

        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuthentication != null && currentAuthentication.isAuthenticated()
                && currentAuthentication.getPrincipal() instanceof User) {

            User authUser = (User) currentAuthentication.getPrincipal();
            authUser.setAuthorities((List<Authority>) user.getAuthorities());


            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                    (Object) authUser,
                    currentAuthentication.getCredentials(),
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        }

    }
}
