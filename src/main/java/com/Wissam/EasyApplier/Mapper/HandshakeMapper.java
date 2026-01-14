package com.Wissam.EasyApplier.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.Wissam.EasyApplier.Dto.Handshake.HandshakeRequest;
import com.Wissam.EasyApplier.Dto.Handshake.HandshakeResponse;
import com.Wissam.EasyApplier.Model.Handshake;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HandshakeMapper {

  HandshakeResponse toHandshakeResponse(Handshake handshake);

  HandshakeRequest toHandshakeRequest(Handshake handshake);

  Handshake toHandshake(HandshakeRequest handshakeRequest);

  Handshake toHandshake(HandshakeResponse handshakeResponse);

}
