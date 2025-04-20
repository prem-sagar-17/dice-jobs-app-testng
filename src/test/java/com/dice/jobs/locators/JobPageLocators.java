package com.dice.jobs.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class JobPageLocators {
    WebDriver driver;

    public JobPageLocators(WebDriver driver) {
        this.driver = driver;
    }

    public By nextButton = By.xpath("//button[@class='seds-button-primary btn-next']");
    public By submitButton = By.xpath("//span[normalize-space()='Submit']");
    public By applicationSubmittedHeading = By.xpath("(//h1[normalize-space()=\"Application submitted. We're rooting for you.\"])");
    public By appSubmitted = By.xpath("//div[text()='Application Submitted']");
    public By corpToCorp = By.xpath("//span[@id='employmentDetailChip: Accepts corp to corp applications']");
}
