package com.Wissam.EasyApplier.Email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JavaMailServiceImpl {
  private final JavaMailSender javaMailSender;
  private final TemplateEngine templateEngine;

  @Async
  public void sendConfirmSignUpEmail(String to, String confirmationUrl) {
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      Context context = new Context();
      context.setVariable("verificationUrl", confirmationUrl);
      String text = templateEngine.process("VerifyEmail", context);
      helper.setPriority(1);
      helper.setTo(to);
      helper.setText(text, true);
      helper.setSubject("Confirm Your EasyApplier Account");
      helper.setFrom("team@easyapplier.com", "EasyApplier Team");
      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
