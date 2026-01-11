package com.Wissam.EasyApplier.Extractions.Jsoup;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Extractions.Jsoup.IInfoMethods.IFlightsInfo;
import com.Wissam.EasyApplier.Extractions.Jsoup.Models.FlightInfo;
import com.Wissam.EasyApplier.Extractions.Jsoup.Models.FlightsFound;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightsInfoGatherer implements IFlightsInfo {

  @Override
  public List<FlightsFound> findFlights(FlightInfo flightInfo) {
    try (
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium()
            .launch(new BrowserType.LaunchOptions().setHeadless(true).setSlowMo(300 + Math.random() * 1300));
        BrowserContext ctx = browser.newContext();) {

      Page page = ctx.newPage();
      page.navigate("https://www.trustfares.com/");

      page.waitForTimeout(5000);

      Locator from = page.getByPlaceholder("Flying From");
      from.waitFor();
      System.out.println("Found from " + from.count());
      from.fill(flightInfo.getCityFrom());
      from.press("Enter");
      page.waitForTimeout(ThreadLocalRandom.current().nextInt(700, 2000));

      Locator to = page.getByPlaceholder("Flying To");
      to.waitFor();
      System.out.println("Found to " + to.count());
      to.fill(flightInfo.getCityTo());
      to.press("Enter");
      page.waitForTimeout(ThreadLocalRandom.current().nextInt(700, 2000));

      Locator departure = page.getByPlaceholder("Depart");
      departure.waitFor();
      System.out.println("Found departure");
      departure.fill(flightInfo.getDepartureDate().toString());
      departure.press("Enter");
      page.waitForTimeout(ThreadLocalRandom.current().nextInt(700, 2000));

      Locator returnDate = page.getByPlaceholder("Return");
      returnDate.waitFor();
      System.out.println("Found return");
      returnDate.fill(flightInfo.getReturnDate().toString());
      returnDate.press("Enter");
      page.waitForTimeout(ThreadLocalRandom.current().nextInt(700, 2000));

      Page newPage = ctx.waitForPage(() -> {
        page.getByTestId("desktop-cta").click();
        page.waitForTimeout(20000);
      });
      page.close();
      String html = newPage.content();

      Document doc = Jsoup.parse(html);
      Elements flights = doc.getElementsByAttributeValue("data-testid", "trip-card");
      for (Element flight : flights) {
        String tripId = flight.attr("data-trip-id");

        Element priceEl = flight
            .getElementsByAttributeValue("data-testid", "price")
            .first();

        String price;
        if (priceEl != null) {
          price = priceEl.text();
        } else {
          price = "N/A";
        }

        System.out.println("Trip ID: " + tripId);
        System.out.println("Price: " + price);
      }

      System.out.println("New page opened: " + newPage.url());

      return null;
    } catch (Exception e) {
      throw new RuntimeException("Error: " + e.getMessage() + "In FlightsInfoGatherer findFlights method");
    }
  }

}
