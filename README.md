# Dice Job Application (Selenium + TestNG)

This project automates the process of applying to relevant jobs on Dice.com using a TestNG framework with Selenium WebDriver. It intelligently filters jobs based on title relevance, performs "Easy Apply" where available, skips already-applied jobs, and exports results to an Excel sheet for tracking.

### 🔧 Tech Stack
- Java 17
- Selenium WebDriver
- TestNG
- Apache POI (Excel Export)
- ChromeDriver
- GitHub Actions / CI (optional for automation)

### 🚀 Features
- Daily automated job application execution
- Filters job titles (e.g., QA Engineer, Quality Assurance Engineer)
- Skips jobs already applied
- Applies to jobs with "Easy Apply" support
- Generates an Excel report of application results

---

### 📁 Output
Excel reports are saved to the `xslx-reports/` directory (excluded from Git).

---

### 🔒 Environment Variables Required
- `EMAIL` – Your Dice login email
- `PASSWORD` – Your Dice login password
