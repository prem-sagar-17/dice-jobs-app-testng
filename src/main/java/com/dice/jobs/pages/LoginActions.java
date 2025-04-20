package com.dice.jobs.pages;

import com.dice.jobs.locators.LoginPageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginActions {

    private final WebDriver driver;
    private final LoginPageLocators locators;
    private final WebDriverWait wait;

    public LoginActions(WebDriver driver) {
        this.driver = driver;
        this.locators = new LoginPageLocators(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email or Password is undefined");
        }

        driver.get("https://www.dice.com/dashboard/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.emailField));

        dismissPopupIfPresent();

        driver.findElement(locators.emailInput).sendKeys(email);
        dismissPopupIfPresent();

        try {
            WebElement signInButton = driver.findElement(locators.signInButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signInButton);
        } catch (Exception e) {
            System.out.println("⚠️ Failed JS click on sign-in button: " + e.getMessage());
            throw e;
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.passwordField));
        dismissPopupIfPresent();

        driver.findElement(locators.passwordInput).sendKeys(password);
        dismissPopupIfPresent();

        try {
            WebElement passwordButton = driver.findElement(locators.submitPasswordButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", passwordButton);
        } catch (Exception e) {
            System.out.println("⚠️ Failed JS click on password button: " + e.getMessage());
            throw e;
        }

        wait.until(ExpectedConditions.urlToBe("https://www.dice.com/home-feed"));
    }

    private void dismissPopupIfPresent() {
        try {
            WebElement cmpWrapper = driver.findElement(By.id("cmpwrapper"));
            if (cmpWrapper.isDisplayed()) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.getElementById('cmpwrapper').style.display='none';");
                Thread.sleep(1000);
            }
        } catch (NoSuchElementException e) {
            // Not present — silently ignore
        } catch (Exception e) {
            System.out.println("⚠️ Failed to dismiss popup: " + e.getMessage());
        }
    }
}
