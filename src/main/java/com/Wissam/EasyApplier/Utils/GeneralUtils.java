package com.Wissam.EasyApplier.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;

@Component
public class GeneralUtils {

  public Map<String, String> getCookiesFromBrowserContext(BrowserContext context) {
    Map<String, String> cookies = new ConcurrentHashMap<>();
    for (Cookie cookie : context.cookies()) {
      cookies.put(cookie.name, cookie.value);
    }
    return cookies;
  }

}
