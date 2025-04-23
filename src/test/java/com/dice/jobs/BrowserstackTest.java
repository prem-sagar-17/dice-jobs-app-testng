package com.dice.jobs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserstackTest {
    protected WebDriver driver;

    // ✅ Set your credentials here
    public static final String USERNAME = "premsagarmulugur_6PlhOI";
    public static final String AUTOMATE_KEY = "FLtzHkfbrEqZHDEK1DJm";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @Parameters({ "os", "os_version", "browser", "browser_version" })
    @BeforeMethod(alwaysRun = true)
    public void setUp(String os, String os_version, String browser, String browser_version) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os", os);
        caps.setCapability("os_version", os_version);
        caps.setCapability("browser", browser);
        caps.setCapability("browser_version", browser_version);
        caps.setCapability("name", "Dice Job Apply Automation"); // Test name
        caps.setCapability("build", "Dice Apply Build 01");       // CI build name
        caps.setCapability("browserstack.debug", true);           // Enable visual logs
        caps.setCapability("browserstack.networkLogs", true);     // Enable network logs

        driver = new RemoteWebDriver(new URL(URL), caps);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
