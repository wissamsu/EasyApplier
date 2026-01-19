package com.Wissam.EasyApplier.Services.IServices;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Model.User;

public interface IHandshakeService {

  HandshakeResponse getHandshake(User user);

  HandshakeResponse getHandshakeByEmail(String email);

  HandshakeResponse createHandshake(HandshakeRequest handshakeRequest, User user);

  HandshakeResponse updateHandshake(HandshakeRequest handshakeRequest, User user);

}
