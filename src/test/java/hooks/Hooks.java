package hooks;

import base.PlaywrightFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;
import config.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();
    private PlaywrightFactory playwrightFactory;
    private static ConfigReader configReader;

    @Before
    public void setUp(Scenario scenario) {
        logger.info("========== TEST START: {} ==========", scenario.getName());
        logger.info("Scenario Tags: {}", scenario.getSourceTagNames());

        if (extentReports == null) {
            configReader = new ConfigReader();
            logger.info("Initializing Extent Reports");
            Path reportPath = Paths.get("reports", "extent-report.html");
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath.toString());

            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Playwright Automation Report");
            spark.config().setReportName("Test Execution Report - " + configReader.getEnvironment().toUpperCase());
            spark.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

            extentReports = new ExtentReports();
            extentReports.attachReporter(spark);
            extentReports.setSystemInfo("Environment", configReader.getEnvironment());
            extentReports.setSystemInfo("Browser", configReader.getBrowserName());
            extentReports.setSystemInfo("Base URL", configReader.getBaseUrl());
            extentReports.setSystemInfo("Framework", "Playwright + Cucumber");
            extentReports.setSystemInfo("Screenshot Mode", configReader.getScreenshotMode());
            extentReports.setSystemInfo("Author", "Automation Team");
            logger.info("Extent Reports initialized successfully");
        }

        playwrightFactory = new PlaywrightFactory();
        if (configReader == null) {
            configReader = new ConfigReader();
        }

        logger.info("Launching browser: {}", configReader.getBrowserName());
        Page currentPage = playwrightFactory.initPage();
        page.set(currentPage);
        logger.info("Browser launched successfully");

        ExtentTest test = extentReports.createTest(scenario.getName());
        extentTest.set(test);
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        String screenshotMode = configReader.getScreenshotMode();
        logger.debug("Screenshot mode: {}, Step status: {}", screenshotMode, scenario.getStatus());

        if ("never".equalsIgnoreCase(screenshotMode)) {
            logger.debug("Screenshot capture skipped (mode: never)");
            return;
        }

        if ("onFailure".equalsIgnoreCase(screenshotMode) && !scenario.isFailed()) {
            logger.debug("Screenshot capture skipped (mode: onFailure, step passed)");
            return;
        }

        Page currentPage = page.get();
        if (currentPage != null) {
            try {
                Page.ScreenshotOptions screenshotOptions = new Page.ScreenshotOptions()
                        .setFullPage(configReader.isScreenshotFullPage())
                        .setTimeout(30000); // 30 seconds timeout
                byte[] screenshot = currentPage.screenshot(screenshotOptions);
                String base64Screenshot = Base64.getEncoder().encodeToString(screenshot);
                logger.info("Screenshot captured successfully (size: {} bytes)", screenshot.length);

                String stepName = "Step: " + scenario.getName();

                if (scenario.isFailed()) {
                    logger.error("Step FAILED: {}", stepName);
                    extentTest.get().log(Status.FAIL, stepName,
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                } else {
                    logger.info("Step PASSED: {}", stepName);
                    extentTest.get().log(Status.PASS, stepName,
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                }
            } catch (Exception e) {
                logger.error("Failed to capture screenshot", e);
                extentTest.get().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        Page currentPage = page.get();

        logger.info("Scenario status: {}", scenario.getStatus());

        if (currentPage != null) {
            try {
                Page.ScreenshotOptions screenshotOptions = new Page.ScreenshotOptions()
                        .setTimeout(30000); // 30 seconds timeout
                byte[] screenshot = currentPage.screenshot(screenshotOptions);
                String base64Screenshot = Base64.getEncoder().encodeToString(screenshot);

                if (scenario.isFailed()) {
                    logger.error("SCENARIO FAILED: {}", scenario.getName());
                    extentTest.get().fail("Scenario failed: " + scenario.getName(),
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());

                    scenario.attach(screenshot, "image/png", "Failed Screenshot");
                } else {
                    logger.info("SCENARIO PASSED: {}", scenario.getName());
                    extentTest.get().pass("Scenario passed: " + scenario.getName(),
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                }
            } catch (Exception e) {
                logger.error("Failed to capture final screenshot", e);
                extentTest.get().log(Status.WARNING, "Failed to capture final screenshot: " + e.getMessage());
            }
        }

        extentReports.flush();
        logger.info("Extent Reports flushed");

        if (playwrightFactory != null) {
            logger.info("Closing browser");
            playwrightFactory.close();
        }

        logger.info("========== TEST END: {} - {} ==========", scenario.getName(), scenario.getStatus());
    }

    public static Page getPage() {
        return page.get();
    }

    public static ConfigReader getConfig() {
        return new ConfigReader();
    }

    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }
}

