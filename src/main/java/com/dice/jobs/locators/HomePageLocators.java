package com.dice.jobs.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePageLocators {
    WebDriver driver;

    public HomePageLocators(WebDriver driver) {
        this.driver = driver;
    }

    // 🔹 Home/Search Page Locators
    public By jobTitleField = By.cssSelector("input[placeholder='Job title, skill, company, keyword'][role='combobox']");
    public By locationField =By.cssSelector("div[role='group'] input[aria-label='Location Field']");
    public By location = By.cssSelector("input[aria-label='Location Field'][placeholder='Location (ex. Denver, remote)']");
    public By unitedStatesOption = By.xpath("//div[@slot='option']//span[text()='United States']");
    public By postedTodayRadio = By.xpath("//button[normalize-space()='Today']");
    public By filterSearchCheckbox = By.xpath("//button[@aria-label='Filter Search Results by Third Party']");
    public By jobCards = By.xpath("//div[@class='card search-card']");
    public By paginationItems = By.xpath("//ul[@class='pagination']/li[contains(@class, 'pagination-page')]");
    public By pageNextDisabled = By.xpath("//li[@class='pagination-next page-item ng-star-inserted disabled']");
    public By pageNextButton = By.xpath("//li[@class='pagination-next']/a[normalize-space()='»']");
}
