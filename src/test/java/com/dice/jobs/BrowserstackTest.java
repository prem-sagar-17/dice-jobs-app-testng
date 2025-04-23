package com.dice.jobs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserstackTest {
    protected WebDriver driver;

    public static final String USERNAME = "premsagarmulugur_6PlhOI";
    public static final String AUTOMATE_KEY = "FLtzHkfbrEqZHDEK1DJm";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @BeforeMethod(alwaysRun = true)
    @Parameters({ "os", "os_version", "browser", "browser_version" })
    public void setUp(
            @Optional("Windows") String os,
            @Optional("10") String os_version,
            @Optional("Chrome") String browser,
            @Optional("latest") String browser_version
    ) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os", os);
        caps.setCapability("os_version", os_version);
        caps.setCapability("browser", browser);
        caps.setCapability("browser_version", browser_version);
        caps.setCapability("name", "Dice Job Apply Automation");
        caps.setCapability("build", "Dice Apply Build 01");
        caps.setCapability("browserstack.debug", true);
        caps.setCapability("browserstack.networkLogs", true);

        driver = new RemoteWebDriver(new URL(URL), caps);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
