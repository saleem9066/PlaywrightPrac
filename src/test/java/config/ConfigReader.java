package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE = "/config.properties";
    private final Properties properties = new Properties();

    public ConfigReader() {
        try (InputStream input = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Unable to find " + CONFIG_FILE + " in classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load " + CONFIG_FILE, ex);
        }
    }

    public String getEnvironment() {
        return getProperty("env", "staging");
    }

    public String getBaseUrl() {
        String env = getEnvironment();
        String envKey = "baseUrl." + env;
        String url = getProperty(envKey, null);

        if (url == null) {
            url = getProperty("baseUrl", "https://practicesoftwaretesting.com");
        }

        return url;
    }

    public String getApiBaseUrl() {
        return getProperty("api.baseUrl", "https://api.practicesoftwaretesting.com");
    }

    public String getBrowserName() {
        return getProperty("browser", "chromium");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    public double getDefaultTimeout() {
        return Double.parseDouble(getProperty("defaultTimeout", "30000"));
    }

    public int getSlowMo() {
        return Integer.parseInt(getProperty("slowMo", "0"));
    }

    public String getScreenshotMode() {
        return getProperty("screenshot.mode", "always"); // always | onFailure | never
    }

    public boolean isScreenshotFullPage() {
        return Boolean.parseBoolean(getProperty("screenshot.fullPage", "true"));
    }

    public boolean isVideoEnabled() {
        return Boolean.parseBoolean(getProperty("video.enabled", "false"));
    }

    public String getVideoDir() {
        return getProperty("video.dir", "reports/videos");
    }

    public boolean isTraceEnabled() {
        return Boolean.parseBoolean(getProperty("trace.enabled", "false"));
    }

    public String getTraceDir() {
        return getProperty("trace.dir", "reports/traces");
    }

    public int getParallelThreads() {
        return Integer.parseInt(getProperty("parallel.threads", "1"));
    }

    public int getRetryFailedCount() {
        return Integer.parseInt(getProperty("retry.failed.count", "0"));
    }

    private String getProperty(String key, String defaultValue) {
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        String value = properties.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return defaultValue;
    }
}

