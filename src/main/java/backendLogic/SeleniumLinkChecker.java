package backendLogic;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumLinkChecker {

//	public static List<String> getAllPageLinks(String pageUrl) throws Exception {
//        //System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
//        WebDriver driver = new ChromeDriver();
//        List<String> links = new ArrayList<>();
//
//        try {
//            driver.get(pageUrl);
//            List<WebElement> elements = driver.findElements(By.tagName("a"));
//
//            for (WebElement el : elements) {
//                String href = el.getAttribute("href");
//                if (href != null && (href.startsWith("http://") || href.startsWith("https://"))) {
//                    links.add(href);
//                }
//            }
//        } finally {
//            driver.quit();
//        }
//        return links;
//    }
//
//    public static int getHttpStatusCode(String link) {
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(link).openConnection();
//            conn.setRequestMethod("HEAD");
//            conn.setConnectTimeout(3000);
//            conn.connect();
//            return conn.getResponseCode();
//        } catch (Exception e) {
//            return -1;
//        }
//    }
	
	
	
	 public static List<String> getAllPageLinks(String url) {
	       // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
	        WebDriver driver = new ChromeDriver();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	        driver.get(url);

	        List<WebElement> elements = driver.findElements(By.tagName("a"));
	        Set<String> links = elements.stream()
	                .map(e -> e.getAttribute("href"))
	                .filter(href -> href != null && href.startsWith("http"))
	                .collect(Collectors.toSet());

	        driver.quit();
	        return new ArrayList<>(links);
	    }

	    public static int getHttpStatusCode(String link) {
	        try {
	            HttpURLConnection conn = (HttpURLConnection) new URI(link).toURL().openConnection();
	            conn.setRequestMethod("HEAD");
	            conn.setConnectTimeout(3000);
	            conn.connect();
	            return conn.getResponseCode();
	        } catch (Exception e) {
	            return -1;
	        }
	    }

	
}
