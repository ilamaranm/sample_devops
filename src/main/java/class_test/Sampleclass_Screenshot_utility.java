package class_test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Sampleclass_Screenshot_utility {
	
	public static String takeScreenshot(WebDriver driver, int screenshotNumber) {
        // Take screenshot and save it to a file
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotPath = "screenshots/screenshot" + screenshotNumber + ".png";
        File screenshotFile = new File(screenshotPath);
        
        try {
            FileUtils.copyFile(screenshot, screenshotFile);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }

        return screenshotPath;
    }

    public static BufferedImage getScreenshotAsBufferedImage(WebDriver driver, int screenshotNumber) {
        String screenshotPath = takeScreenshot(driver, screenshotNumber);
        BufferedImage screenshotImage = null;

        try {
            screenshotImage = ImageIO.read(new File(screenshotPath));
        } catch (IOException e) {
            System.out.println("Failed to read screenshot as BufferedImage: " + e.getMessage());
        }

        return screenshotImage;
    }

}
