package com.dice.jobs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserStackFactory {

    private static final String USERNAME = "premsagarmulugur_6PlhOI";
    private static final String ACCESS_KEY = "FLtzHkfbrEqZHDEK1DJm";
    private static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static WebDriver getDriver(String os, String osVersion, String browser, String browserVersion, String testName) {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("os", os);
        caps.setCapability("os_version", osVersion);
        caps.setCapability("browser", browser);
        caps.setCapability("browser_version", browserVersion);
        caps.setCapability("name", testName);
        caps.setCapability("build", "BrowserStack-Sample-Build");
        caps.setCapability("browserstack.debug", true);
        caps.setCapability("browserstack.networkLogs", true);

        try {
            return new RemoteWebDriver(new URL(URL), caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create WebDriver: " + e.getMessage());
        }
    }
}
