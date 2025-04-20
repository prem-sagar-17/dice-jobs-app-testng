package com.dice.jobs.pages;

import com.dice.jobs.locators.LoginPageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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

        // ⛔ Handle cookie/consent popup
        try {
            List<WebElement> cmpWrappers = driver.findElements(By.id("cmpwrapper"));
            if (!cmpWrappers.isEmpty() && cmpWrappers.get(0).isDisplayed()) {
                System.out.println("⚠️ Cookie popup detected, trying to close...");
                WebElement closeButton = cmpWrappers.get(0).findElement(By.cssSelector("button"));
                closeButton.click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("cmpwrapper")));
                System.out.println("✅ Cookie popup closed.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not close cookie popup: " + e.getMessage());
        }

        // Continue with login
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.emailField));
        driver.findElement(locators.emailInput).sendKeys(email);
        System.out.println("entered email");

        driver.findElement(locators.signInButton).click();
        System.out.println("clicked email");

        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.passwordField));
        driver.findElement(locators.passwordInput).sendKeys(password);
        System.out.println("entered password");

        driver.findElement(locators.submitPasswordButton).click();
        System.out.println("clicked password button");

        wait.until(ExpectedConditions.urlToBe("https://www.dice.com/home-feed"));
        System.out.println("✅ Logged in successfully.");
    }
}
