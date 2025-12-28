package com.Wissam.EasyApplier.Services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authManager;

  @Override
  public boolean login(String email, String password) {
    User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    boolean isValid = passwordEncoder.matches(password, user.getPassword());
    if (isValid && user.isVerified()) {
      Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
      SecurityContextHolder.getContext().setAuthentication(auth);
      return true;
    }
    return false;
  }

  @Override
  public String register(String email, String password, String uuid) {
    if (userRepo.existsByEmail(email)) {
      return "User with email " + email + "already exists";
    }
    userRepo
        .save(User.builder().email(email).password(passwordEncoder.encode(password)).uuid(uuid).role(UserRole.ROLE_USER)
            .build());
    return "User registered successfully";
  }

  @Override
  public void verifyUser(User user) {
    user.setVerified(true);
    userRepo.save(user);
  }

}
