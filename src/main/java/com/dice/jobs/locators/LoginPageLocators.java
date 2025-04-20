package com.dice.jobs.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPageLocators {
    WebDriver driver;

    public LoginPageLocators(WebDriver driver) {
        this.driver = driver;
    }

    public By emailField = By.cssSelector("[data-testid='email-input']");
    public By emailInput = By.cssSelector("input[name='email']");
    public By signInButton = By.cssSelector("[data-testid='sign-in-button']");
    public By passwordField = By.cssSelector("[data-testid='password-input']");
    public By passwordInput = By.cssSelector("input[name='password']");
    public By submitPasswordButton = By.xpath("//span[text()='Sign In']");
}
