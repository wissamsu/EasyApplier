package com.Wissam.EasyApplier.ObjectReturns.job;

import com.Wissam.EasyApplier.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record LinkedinEasyJobInfo(
    String jobId,
    String jobTitle,
    String jobImageLink,
    String jobLink,
    String jobLocation,
    String jobCompany,
    @JsonIgnore User user) {
}
