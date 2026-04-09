package com.Wissam.EasyApplier.Messaging.Events;

import java.util.UUID;

public record EmailVerificationRequestedEvent(String email, UUID verificationToken) {
}
