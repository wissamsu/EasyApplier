package com.Wissam.EasyApplier.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Model.Linkedin;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LinkedinMapper {

  LinkedinResponse toLinkedinResponse(Linkedin linkedin);

  LinkedinRequest toLinkedinRequest(Linkedin linkedin);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "liatCookie", ignore = true)
  @Mapping(target = "user", ignore = true)
  Linkedin toLinkedin(LinkedinRequest linkedinRequest);

  @Mapping(target = "liatCookie", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "password", ignore = true)
  Linkedin toLinkedin(LinkedinResponse linkedinResponse);

}
