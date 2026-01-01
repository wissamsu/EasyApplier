package com.Wissam.EasyApplier.Controller;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Wissam.EasyApplier.Config.Security.SecurityUtils.JwtUtils;
import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Events.Mail.EmailVerificationEvent;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IAuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
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
  private final ApplicationEventPublisher publisher;

  @GetMapping("/failure")
  public String failure() {
    return "fuck";
  }

  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }

  @PostMapping("/login")
  public boolean login(@RequestBody UserRequest userRequest, HttpServletResponse response) {
    String email = userRequest.getEmail();
    String password = userRequest.getPassword();
    String token = jwtUtils.generateToken(email);
    Cookie cookie = new Cookie("jwt", token);
    cookie.setMaxAge(3600);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    response.addCookie(cookie);
    return authService.login(email, password);

  }

  @PostMapping("/register")
  public String register(@RequestBody UserRequest userRequest) {
    String email = userRequest.getEmail();
    String password = userRequest.getPassword();
    String uuid = UUID.randomUUID().toString();
    publisher.publishEvent(new EmailVerificationEvent(email, uuid));
    return authService.register(email, password, uuid);
  }

  @GetMapping("/verify/{uuid}")
  public boolean verifyEmail(@PathVariable String uuid) {
    User user = userRepo.findByUuid(uuid)
        .orElseThrow(() -> new UserNotFoundException("User with uuid " + uuid + " not found"));

    user.setVerified(true);
    authService.verifyUser(user);
    return true;
  }

}
