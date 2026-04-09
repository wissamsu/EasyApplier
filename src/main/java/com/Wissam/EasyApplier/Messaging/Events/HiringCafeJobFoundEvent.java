package com.Wissam.EasyApplier.Messaging.Events;

import java.util.UUID;

public record HiringCafeJobFoundEvent(
    String jobId,
    String jobTitle,
    String jobImageLink,
    String jobLink,
    String jobLocation,
    String jobCompany,
    UUID userUuid) {
}
