package com.Wissam.EasyApplier.Services.IServices;

import com.Wissam.EasyApplier.Model.User;

public interface IAuthService {

  boolean login(String email, String password);

  String register(String email, String password, String uuid);

  void verifyUser(User user);

}
