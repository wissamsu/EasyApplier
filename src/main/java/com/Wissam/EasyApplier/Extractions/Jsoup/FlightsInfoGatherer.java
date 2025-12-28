package com.Wissam.EasyApplier.Extractions.Jsoup;

import org.springframework.stereotype.Component;

import com.Wissam.EasyApplier.Playwright.Config.PlaywrightConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightsInfoGatherer {

  private final PlaywrightConfig playwrightConfig;

}
