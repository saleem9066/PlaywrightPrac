package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import config.ConfigReader;

public class PlaywrightFactory {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public Page initPage() {
        ConfigReader config = new ConfigReader();
        String browserName = config.getBrowserName();
        boolean headless = config.isHeadless();

        playwright = Playwright.create();
        browser = launchBrowser(browserName, headless);
        context = browser.newContext();
        page = context.newPage();
        page.setDefaultTimeout(config.getDefaultTimeout());
        playwright.selectors().setTestIdAttribute("data-test");
        return page;
    }

    private Browser launchBrowser(String browserName, boolean headless) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
        if ("firefox".equalsIgnoreCase(browserName)) {
            return playwright.firefox().launch(launchOptions);
        }
        if ("webkit".equalsIgnoreCase(browserName)) {
            return playwright.webkit().launch(launchOptions);
        }
        return playwright.chromium().launch(launchOptions);
    }

    public void close() {
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}

