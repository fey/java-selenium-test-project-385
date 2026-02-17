package dev.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppSmokeTest {
    private WebDriver driver;

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void opensAppAndShowsLoginForm() {
        var baseUrl = System.getenv().getOrDefault("APP_BASE_URL", "http://localhost:5173");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        driver.get(baseUrl);

        Boolean responseOk = (Boolean) ((JavascriptExecutor) driver).executeAsyncScript(
            "const cb = arguments[arguments.length - 1];"
                + "fetch(window.location.href, { cache: 'no-store' })"
                + ".then(r => cb(r.ok))"
                + ".catch(() => cb(false));"
        );

        assertTrue(responseOk, "Expected HTTP response to be OK");

        var wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        var form = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("form"))
        );

        assertNotNull(form, "Login form should be present");

        var passwordInputs = driver.findElements(By.cssSelector("input[type='password']"));
        assertFalse(passwordInputs.isEmpty(), "Password input should be present in login form");
    }
}
