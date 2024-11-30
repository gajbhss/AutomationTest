package test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChallengeTest {

    @Test
    public void setup() throws InterruptedException {

        WebDriver driver= new ChromeDriver();
        driver.get("https://www.tendable.com");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //Confirm accessibility of the top-level menus: Home, Our Story, Our Solution, and Why Tendable.
        //Verify that the "Request a Demo" button is present and active on each of the aforementioned top-level menu pages.
        List<WebElement> menus= driver.findElements(By.xpath("//a[@class='navbar7_link w-nav-link']"));


        for(WebElement e:menus){
            System.out.println(e.getText());
            e.click();
            Thread.sleep(3000);
            boolean status= driver.findElement(By.xpath("//*[@class='button is-small w-button']")).isDisplayed();
            System.out.println("Status"+ status);
            Thread.sleep(5000);

            try{
                WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
                WebElement closeIcon= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='interactive-close-button']")));
                closeIcon.click();
                System.out.println("Clicked on cross Icon");
            }catch (Exception errormsg){
                System.out.println(errormsg.getMessage());
            }

        }

        //Navigate to the "Contact Us" section, choose "Marketing", and complete the form—
        //excluding the "Message" field. On submission, an error should arise. Ensure your script
        //confirms the error message's appearance. If the error is displayed, mark the test as PASS. If
        //absent, it's a FAIL.
        driver.findElement(By.xpath("(//a[@href='/contact'])[1]")).click();

        driver.findElement(By.xpath("(//input[@name='email'])[1]")).sendKeys("Sanket@gmail.com");
        driver.findElement(By.xpath("(//input[@name='firstname'])[1]")).sendKeys("Sanket");
        driver.findElement(By.xpath("(//input[@name='lastname'])[1]")).sendKeys("Dummy");
        driver.findElement(By.xpath("(//input[@name='company'])[1]")).sendKeys("Abc pvt ltd");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,250)", "");

        WebElement dropDown= driver.findElement(By.xpath("(//label[contains(text(),'Message type')]//following-sibling::select)[1]"));

        Select sel= new Select(dropDown);

        sel.selectByVisibleText("Marketing");

        Thread.sleep(4000);

        WebElement submitButton= driver.findElement(By.xpath("(//button[@class='button'])[1]"));

        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

        submitButton.click();

        Thread.sleep(4000);

        String expectedText= "Form Submission Failed";

        String actualText= driver.findElement(By.xpath("//*[contains(text(),'Form Submission Failed')]")).getText();

        Assert.assertEquals(actualText, expectedText);

        driver.quit();

    }
}
