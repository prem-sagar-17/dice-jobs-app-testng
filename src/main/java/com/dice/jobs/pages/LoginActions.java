package com.dice.jobs.pages;

import com.dice.jobs.locators.LoginPageLocators;
import org.openqa.selenium.WebDriver;
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
        driver.findElement(locators.emailInput).sendKeys(email);
        driver.findElement(locators.signInButton).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.passwordField));
        driver.findElement(locators.passwordInput).sendKeys(password);
        driver.findElement(locators.submitPasswordButton).click();

        wait.until(ExpectedConditions.urlToBe("https://www.dice.com/home-feed"));

        System.out.println("✅ Logged in successfully.");
    }
}
