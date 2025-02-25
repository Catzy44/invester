package me.catzy.invester;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Utils {
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
}
