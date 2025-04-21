package com.dice.jobs.pages;

import com.dice.jobs.locators.JobPageLocators;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobActions {
    private final WebDriver driver;
    private final JobPageLocators locators;
    private final WebDriverWait wait;
    private final List<HashMap<String, String>> jobResults = new ArrayList<>();

    public JobActions(WebDriver driver) {
        this.driver = driver;
        this.locators = new JobPageLocators(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void applyForJob(WebElement jobCard) {
        try {
            WebElement link = jobCard.findElement(By.xpath(".//h5//a"));
            String jobTitle = link.getText();

            if (!jobTitle.matches("(?i).*\\b(qa|automation|tester|analyst|test|quality\\s?assurance|quality\\s?engineer)\\b.*")) {
                System.out.println("⏭️ Skipping:" + jobTitle + " application. Not a relevant job.");
                return;
            }

            if (isAlreadyApplied(jobCard)) {
                System.out.println("⏭️ Skipping: Already applied job found.");
                return;
            }

            System.out.println("🚀 Applying for: " + jobTitle);
            String parentWindow = driver.getWindowHandle();
            link.click();
            switchToNewWindow(parentWindow);

            if (isApplicationAlreadySubmitted(jobTitle)) {
                closeAndSwitchBack(parentWindow);
                return;
            }

            if (!scrollToElement(locators.corpToCorp)) {
                System.out.println("❌ Skipping: " + jobTitle + " (Corp to Corp not visible)");
                saveJobResult(jobTitle, "❌", "❌", "✅", driver.getCurrentUrl());
                closeAndSwitchBack(parentWindow);
                return;
            }

            System.out.println("✅ 'Accepts corp to corp applications' is visible. Proceeding with application...");
            performEasyApplyFlow(jobTitle);
            closeAndSwitchBack(parentWindow);

        } catch (Exception e) {
            System.out.println("❌ Error applying for job: " + e.getMessage());
        }
    }

    private boolean isAlreadyApplied(WebElement jobCard) {
        List<WebElement> appliedTextElements = jobCard.findElements(By.xpath(".//span[contains(text(), 'applied')]"));
        return !appliedTextElements.isEmpty() && appliedTextElements.get(0).isDisplayed();
    }

    private void switchToNewWindow(String parentWindow) {
        for (String winHandle : driver.getWindowHandles()) {
            if (!winHandle.equals(parentWindow)) {
                driver.switchTo().window(winHandle);
                break;
            }
        }
    }

    private void closeAndSwitchBack(String parentWindow) {
        driver.close();
        driver.switchTo().window(parentWindow);
    }

    private boolean isApplicationAlreadySubmitted(String jobTitle) {
        List<WebElement> elements = driver.findElements(locators.appSubmitted);
        if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
            System.out.println("⏭️ Skipping: " + jobTitle + " (Job already applied)");
            saveJobResult(jobTitle, "✅", "❌", "❌", driver.getCurrentUrl());
            return true;
        }
        return false;
    }

    private void performEasyApplyFlow(String jobTitle) {
        if (!clickEasyApplyButtonIfPresent()) {
            System.out.println("⏭️ Skipping: Easy Apply not available for " + jobTitle);
            saveJobResult(jobTitle, "❌", "❌", "✅", driver.getCurrentUrl());
            return;
        }

        try {
            wait.until(ExpectedConditions.elementToBeClickable(locators.nextButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(locators.submitButton)).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(locators.applicationSubmittedHeading));

            System.out.println("✅ Successfully applied: " + jobTitle);
            saveJobResult(jobTitle, "❌", "✅", "❌", driver.getCurrentUrl());

        } catch (Exception e) {
            System.out.println("❌ Easy Apply failed during form submission for: " + jobTitle + " - " + e.getMessage());
            saveJobResult(jobTitle, "❌", "❌", "✅", driver.getCurrentUrl());
        }
    }

    private boolean clickEasyApplyButtonIfPresent() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.querySelector('apply-button-wc')?.scrollIntoView({ behavior: 'smooth', block: 'center' });");
            Thread.sleep(2000);

            WebElement easyApplyBtn = null;
            String buttonText;

            for (int i = 0; i < 5; i++) {
                easyApplyBtn = (WebElement) js.executeScript(
                        "const host = document.querySelector('apply-button-wc');" +
                                "if (host && host.shadowRoot) {" +
                                "  const btn = host.shadowRoot.querySelector('button.btn.btn-primary');" +
                                "  if (btn) return btn;" +
                                "} return null;"
                );

                if (easyApplyBtn != null) {
                    buttonText = (String) js.executeScript("return arguments[0].innerText;", easyApplyBtn);
                    if (buttonText != null && buttonText.toLowerCase().contains("easy apply")) {
                        break;
                    } else {
                        easyApplyBtn = null;
                    }
                }
                Thread.sleep(1000);
            }

            if (easyApplyBtn != null && easyApplyBtn.isDisplayed()) {
                js.executeScript("arguments[0].click();", easyApplyBtn);
                System.out.println("✅ Easy Apply button clicked.");
                return true;
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to click Easy Apply button: " + e.getMessage());
        }
        return false;
    }

    private void saveJobResult(String title, String alreadyApplied, String applied, String notApplied, String link) {
        HashMap<String, String> result = new HashMap<>();
        result.put("title", title);
        result.put("alreadyApplied", alreadyApplied);
        result.put("applied", applied);
        result.put("notApplied", notApplied);
        result.put("link", link);
        jobResults.add(result);
    }

    private boolean scrollToElement(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 0; i < 10; i++) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) return true;
                js.executeScript("window.scrollBy(0,200);");
                Thread.sleep(500);
            } catch (Exception ignored) {}
        }
        return false;
    }

    public void exportToExcel() {
        if (jobResults.isEmpty()) {
            System.out.println("⚠️ No jobs processed. Skipping Excel export.");
            return;
        }

        System.out.println("📤 Exporting job results to Excel...");

        int totalApplied = (int) jobResults.stream()
                .filter(job -> "✅".equals(job.get("applied")))
                .count();
        int totalNotApplied = (int) jobResults.stream()
                .filter(job -> "✅".equals(job.get("notApplied")))
                .count();
        int totalAlreadyApplied = (int) jobResults.stream()
                .filter(job -> "✅".equals(job.get("alreadyApplied")))
                .count();

        HashMap<String, String> summary = new HashMap<>();
        summary.put("title", "Total");
        summary.put("alreadyApplied", String.valueOf(totalAlreadyApplied));
        summary.put("applied", String.valueOf(totalApplied));
        summary.put("notApplied", String.valueOf(totalNotApplied));
        summary.put("link", "");
        jobResults.add(summary);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Job Applications");
            Row header = sheet.createRow(0);
            String[] headers = {"title", "alreadyApplied", "applied", "notApplied", "link"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowIndex = 1;
            for (HashMap<String, String> job : jobResults) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 0; i < headers.length; i++) {
                    row.createCell(i).setCellValue(job.getOrDefault(headers[i], ""));
                }
            }

            File dir = new File("xslx-reports");
            if (!dir.exists() && !dir.mkdirs()) {
                System.out.println("❌ Failed to create directory 'xslx-reports'. Export aborted.");
                return;
            }

            try (FileOutputStream fos = new FileOutputStream("xslx-reports/job_applications.xlsx")) {
                workbook.write(fos);
                System.out.println("✅ Job results saved to xslx-reports/job_applications.xlsx");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to export Excel file: " + e.getMessage());
        }
    }
}
