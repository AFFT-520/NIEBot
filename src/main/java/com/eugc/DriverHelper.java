/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 *
 * @author 520
 */
public class DriverHelper {
    String sep = System.getProperty("file.separator");
    public static Boolean isWindows = GetGeckoDriver.isWindows();
    
    public static int TIMEOUT = mainClass.TIMEOUT;
    
    public WebDriver driver;
    
    public void Init(){
        if (isWindows) {
            System.setProperty("webdriver.gecko.driver", "." + sep + "GeckoDriver" + sep + "geckodriver.exe");
        } else {
            System.setProperty("webdriver.gecko.driver", "." + sep + "GeckoDriver" + sep + "geckodriver");
        }
        
    
    }
    
    public WebDriver TestRun(MessageBox mb){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        try{
            driver = new FirefoxDriver(options);
            driver.quit();
            return(driver);
        }
        catch (Exception e){
            mb.addLog("Error loading GeckoDriver: " + e.toString());
            return null;
        }
}
    public void Start(){
        FirefoxProfile profile = new FirefoxProfile();
        FirefoxOptions options = new FirefoxOptions();
        //profile.setPreference("javascript.enabled", false);
        //options.setProfile(profile);
        driver = new FirefoxDriver(options);
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
        
    }
    
    public void Restart(){
        driver.close();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
    }
    
}
