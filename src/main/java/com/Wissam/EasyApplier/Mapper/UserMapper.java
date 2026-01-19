package com.Wissam.EasyApplier.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
    LinkedinMapper.class, HandshakeMapper.class })
public interface UserMapper {

  UserResponse toUserResponse(User user);

  @Mapping(target = "jobAnswer", ignore = true)
  @Mapping(target = "password", ignore = true)
  User toUser(UserResponse userResponse);

  UserRequest toUserRequest(User user);

  @Mappings({
      @Mapping(target = "linkedin", ignore = true),
      @Mapping(target = "handshake", ignore = true),
      @Mapping(target = "jobAnswer", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "updatedAt", ignore = true),
      @Mapping(target = "uuid", ignore = true),
      @Mapping(target = "verified", ignore = true),
      @Mapping(target = "role", ignore = true),
      @Mapping(target = "id", ignore = true)
  })
  User toUser(UserRequest userRequest);

  List<UserResponse> toUserResponseList(List<User> users);

  @Mappings({
      @Mapping(target = "linkedin", ignore = true),
      @Mapping(target = "handshake", ignore = true),
      @Mapping(target = "jobAnswer", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "updatedAt", ignore = true),
      @Mapping(target = "uuid", ignore = true),
      @Mapping(target = "verified", ignore = true),
      @Mapping(target = "role", ignore = true),
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "authorities", ignore = true)
  })
  void updateUserFromRequest(@MappingTarget User user, UserRequest userRequest);

}
