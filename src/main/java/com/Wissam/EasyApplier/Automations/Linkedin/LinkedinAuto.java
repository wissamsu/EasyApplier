package com.Wissam.EasyApplier.Automations.Linkedin;

import java.util.List;

import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.User;
import com.Wissam.EasyApplier.Utils.LinkedinUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkedinAuto {

  private final LinkedinUtils linkedinUtils;
  private final Browser browser;

  public void jobAutomatorById(String jobId, User user) {
    linkedinUtils.checkOrgetLiAtCookie(user);
    try (
        BrowserContext context = browser.newContext();) {
      Cookie cookie = new Cookie("li_at", user.getLinkedin().getLiatCookie());
      cookie.setUrl("https://www.linkedin.com");
      cookie.setSecure(true);
      cookie.setHttpOnly(true);
      context.addCookies(List.of(cookie));
      Page page = context.newPage();
      page.navigate("https://www.linkedin.com/jobs/view/" + jobId);
      page.waitForLoadState(LoadState.DOMCONTENTLOADED);
      page.getByText("Easy Apply").first().click();
      Locator dialog = page.locator("div[role='region']").first();
      page.waitForSelector("button.artdeco-button.artdeco-button--2.artdeco-button--primary.ember-view");
      while (dialog.locator("button.artdeco-button.artdeco-button--2.artdeco-button--primary.ember-view").count() > 0) {
        if (page.getByText("Application sent").count() > 0) {
          log.info("Application sent");
          break;
        }
        dialog.locator("button.artdeco-button.artdeco-button--2.artdeco-button--primary.ember-view").click();

        if (dialog.locator("select").count() > 0) {
          log.info("Select field found");
          for (int i = 0; i < dialog.locator("select").count(); i++) {
            dialog.locator("select").nth(i).selectOption(new SelectOption().setIndex(1));
          }
        }

        if (dialog.locator("input[type='text']").count() > 0) {
          log.info("Text field found");
          for (int i = 0; i < dialog.locator("input[type='text']").count(); i++) {
            dialog.locator("input[type='text']").nth(i).fill("3");
          }
        }

        if (dialog.locator("label[data-test-text-selectable-option__label='Yes']").count() > 0) {
          log.info("Checkbox field found");
          for (int i = 0; i < dialog.locator("label[data-test-text-selectable-option__label='Yes']").count(); i++) {
            dialog.locator("label[data-test-text-selectable-option__label='Yes']").nth(i).click();
          }
        }

      }

    } catch (Exception e) {
      log.error("Error while automating jobs: " + e.getMessage());
    }
  }

  public void autoConnect(User user) {
    try (
        BrowserContext context = browser.newContext();) {
      Cookie cookie = new Cookie("li_at", user.getLinkedin().getLiatCookie());
      cookie.setUrl("https://www.linkedin.com");
      cookie.setSecure(true);
      cookie.setHttpOnly(true);
      context.addCookies(List.of(cookie));
      Page page = context.newPage();

    } catch (Exception e) {
      log.error("Error while automating jobs: " + e.getMessage());
    }
  }

}
