package com.Wissam.EasyApplier.Services.IServices;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Model.User;

public interface ILinkedinService {

  LinkedinResponse getLinkedinById(Long id);

  LinkedinResponse getLinkedinByEmail(String email);

  LinkedinResponse createLinkedinByUserId(LinkedinRequest linkedinRequest, Long userId);

  LinkedinResponse addLi_AtCookie(User user, String liAtCookie);

}
