package com.Wissam.EasyApplier.Automations.Job.HiringCafe;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Model.User;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.LoadState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HiringCafeAuto {

  private final ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();
  private final Browser browser;

  @Async
  public void jobAutomatorById(User user, String jobLink) {
    UUID uuid = user.getUuid();
    Object lock = locks.computeIfAbsent(uuid, id -> new Object());
    synchronized (lock) {
      try (
          BrowserContext ctx = browser.newContext();
          Page page = ctx.newPage();) {

        page.navigate(jobLink);
        Page page2 = page.waitForPopup(() -> {
          page.getByText("Apply now").first().click();
        });
        page2.waitForLoadState(LoadState.NETWORKIDLE);

        Locator acceptCookies = page2.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName(Pattern.compile("Accept all", Pattern.CASE_INSENSITIVE)));

        if (acceptCookies.count() > 0) {
          acceptCookies.first().click();
        }

        if (page2.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName(Pattern.compile("Apply", Pattern.CASE_INSENSITIVE))).count() > 0) {
          page2.getByRole(AriaRole.BUTTON,
              new Page.GetByRoleOptions().setName(Pattern.compile("Apply", Pattern.CASE_INSENSITIVE))).first().click();
          page2.waitForLoadState(LoadState.NETWORKIDLE);
        }

        Locator confirm1 = page2.locator("input[value='I Confirm']");
        Locator confirm2 = page2.locator("input[title='I Confirm']");

        Locator firstName = page2.getByLabel("First Name");
        Locator lastName = page2.getByLabel("Last Name");
        Locator firstName2 = page2.getByLabel("Name", new Page.GetByLabelOptions().setExact(true));
        Locator date = page2.getByLabel("Date");
        Locator email = page2.getByLabel("Email Address");
        Locator email2 = page2.getByLabel("Email");
        Locator phone = page2.getByLabel("Phone");
        Locator Address = page2.getByLabel("Address");
        Locator city = page2.getByLabel("City");
        Locator city2 = page2.getByText("Location (City)");
        Locator state = page2.getByLabel("State");
        Locator postal = page2.getByPlaceholder("Postal");
        Locator linkedinProfileUrl = page2.getByLabel("LinkedIn profile");
        Locator preferredName = page2.getByLabel("Preferred");
        Locator resumeUpload = page2.locator("input[type='file']");

        Locator country = page2.getByLabel("country");
        Locator workStatus = page2.getByLabel("work status");
        Locator clearance = page2.getByLabel("clearance");
        Locator imNotARobot = page2.getByText("I'm not a robot");
        Locator radioInput = page2.locator("input[type='radio']");
        // Option tag that has text male find by option tag
        Locator gender = page2.locator("xpath=//option[normalize-space(text())='Male']");
        Locator genderSelect = gender.locator("xpath=ancestor::select");

        Locator race = page2.locator("xpath=//option[normalize-space(text())='White, not Hispanic or Latino']");
        Locator raceSelect = race.locator("xpath=ancestor::select");

        // get all <select> tags
        Locator allSelects = page2.locator("select[class]");

        // get all with text Yes
        Locator checkBox = page2.locator("label:has(input[type='checkbox'])");
        Locator requireSponsorshipCheckBox = page2.locator(
            "xpath=//label[contains(text(), 'sponsorship')]/following-sibling::label[.//text()[contains(., 'No')]]");
        // Veteran/Disability
        Locator protectedVeteran = page2.getByLabel("I AM NOT A PROTECTED VETERAN");
        Locator disability = page2.getByLabel("NO, I DO NOT HAVE A DISABILITY AND HAVE NOT HAD ONE IN THE PAST");

        Locator submit = page2.getByText("Submit");
        Locator submitApplication = page2.getByText("submit application");

        if (confirm1.count() > 0) {
          confirm1.first().click();
        }

        if (confirm2.count() > 0) {
          confirm2.first().click();
        }
        if (firstName.count() > 0) {
          firstName.first().fill(user.getFirstName());
        }

        if (firstName2.count() > 0) {
          firstName2.first().fill(user.getFirstName() + " " + user.getLastName());
        }

        if (lastName.count() > 0) {
          lastName.first().fill(user.getLastName());
        }

        if (date.count() > 0) {
          date.first().fill(LocalDate.now().toString());
        }

        if (linkedinProfileUrl.count() > 0) {
          linkedinProfileUrl.first().fill(user.getJobAnswer().getLinkedinProfileUrl());
        }

        if (preferredName.count() > 0) {
          preferredName.first().fill(user.getFirstName());
        }

        if (email.count() > 0) {
          email.first().fill(user.getEmail());
        }

        if (email2.count() > 0) {
          email2.first().fill(user.getEmail());
        }

        if (phone.count() > 0) {
          phone.first().fill(user.getPhoneNumber());
        }

        if (Address.count() > 0) {
          Address.first().fill(user.getJobAnswer().getAddress());
        }

        if (city.count() > 0) {
          city.first().fill(user.getJobAnswer().getCity());
        }
        if (state.count() > 0) {
          state.first().fill(user.getJobAnswer().getState());
        }

        if (postal.count() > 0) {
          postal.first().fill(user.getJobAnswer().getZipCode());
        }

        if (resumeUpload.count() > 0) {
          HttpClient client = HttpClient.newHttpClient();

          HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(
                  user.getResumeLink()))
              .GET()
              .build();

          byte[] fileBytes = client
              .send(request, HttpResponse.BodyHandlers.ofByteArray())
              .body();

          FilePayload payload = new FilePayload(
              "WissamResume.pdf",
              "application/pdf",
              fileBytes);
          resumeUpload.first().setInputFiles(payload);
          if (resumeUpload.nth(1).count() > 0) {
            resumeUpload.nth(1).setInputFiles(payload);
          }
        }
        if (country.count() > 0) {
          for (Locator i : country.all()) {
            i.first().pressSequentially(user.getJobAnswer().getCountry());
            i.press("Enter");
          }
        }

        if (workStatus.count() > 0) {
          for (Locator i : workStatus.all()) {
            if (user.getJobAnswer().getCountry().equals("United States")) {
              i.first().pressSequentially("US Citizen");
              i.press("Enter");
            } else {
              i.first().pressSequentially("Permanent Resident");
              i.press("Enter");
            }
          }
        }
        if (clearance.count() > 0) {
          for (Locator i : clearance.all()) {
            i.first().pressSequentially("Public");
            i.press("Enter");
          }
        }
        if (city2.count() > 0) {
          city2.first().fill(user.getJobAnswer().getCity());
        }

        if (requireSponsorshipCheckBox.count() > 0) {
          requireSponsorshipCheckBox.first().click();
        }
        if (gender.count() > 0) {
          genderSelect.selectOption("Male");
        }

        if (race.count() > 0) {
          raceSelect.selectOption("White, not Hispanic or Latino");
        }

        // Veteran/Disability
        if (protectedVeteran.count() > 0) {
          protectedVeteran.first().click();
        }

        if (disability.count() > 0) {
          disability.first().click();
        }

        // final checks
        if (radioInput.count() > 0) {
          for (Locator radio : radioInput.all()) {
            if (radio.innerText().contains("Yes") && !radio.isChecked()) {
              radio.click();
            }
          }
        }
        if (checkBox.count() > 0) {
          for (Locator check : checkBox.all()) {
            if (check.innerText().contains("Yes")) {
              Locator container = check.locator("xpath=ancestor::*[label]"); // any ancestor that has a label child
              Locator mainLabel = container.locator("xpath=label[not(.//input)]").first();
              boolean skip = false;
              if (mainLabel.count() > 0) {
                Locator allCheckboxes = container.locator("input[type='checkbox']");
                for (Locator cb : allCheckboxes.all()) {
                  if (cb.isChecked()) {
                    skip = true;
                    break;
                  }
                }
              }
              if (skip) {
                continue;
              }
              Locator checkboxInput = check.locator("input[type='checkbox']");
              if (!checkboxInput.isChecked()) {
                check.click();
              }
            }
          }
        }
        if (allSelects.count() > 0) {
          for (Locator select : allSelects.all()) {
            // check if an option is already selected
            if (select.locator("option[selected]").count() == 0) {
              select.selectOption(select.locator("option").last().innerText());
            }
          }
        }

        // Submition
        if (imNotARobot.count() > 0) {
          imNotARobot.first().click();
        }

        if (submit.count() > 0) {
          submit.first().click();
        }

        if (submitApplication.count() > 0) {
          submitApplication.first().click();
        }

        page2.waitForTimeout(5000);

      } catch (Exception e) {
        log.error("HiringCafe apply failed", e);
      }
    }
  }

}
