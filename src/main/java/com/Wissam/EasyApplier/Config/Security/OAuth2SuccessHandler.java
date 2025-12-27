package com.Wissam.EasyApplier.Config.Security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtils jwtUtils;
  private final UserRepository userRepo;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    log.info("OAuth2 login 1");

    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
    String email = oauthUser.getAttribute("email");
    log.info("Email: {}", email);

    log.info("OAuth2 login 2");
    User user = userRepo.findByEmail(email).orElseGet(() -> {
      User newUser = User.builder()
          .email(email)
          .uuid(UUID.randomUUID().toString())
          .verified(true)
          .role(UserRole.ROLE_USER)
          .build();
      return userRepo.save(newUser);
    });
    log.info("OAuth2 login 3");

    String token = jwtUtils.generateToken(user.getEmail());

    Cookie cookie = new Cookie("jwt", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60);

    log.info("OAuth2 login 4");
    response.addCookie(cookie);
    response.sendRedirect("http://localhost:8080/auth/hello");
  }
}
