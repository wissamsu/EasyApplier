package com.Wissam.EasyApplier.Config.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Enums.UserRole;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;

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
  @Value("${frontend.host.url}")
  private String frontendHostUrl;
  @Value("${jwt.expiration}")
  private long jwtExpirationMs;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    log.info("OAuth2 login 1");

    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
    String email = oauthUser.getAttribute("email");
    log.info("Email: {}", email);

    log.info("OAuth2 login 2");
    if (email == null || email.isEmpty()) {
      response.sendRedirect(frontendHostUrl + "/Home");
      return;
    }
    User user = userRepo.findByEmail(email).orElseGet(() -> {
      User newUser = User.builder()
          .email(email)
          .verified(true)
          .role(UserRole.ROLE_USER)
          .build();
      return userRepo.save(newUser);
    });
    log.info("OAuth2 login 3");

    String token = jwtUtils.generateToken(user.getEmail());

    log.info("OAuth2 login 4");
    ResponseCookie cookie = ResponseCookie.from("jwt", token)
        .httpOnly(true)
        .secure(request.isSecure())
        .sameSite("Lax")
        .path("/")
        .maxAge(Math.max(1, jwtExpirationMs / 1000))
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    response.sendRedirect(frontendHostUrl + "/Home");
  }
}
