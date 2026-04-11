package com.Wissam.EasyApplier.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Messaging.KafkaEventPublisher;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IAuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth")
public class AuthController {

  private final IAuthService authService;
  private final JwtUtils jwtUtils;
  private final UserRepository userRepo;
  private final KafkaEventPublisher kafkaEventPublisher;
  @Value("${jwt.expiration}")
  private long jwtExpirationMs;

  @GetMapping("/failure")
  public String failure() {
    return "Authentication failed";
  }

  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }

  @GetMapping("/auth-status")
  public boolean checkAuthentication() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()
        || auth.getPrincipal().equals("anonymousUser")) {
      return false;
    }
    return true;
  }

  @PostMapping("/login")
  public boolean login(@RequestParam String email, @RequestParam String password, HttpServletRequest request,
      HttpServletResponse response) {
    boolean authenticated = authService.login(email, password);

    if (authenticated) {
      String token = jwtUtils.generateToken(email);
      ResponseCookie cookie = ResponseCookie.from("jwt", token)
          .httpOnly(true)
          .secure(request.isSecure())
          .sameSite("Lax")
          .path("/")
          .maxAge(Math.max(1, jwtExpirationMs / 1000))
          .build();
      response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    return authenticated;

  }

  @PostMapping("/register")
  public String register(@RequestParam String email, @RequestParam String password) {
    if (userRepo.existsByEmail(email)) {
      return "If the email is available, a verification email will be sent";
    }
    UUID uuid = UUID.randomUUID();
    String result = authService.register(email, password, uuid);
    kafkaEventPublisher.publishEmailVerificationRequested(email, uuid);
    return result;
  }

  @GetMapping("/verify/{uuid}")
  public boolean verifyEmail(@PathVariable UUID uuid) {
    User user = userRepo.findByUuid(uuid)
        .orElseThrow(() -> new UserNotFoundException("User with uuid " + uuid + " not found"));

    user.setVerified(true);
    authService.verifyUser(user);
    return true;
  }

}
