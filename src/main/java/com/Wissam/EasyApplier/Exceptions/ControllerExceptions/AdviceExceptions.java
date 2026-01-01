package com.Wissam.EasyApplier.Exceptions.ControllerExceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.LiAtCookieInvalidException;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.LinkedinNotFoundException;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;

@RestControllerAdvice
public class AdviceExceptions {

  @ExceptionHandler(LinkedinNotFoundException.class)
  public ResponseEntity<String> handleLinkedinNotFoundException(LinkedinNotFoundException e) {
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
    return ResponseEntity.status(404).body(e.getMessage());
  }

  @ExceptionHandler(LiAtCookieInvalidException.class)
  public ResponseEntity<String> handleLiAtCookieInvalidException(LiAtCookieInvalidException e) {
    return ResponseEntity.status(404).body(e.getMessage());
  }

  // spring validation Exceptions
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();

    e.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.badRequest().body(errors);
  }

}
