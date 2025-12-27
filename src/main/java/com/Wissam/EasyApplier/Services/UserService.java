package com.Wissam.EasyApplier.Services;

import org.springframework.stereotype.Service;

import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

  private final UserRepository userRepo;

}
