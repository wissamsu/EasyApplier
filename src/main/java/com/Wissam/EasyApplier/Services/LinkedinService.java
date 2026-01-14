package com.Wissam.EasyApplier.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.LinkedinNotFoundException;
import com.Wissam.EasyApplier.Mapper.LinkedinMapper;
import com.Wissam.EasyApplier.Model.Linkedin;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.LinkedinRepository;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.ILinkedinService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LinkedinService implements ILinkedinService {

  private final LinkedinRepository linkedinRepo;
  private final LinkedinMapper linkedinMapper;
  private final UserRepository userRepo;

  @Override
  @Transactional(readOnly = true)
  public LinkedinResponse getLinkedinById(Long id) {
    return linkedinMapper.toLinkedinResponse(linkedinRepo.findById(id)
        .orElseThrow(() -> new LinkedinNotFoundException("Linkedin with id " + id + " not found")));
  }

  @Override
  @Transactional(readOnly = true)
  public LinkedinResponse getLinkedinByEmail(String email) {
    return linkedinMapper.toLinkedinResponse(linkedinRepo.findByEmail(email)
        .orElseThrow(() -> new LinkedinNotFoundException("Linkedin with email " + email + " not found")));
  }

  @Override
  @Transactional
  public LinkedinResponse createLinkedinByUserId(LinkedinRequest linkedinRequest, Long userId) {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new LinkedinNotFoundException("User with id " + userId + " not found"));
    Linkedin linkedin = linkedinMapper.toLinkedin(linkedinRequest);
    linkedin.setUser(user);
    user.setLinkedin(linkedin);
    userRepo.save(user);
    return linkedinMapper.toLinkedinResponse(linkedin);
  }

  @Override
  @Transactional
  public LinkedinResponse addLi_AtCookie(User user, String liAtCookie) {
    Linkedin linkedin = user.getLinkedin();
    linkedin.setLiatCookie(liAtCookie);
    linkedinRepo.save(linkedin);
    return linkedinMapper.toLinkedinResponse(linkedin);
  }

}
