package com.Wissam.EasyApplier.Automations.Linkedin;

import org.springframework.stereotype.Component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LinkedinScripts {

  private static final String YEARS_OF_EXPERIENCE = "3";

  // These should NOT be initialized yet
  private Locator dialog;
  private Locator nextButton;
  private Locator submitButton;
  private Locator reviewButton;
  private Locator dismissButton;
  private Locator selectFields;
  private Locator textInputFields;
  private Locator radioInputFields;

  public void EasyApplyScript(Page page) {
    if (page.locator("#jobs-apply-button-id").count() > 0) {
      page.waitForTimeout(200 + Math.random() * 1000);
      if (page.locator("button[aria-label^='Easy Apply to']").count() > 0) {
        page.locator("button[aria-label^='Easy Apply to']").first().click();
        log.info("Clicked on Easy Apply button");
        whichButtonCheck(page);
        log.info("Started application process");
      }
    }
  }

  private void initLocators(Page page) {
    dialog = page.locator("div[role='dialog']");
    nextButton = dialog.getByText("Next");
    submitButton = dialog.getByLabel("Submit application");
    reviewButton = dialog.getByText("Review");
    dismissButton = page.getByLabel("Dismiss");
    selectFields = dialog.locator("select");
    textInputFields = dialog.locator("input[type='text']");
    radioInputFields = dialog.locator("input[type='radio'][value='Yes']");
  }

  public void whichButtonCheck(Page page) {
    initLocators(page); // initialize locators after page exists

    if (submitButton.count() > 0) {
      log.info("Submit button found Easy dub");
      submitButton.first().click();
      dismissButton.first().click();
    }

    while (nextButton.isVisible() || submitButton.isVisible() || reviewButton.isVisible()) {
      log.info("One of the buttons is found checking and filling fields");
      fillFieldsAndSubmit();
      checkWhichButtonAndClick();
    }
  }

  public void fillFieldsAndSubmit() {

    // if (submitButton.isVisible()) {
    // log.info("Submit button found 1");
    // submitButton.first().click();
    // }

    if (dialog.getByText("Contact info").count() > 0 && submitButton.isVisible()) {
      if (submitButton.isVisible()) {
        log.info("Submit button found 2");
        submitButton.first().click();
        dismissButton.first().click();
      } else {
        log.info("Next button found 1");
        nextButton.first().click();
      }
    }

    // if (dialog.getByText("Resume").count() > 0 && submitButton.isVisible()) {
    // if (submitButton.isVisible()) {
    // log.info("Submit button found 3");
    // submitButton.first().click();
    // } else {
    // log.info("Next button found 2");
    // nextButton.first().click();
    // }
    // }

    if (selectFields.count() > 0) {
      log.info("Select field found");
      for (int i = 0; i < selectFields.count(); i++) {
        selectFields.nth(i).selectOption(new SelectOption().setIndex(1));
      }
    }

    // Fill text fields
    if (textInputFields.count() > 0) {
      log.info("Text field found");
      for (int i = 0; i < textInputFields.count(); i++) {
        textInputFields.nth(i).fill(YEARS_OF_EXPERIENCE);
      }
    }

    // Click radio fields
    if (radioInputFields.count() > 0) {
      log.info("Radio field found");
      for (int i = 1; i < radioInputFields.count(); i++) {
        log.info("Clicking radio field " + i);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          log.error("Thread sleep error");
        }
        radioInputFields.nth(i).check();
      }
    }
  }

  public void checkWhichButtonAndClick() {
    if (nextButton.isVisible()) {
      log.info("Next button found");
      nextButton.first().click();
    } else if (reviewButton.isVisible()) {
      log.info("Review button found");
      reviewButton.first().click();
    } else if (submitButton.isVisible()) {
      log.info("Submit button found");
      submitButton.first().click();
      dismissButton.first().click();
    }
  }

  private void clickDismissSafely(Page page) {
    Locator loader = page.locator("div.jobs-loader");

    if (loader.count() > 0) {
      loader.first().waitFor(new Locator.WaitForOptions()
          .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN)
          .setTimeout(10_000));
    }

    if (dismissButton.count() > 0) {
      dismissButton.first().click(new Locator.ClickOptions().setForce(true));
      log.info("Dismiss clicked");
    }
  }
}
