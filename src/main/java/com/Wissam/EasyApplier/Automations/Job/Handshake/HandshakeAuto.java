package com.Wissam.EasyApplier.Automations.Job.Handshake;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Utils.HandshakeUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandshakeAuto {

  private final Browser browser;
  private final HandshakeUtils handshakeUtils;
  private ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();

  @Async
  public void jobsExtractor(String jobTitle, User user) {
    UUID userId = UUID.fromString(user.getUuid());
    Object lock = locks.computeIfAbsent(userId, id -> new Object());
    synchronized (lock) {
      Path statePath = handshakeUtils.getContextPath(userId);
      // first 25 jobs
      try (BrowserContext context = handshakeUtils.createOrLoadContext(statePath, browser);
          Page page = context.newPage();) {
        page.navigate("https://app.joinhandshake.com/explore");
        page.waitForTimeout(10000);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

}
