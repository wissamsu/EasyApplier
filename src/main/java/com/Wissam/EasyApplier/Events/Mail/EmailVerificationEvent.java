package com.Wissam.EasyApplier.Events.Mail;

import java.util.UUID;

public record EmailVerificationEvent(String email, UUID uuid) {
}
