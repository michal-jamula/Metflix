package com.metflix.auth;


import com.metflix.model.User;
import com.metflix.repositories.UserRepository;
import com.metflix.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;

  //TODO: This needs to be done
//  public AuthenticationResponse register(RegisterRequest request) {
//    var user = User.builder()
//        .firstname(request.getFirstname())
//        .lastname(request.getLastname())
//        .email(request.getEmail())
//        .password(passwordEncoder.encode(request.getPassword()))
//        .role(Role.USER)
//        .build();
//    repository.save(user);
//    var jwtToken = tokenService.generateToken(user);
//    return AuthenticationResponse.builder()
//        .token(jwtToken)
//        .build();
//  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
    } catch (BadCredentialsException e) {
      System.err.println("Bad credentials, user not logged in!");
    }

    var user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Username/password combination is wrong"));

    var jwtToken = tokenService.generateToken(user);


    System.err.println("Token generated: " + jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }
}
