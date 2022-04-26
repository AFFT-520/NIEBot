/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import static com.eugc.Barcelona.CUE.TIMEOUT;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author 520
 */
public class emulateHuman {
    
    private String retryMode;
    
    private ExpectedCondition < Boolean > pageLoad = new ExpectedCondition < Boolean > () {
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                }
            };
    
    public emulateHuman(String rm){
        this.retryMode = rm.toLowerCase();
    }
   
    public void sendKeys(WebElement elem, String msg, DriverHelper dh) throws InterruptedException{
        long priormin = 600L;
        long priormax = 800L; 
        ((JavascriptExecutor) dh.driver).executeScript("arguments[0].scrollIntoView(true);", elem);
        randTypeDelay(priormin, priormax);
        retryMode="re-enter";
        if (retryMode.equals("re-enter")){
            elem.sendKeys(msg);
        }
        else {
            long min = 150L;
            long max = 350L;
            elem.click();
            for(int i = 0; i < msg.length(); i++){
                randTypeDelay(min, max);
                char c = msg.charAt(i);
                elem.sendKeys(Character.toString(c));
            }
        }  
    }
    
    public void selectByVisibleText(WebElement elem, String txt, DriverHelper dh) throws InterruptedException{
        
        long min = 600L;
        long max = 800L;
        long minmenu = 400L;
        long maxmenu = 750L;
        randTypeDelay(min, max);
        ((JavascriptExecutor) dh.driver).executeScript("arguments[0].scrollIntoView(true);", elem);
        Select e = new Select(elem);
        elem.click();
        randTypeDelay(minmenu, maxmenu);
        e.selectByVisibleText(txt);
        randTypeDelay(min, max);
    }
    
    public void click(WebElement elem, DriverHelper dh) throws InterruptedException{
        long min = 800L;
        long max = 1200L;
        ((JavascriptExecutor) dh.driver).executeScript("arguments[0].scrollIntoView(true);", elem);
        randTypeDelay(min, max);
        elem.click();
        
    }
    
    public void delay(String length){
        long min = 0L;
        long max = 0L;
        if (length.equals("short")){
            min = 400L;
            max = 750L;
        }
        else if (length.equals("medium")){
            min = 800L;
            max = 1440L;
        }
        else if (length.equals("long")){
            min = 5000L;
            max = 15000L;
        }
        try {
            randTypeDelay(min, max);
        } catch (InterruptedException ex) {
            Logger.getLogger(emulateHuman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void waitUntil(WebDriverWait wait, ExpectedCondition<WebElement> expect) throws InterruptedException{
        long min = 350L;
        long max = 500L;
        
        wait.until(expect);
        randTypeDelay(min, max);
        
    }
    
    public void waitUntilBool(WebDriverWait wait, ExpectedCondition<Boolean> expect) throws InterruptedException{
        long min = 350L;
        long max = 500L;
        
        wait.until(expect);
        randTypeDelay(min, max);
        
    }
    
    public void refresh (DriverHelper dh, String elementId) throws InterruptedException{
        long min = 100L;
        long max = 200L;
        randTypeDelay(min, max);
        dh.driver.navigate().refresh();
        if (dh.useFirefox){                         //Firefox puts up an alert for resending data upon refresh.
            Alert alert = dh.driver.switchTo().alert();
            randTypeDelay(200L, 400L);
            alert.accept();
        }
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        if (dh.useFirefox){ // Breaks with Chrome, whereas Firefox ignores page reload waits
            wait.until(ExpectedConditions.stalenessOf(dh.driver.findElement(By.id(elementId))));
        }
        wait.until(pageLoad);
        
        
    }
    
    private static void randTypeDelay(long min, long max) throws InterruptedException{
        Random r = new Random();
        long random = min + (long) (Math.random() * (max - min));
        Thread.sleep(random, 0);
    }
    
    
    
}
