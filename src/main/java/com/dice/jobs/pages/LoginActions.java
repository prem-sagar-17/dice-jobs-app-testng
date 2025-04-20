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

        // Wait for page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.emailField));

        // ⛔ Attempt to remove cookie popup if present
        try {
            WebElement cmpWrapper = driver.findElement(By.id("cmpwrapper"));
            if (cmpWrapper.isDisplayed()) {
                System.out.println("⚠️ Cookie popup is blocking. Attempting to hide it via JS...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.getElementById('cmpwrapper').style.display='none';");
                Thread.sleep(1000); // Give time for it to disappear
                System.out.println("✅ Cookie popup hidden via JS.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("✅ No cookie popup detected.");
        } catch (Exception e) {
            System.out.println("⚠️ Failed to hide cookie popup: " + e.getMessage());
        }

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
