package com.Wissam.EasyApplier.Services.IServices;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;

public interface IHandshakeService {

  HandshakeResponse getHandshakeById(Long id);

  HandshakeResponse getHandshakeByEmail(String email);

  HandshakeResponse createHandshakeByUserId(HandshakeRequest handshakeRequest, Long userId);

}
