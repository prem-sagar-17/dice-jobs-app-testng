package com.dice.jobs.pages;

import com.dice.jobs.locators.HomePageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
            jobRole = "java full stack developer";
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

    public List<WebElement> GetPageNumber() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.paginationItems));
        return driver.findElements(locators.paginationItems);
    }

    public List<WebElement> GetJobCards() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.jobCards));
        return driver.findElements(locators.jobCards);
    }

    public WebElement GetPageNextButtonLocator() {
        wait.until(ExpectedConditions.presenceOfElementLocated(locators.pageNextButton));
        return driver.findElement(locators.pageNextButton);
    }

    public boolean GetPageNextButtonVisibility() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locators.pageNext));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
