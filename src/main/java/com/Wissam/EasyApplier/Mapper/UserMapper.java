package com.Wissam.EasyApplier.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.Wissam.EasyApplier.Dto.User.UserRequest;
import com.Wissam.EasyApplier.Dto.User.UserResponse;
import com.Wissam.EasyApplier.Model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  UserResponse toUserResponse(User user);

  User toUser(UserResponse userResponse);

  UserRequest toUserRequest(User user);

  User toUser(UserRequest userRequest);

}
