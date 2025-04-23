package com.dice.jobs.pages;

import com.dice.jobs.locators.HomePageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;


import java.time.Duration;
import java.util.List;

public class HomeActions {

    private final WebDriver driver;
    private final HomePageLocators locators;
    private final WebDriverWait wait;

    public HomeActions(WebDriver driver) {
        this.driver = driver;
        this.locators = new HomePageLocators(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void searchJobs() {
        String jobRole = System.getenv("JOB_ROLE");
        if (jobRole == null || jobRole.isEmpty()) {
            jobRole = "qa engineer";
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.jobTitleField));
        WebElement jobTitle = driver.findElement(locators.jobTitleField);
        jobTitle.clear();
        jobTitle.sendKeys(jobRole);

        driver.findElement(locators.location).click();

        WebElement locationField = driver.findElement(locators.locationField);
        locationField.clear();
        locationField.sendKeys("united states");

        wait.until(ExpectedConditions.elementToBeClickable(locators.unitedStatesOption));
        driver.findElement(locators.unitedStatesOption).click();

        wait.until(ExpectedConditions.elementToBeClickable(locators.postedTodayRadio));
        WebElement postedToday = driver.findElement(locators.postedTodayRadio);
        postedToday.click();

        wait.until(ExpectedConditions.elementToBeClickable(locators.filterSearchCheckbox));
        driver.findElement(locators.filterSearchCheckbox).click();

        System.out.println("🔎 Job search filters applied. " + postedToday.getText() + " for " + jobRole);
    }

    public int GetPageNumberCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.paginationItems));
        List<WebElement> pages = driver.findElements(locators.paginationItems);
        int count = 0;

        for (WebElement page : pages) {
            String text = page.getText().trim();
            if (text.matches("\\d+")) {
                count++;
            }
        }

        return count;
    }

    public List<WebElement> GetJobCards() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.jobCards));
        return driver.findElements(locators.jobCards);
    }

    public WebElement GetPageNextButtonLocator() {
        try {
            WebElement nextButton = driver.findElement(By.cssSelector("li[class='pagination-next page-item ng-star-inserted'] a[class='page-link']"));
            wait.until(ExpectedConditions.visibilityOf(nextButton));
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);

            WebElement geoLocateButton = driver.findElement(By.id("IPGeoLocateButton"));
            wait.until(ExpectedConditions.invisibilityOf(geoLocateButton));

            // Click via Actions class
            Actions actions = new Actions(driver);
            actions.moveToElement(nextButton).click().perform();

            return nextButton;
        } catch (Exception e) {
            System.out.println("❌ Error encountered: " + e.getMessage());
            return null;
        }
    }

    public boolean GetPageNextDisabledButtonVisibility() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locators.pageNextDisabled));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
