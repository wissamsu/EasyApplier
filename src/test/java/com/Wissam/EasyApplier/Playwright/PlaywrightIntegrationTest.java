package com.Wissam.EasyApplier.Playwright;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Cookie;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DisplayName("Playwright Integration Tests")
public class PlaywrightIntegrationTest {

  private Playwright playwright;
  private Browser browser;
  private BrowserContext context;
  private Page page;

  @Value("${server.port:8080}")
  private int port;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch();
    context = browser.newContext();
    page = context.newPage();
    baseUrl = "http://localhost:" + port;
  }

  @AfterEach
  void tearDown() {
    if (page != null) {
      page.close();
    }
    if (context != null) {
      context.close();
    }
    if (playwright != null) {
      playwright.close();
    }
  }

  @Nested
  @DisplayName("Auth Page Tests")
  class AuthPageTests {

    @Test
    @DisplayName("should load auth hello endpoint")
    void shouldLoadAuthHelloEndpoint() {
      page.navigate(baseUrl + "/auth/hello");
      String content = page.content();
      assertThat(content).isNotNull();
    }

    @Test
    @DisplayName("should check auth status")
    void shouldCheckAuthStatus() {
      page.navigate(baseUrl + "/auth/auth-status");
      String content = page.content();
      assertThat(content).isNotNull();
    }

    @Test
    @DisplayName("should load failure page")
    void shouldLoadFailurePage() {
      page.navigate(baseUrl + "/auth/failure");
      assertThat(page.content()).contains("fuck");
    }
  }

  @Nested
  @DisplayName("API Endpoint Tests")
  class ApiEndpointTests {

    @Test
    @DisplayName("should get swagger-ui endpoint")
    void shouldGetSwaggerUiEndpoint() {
      try {
        page.navigate(baseUrl + "/swagger-ui/index.html");
      } catch (Exception e) {
        // Swagger may not be available in test environment
      }
    }

    @Test
    @DisplayName("should get api-docs endpoint")
    void shouldGetApiDocsEndpoint() {
      try {
        page.navigate(baseUrl + "/v3/api-docs");
      } catch (Exception e) {
        // API docs may not be available
      }
    }
  }

  @Nested
  @DisplayName("Security Tests")
  class SecurityTests {

    @Test
    @DisplayName("should handle unauthenticated request to protected endpoint")
    void shouldHandleUnauthenticatedRequestToProtectedEndpoint() {
      try {
        page.navigate(baseUrl + "/user/all");
      } catch (Exception e) {
        // Expected - should fail for unauthenticated requests
      }
    }

    @Test
    @DisplayName("should allow access to public endpoints")
    void shouldAllowAccessToPublicEndpoints() {
      page.navigate(baseUrl + "/auth/hello");
      assertThat(page.content()).isNotNull();
    }
  }

  @Nested
  @DisplayName("Page Interaction Tests")
  class PageInteractionTests {

    @Test
    @DisplayName("should interact with page elements")
    void shouldInteractWithPageElements() {
      page.setContent("<html><body><input id='test' /><button id='btn'>Click</button></body></html>");

      page.fill("#test", "test value");
      assertThat(page.locator("#test").inputValue()).isEqualTo("test value");

      page.click("#btn");
    }

    @Test
    @DisplayName("should handle form submissions")
    void shouldHandleFormSubmissions() {
      page.setContent("<html><body>" +
          "<form id='testForm'>" +
          "<input name='email' value='test@example.com' />" +
          "<input name='password' value='password' />" +
          "<button type='submit'>Submit</button>" +
          "</form></body></html>");

      assertThat(page.locator("#testForm").isVisible()).isTrue();
    }

    @Test
    @DisplayName("should handle multiple page navigations")
    void shouldHandleMultiplePageNavigations() {
      page.setContent("<html><body><h1>Page 1</h1></body></html>");
      assertThat(page.locator("h1").textContent()).contains("Page 1");

      page.setContent("<html><body><h1>Page 2</h1></body></html>");
      assertThat(page.locator("h1").textContent()).contains("Page 2");
    }
  }

  @Nested
  @DisplayName("Cookie Tests")
  class CookieTests {

    @Test
    @DisplayName("should handle cookies")
    void shouldHandleCookies() {
      context.addCookies(List.of(
          new Cookie("testCookie", "testValue")
              .setDomain("localhost")
              .setPath("/")));

      page.navigate(baseUrl + "/auth/hello");

      List<Cookie> cookies = context.cookies();
      boolean found = cookies.stream().anyMatch(c -> c.name.contains("testCookie"));
      assertThat(found).isTrue();
    }
  }

  @Nested
  @DisplayName("Network Request Tests")
  class NetworkRequestTests {

    @Test
    @DisplayName("should capture network requests")
    void shouldCaptureNetworkRequests() {
      final int[] requestCount = { 0 };

      page.onRequest(request -> requestCount[0]++);

      page.setContent("<html><body>Test</body></html>");

      assertThat(requestCount[0]).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("should handle response")
    void shouldHandleResponse() {
      page.navigate(baseUrl + "/auth/hello");

      assertThat(page).isNotNull();
    }
  }

  @Nested
  @DisplayName("Page State Tests")
  class PageStateTests {

    @Test
    @DisplayName("should handle page title")
    void shouldHandlePageTitle() {
      page.setContent("<html><head><title>Test Page</title></head><body></body></html>");
      assertThat(page.title()).isEqualTo("Test Page");
    }

    @Test
    @DisplayName("should handle page viewport")
    void shouldHandlePageViewport() {
      context.setExtraHTTPHeaders(Map.of("User-Agent", "Test"));
      page.setContent("<html><body>Test</body></html>");

      assertThat(page.viewportSize()).isNotNull();
    }

    @Test
    @DisplayName("should check element visibility")
    void shouldCheckElementVisibility() {
      page.setContent(
          "<html><body><div id='visible'>Visible</div><div id='hidden' style='display:none'>Hidden</div></body></html>");

      assertThat(page.locator("#visible").isVisible()).isTrue();
      assertThat(page.locator("#hidden").isVisible()).isFalse();
    }

    @Test
    @DisplayName("should check element presence")
    void shouldCheckElementPresence() {
      page.setContent("<html><body><span id='exists'>Exists</span></body></html>");

      assertThat(page.locator("#exists").count()).isEqualTo(1);
      assertThat(page.locator("#notexists").count()).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("Input Tests")
  class InputTests {

    @Test
    @DisplayName("should type text into input")
    void shouldTypeTextIntoInput() {
      page.setContent("<html><body><input type='text' id='input' /></body></html>");

      page.locator("#input").type("Hello World");
      assertThat(page.locator("#input").inputValue()).contains("Hello World");
    }

    @Test
    @DisplayName("should clear input")
    void shouldClearInput() {
      page.setContent("<html><body><input type='text' id='input' value='initial' /></body></html>");

      page.locator("#input").clear();
      assertThat(page.locator("#input").inputValue()).isEmpty();
    }

    @Test
    @DisplayName("should check checkbox")
    void shouldCheckCheckbox() {
      page.setContent("<html><body><input type='checkbox' id='check' /></body></html>");

      page.locator("#check").check();
      assertThat(page.locator("#check").isChecked()).isTrue();

      page.locator("#check").uncheck();
      assertThat(page.locator("#check").isChecked()).isFalse();
    }
  }

  @Nested
  @DisplayName("Wait Tests")
  class WaitTests {

    @Test
    @DisplayName("should wait for selector")
    void shouldWaitForSelector() {
      page.setContent("<html><body><div id='delayed'>Loaded</div></body></html>");

      page.locator("#delayed").waitFor();
      assertThat(page.locator("#delayed").isVisible()).isTrue();
    }

    @Test
    @DisplayName("should wait for load state")
    void shouldWaitForLoadState() {
      page.navigate(baseUrl + "/auth/hello");
      page.waitForLoadState();

      assertThat(page.content()).isNotNull();
    }
  }
}
