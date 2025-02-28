package me.catzy.invester.objects.webdriver;

import java.util.Collections;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class WebDriverService {
	private WebDriver driver;
	
	public WebDriver get() {
		if(driver == null) {
			driver = loadDriverB();
		}
		return driver;
	}
	
	public void close() {
		if(driver != null) {
			driver.close();
		}
		driver = null;
	}
	
	@SuppressWarnings("deprecation")
	private WebDriver loadDriverB() {
	    System.setProperty("webdriver.chrome.driver", "C:\\Users\\catzy\\Desktop\\invester.local\\undetected_chromedriver.exe");
	    ChromeOptions options = new ChromeOptions();
	    options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
	    options.setExperimentalOption("useAutomationExtension", false);
	    options.addArguments("--disable-blink-features=AutomationControlled");
	    options.setHeadless(false);
	    String modernUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36";
	    options.addArguments("--user-agent=" + modernUserAgent);
	    WebDriver driver = new ChromeDriver(options);
	    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
	    return driver;
	}
}
