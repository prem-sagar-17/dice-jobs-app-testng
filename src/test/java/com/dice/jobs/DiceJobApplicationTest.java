package com.dice.jobs;

import com.dice.jobs.pages.JobActions;
import com.dice.jobs.pages.LoginActions;
import com.dice.jobs.pages.HomeActions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class DiceJobApplicationTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginActions loginActions;
    private HomeActions homeActions;
    private JobActions jobActions;
    private static final String EMAIL = System.getenv("EMAIL");
    private static final String PASSWORD = System.getenv("PASSWORD");

    @BeforeClass
    public void setupClass() {
        if (EMAIL == null || PASSWORD == null) {
            System.out.println("❌ ERROR: EMAIL or PASSWORD environment variables not set.");
            Assert.fail("EMAIL or PASSWORD environment variables not set.");
        }
    }

    @BeforeMethod
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headed"); // Use "--headless" if needed

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginActions = new LoginActions(driver);
        homeActions = new HomeActions(driver);
        jobActions = new JobActions(driver);
    }

    @Test(timeOut = 20 * 60 * 1000, priority = 1) // 20 minutes
    public void testJobApplicationAutomation() {
        try {
            // 🔐 Step 1: Login
            System.out.println("🔑 Logging in...");
            loginActions.login(EMAIL, PASSWORD);

            // 🔍 Step 2: Search Jobs
            homeActions.searchJobs();
            int pageNumber = 1;

            while (true) {
                System.out.println("🔄 Processing job listings on page - " + pageNumber++);

                List<WebElement> jobCards = homeActions.GetJobCards();

                for (int index = 0; index < jobCards.size(); index++) {
                    System.out.println("📌 Processing job at index " + index);
                    jobActions.applyForJob(jobCards.get(index));
                    jobCards = homeActions.GetJobCards(); // Refresh cards after DOM change
                }

                if (homeActions.GetPageNextButtonVisibility()) {
                    System.out.println("✅ No more pages to process.");
                    break;
                } else {
                    System.out.println("🔄 Moving to the next page...");
                    WebElement nextButton = homeActions.GetPageNextButtonLocator();
                    nextButton.click();
                    wait.until(ExpectedConditions.stalenessOf(nextButton)); // Wait for page change
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error encountered: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            System.out.println("📊 Exporting job applications before exit...");
            jobActions.exportToExcel();
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
