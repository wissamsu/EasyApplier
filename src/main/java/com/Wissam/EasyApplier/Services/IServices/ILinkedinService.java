package com.Wissam.EasyApplier.Services.IServices;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Model.User;

public interface ILinkedinService {

  LinkedinResponse getLinkedinById(Long id);

  LinkedinResponse getLinkedinByEmail(String email);

  LinkedinResponse createLinkedin(LinkedinRequest linkedinRequest, User user);

  LinkedinResponse addLi_AtCookie(User user, String liAtCookie);

}
