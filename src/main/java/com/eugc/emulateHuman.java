/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author 520
 */
public class emulateHuman {
    
    private String retryMode;
    
    public emulateHuman(String rm){
        this.retryMode = rm.toLowerCase();
    }
   
    public void sendKeys(WebElement elem, String msg) throws InterruptedException{
        retryMode="re-enter";
        if (retryMode.equals("re-enter")){
            elem.sendKeys(msg);
        }
        else {
            long min = 25L;
            long max = 75L;
            elem.click();
            for(int i = 0; i < msg.length(); i++){
                randTypeDelay(min, max);
                char c = msg.charAt(i);
                elem.sendKeys(Character.toString(c));
            }
        }  
    }
    
    public void selectByVisibleText(WebElement elem, String txt) throws InterruptedException{
        
        long min = 50L;
        long max = 150L;
        long minmenu = 150L;
        long maxmenu = 225L;
        Select e = new Select(elem);
        elem.click();
        //randTypeDelay(minmenu, maxmenu);
        e.selectByVisibleText(txt);
        //randTypeDelay(min, max);
    }
    
    public void click(WebElement elem) throws InterruptedException{
        long min = 200L;
        long max = 400L;
        randTypeDelay(min, max);
        elem.click();
        
    }
    
    public void delay(String length){
        long min = 0L;
        long max = 0L;
        if (length.equals("short")){
            min = 200L;
            max = 550L;
        }
        else if (length.equals("medium")){
            min = 600L;
            max = 1240L;
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
        long min = 250L;
        long max = 400L;
        randTypeDelay(min, max);
        wait.until(expect);
        
    }
    
    public void waitUntilBool(WebDriverWait wait, ExpectedCondition<Boolean> expect) throws InterruptedException{
        long min = 250L;
        long max = 400L;
        randTypeDelay(min, max);
        wait.until(expect);
        
    }
    
    private static void randTypeDelay(long min, long max) throws InterruptedException{
        Random r = new Random();
        long random = min + (long) (Math.random() * (max - min));
        Thread.sleep(random, 0);
    }
    
}
