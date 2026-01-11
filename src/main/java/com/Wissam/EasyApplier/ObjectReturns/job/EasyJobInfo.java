package com.Wissam.EasyApplier.ObjectReturns.job;

import com.Wissam.EasyApplier.Model.User;

public record EasyJobInfo(
    String jobId,
    String jobTitle,
    String jobImageLink,
    String jobLink,
    String jobLocation,
    String jobCompany,
    User user) {
}
