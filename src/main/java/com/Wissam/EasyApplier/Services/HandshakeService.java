package com.Wissam.EasyApplier.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Exceptions.ServiceExceptions.HandshakeNotFoundException;
import com.Wissam.EasyApplier.Mapper.HandshakeMapper;
import com.Wissam.EasyApplier.Model.Handshake;
import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Repository.HandshakeRepository;
import com.Wissam.EasyApplier.Repository.UserRepository;
import com.Wissam.EasyApplier.Services.IServices.IHandshakeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HandshakeService implements IHandshakeService {

  private final HandshakeMapper handshakeMapper;
  private final HandshakeRepository handshakeRepo;
  private final UserRepository userRepo;

  @Override
  @Transactional(readOnly = true)
  public HandshakeResponse getHandshakeById(Long id) {
    return handshakeMapper.toHandshakeResponse(handshakeRepo.findById(id)
        .orElseThrow(() -> new HandshakeNotFoundException("Handshake with id " + id + " not found")));
  }

  @Override
  @Transactional(readOnly = true)
  public HandshakeResponse getHandshakeByEmail(String email) {
    return handshakeMapper.toHandshakeResponse(handshakeRepo.findByEmail(email)
        .orElseThrow(() -> new HandshakeNotFoundException("Handshake with email " + email + " not found")));
  }

  @Override
  @Transactional
  public HandshakeResponse createHandshakeByUserId(HandshakeRequest handshakeRequest, Long userId) {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new HandshakeNotFoundException("User with id " + userId + " not found"));
    Handshake handshake = handshakeMapper.toHandshake(handshakeRequest);
    handshake.setUser(user);
    user.setHandshake(handshake);
    userRepo.save(user);
    return handshakeMapper.toHandshakeResponse(handshake);
  }

}
