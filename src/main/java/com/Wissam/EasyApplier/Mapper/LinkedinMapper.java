package com.Wissam.EasyApplier.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinRequest;
import com.Wissam.EasyApplier.Dto.Linkedin.LinkedinResponse;
import com.Wissam.EasyApplier.Model.Linkedin;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LinkedinMapper {

  LinkedinResponse toLinkedinResponse(Linkedin linkedin);

  LinkedinRequest toLinkedinRequest(Linkedin linkedin);

  Linkedin toLinkedin(LinkedinRequest linkedinRequest);

  Linkedin toLinkedin(LinkedinResponse linkedinResponse);

}
