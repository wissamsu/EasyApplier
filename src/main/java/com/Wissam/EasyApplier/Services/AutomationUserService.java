package com.Wissam.EasyApplier.Services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.UserNotFoundException;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutomationUserService {

  private final UserRepository userRepository;

  public User getRequiredAutomationUser(UUID userUuid) {
    return userRepository.findAutomationUserByUuid(userUuid)
        .orElseThrow(() -> new UserNotFoundException("User with uuid " + userUuid + " not found"));
  }

}
