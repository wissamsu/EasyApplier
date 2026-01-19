package com.Wissam.EasyApplier.Services.IServices;

import java.util.UUID;

import com.Wissam.EasyApplier.Model.User;

public interface IAuthService {

  boolean login(String email, String password);

  String register(String email, String password, UUID uuid);

  void verifyUser(User user);

}
