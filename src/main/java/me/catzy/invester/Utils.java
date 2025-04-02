package me.catzy.invester;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	public static String extractArticleText(WebElement parent) {
	    StringBuilder articleText = new StringBuilder();

	    // Znajdź wszystkie potomne elementy <p> oraz <span>
	    List<WebElement> textElements = parent.findElements(By.xpath(".//p | .//span"));

	    for (WebElement element : textElements) {
	        String text = element.getText();
	        if (text != null && !text.trim().isEmpty()) {
	            articleText.append(text).append("\n");
	        }
	    }
	    return articleText.toString().trim();
	}
	
	private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m");
	public static void dumpUntilExahausted(InputStream i) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader r = new BufferedReader(new InputStreamReader(i, StandardCharsets.UTF_8));
					String l;
					while((l = r.readLine()) != null) {
						l = ANSI_PATTERN.matcher(l).replaceAll("");
						logger.info(l);
					}
					r.close();
				} catch (Exception e) {
					logger.warn("Process InputStream terminated!");
				}
			}
		}).start();
	}
}
