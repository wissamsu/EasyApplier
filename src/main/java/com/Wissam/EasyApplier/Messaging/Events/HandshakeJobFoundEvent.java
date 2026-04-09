package com.Wissam.EasyApplier.Messaging.Events;

import java.util.UUID;

public record HandshakeJobFoundEvent(
    String jobId,
    String jobTitle,
    String jobImageLink,
    String jobLink,
    String jobLocation,
    String jobCompany,
    UUID userUuid) {
}
