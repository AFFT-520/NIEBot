/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import static com.eugc.Barcelona.CUE.TIMEOUT;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.eugc.DDOSException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author 520
 */
public class DriverHelper {
    String sep = System.getProperty("file.separator");
    public static Boolean isWindows = GetGeckoDriver.isWindows();
    public Boolean useFirefox = false;
    public Boolean useChrome = false;
    private static String cookiesFile = "niebot-cookies.dat";
    public static int TIMEOUT = mainClass.TIMEOUT;
    
    public WebDriver driver;
        
    public void Init(){
        decideDriver();
        if (useFirefox){
            if (isWindows) {
                System.setProperty("webdriver.gecko.driver", "." + sep + "GeckoDriver" + sep + "geckodriver.exe");
            } else {
                System.setProperty("webdriver.gecko.driver", "." + sep + "GeckoDriver" + sep + "geckodriver");
            }
        }
        else if (useChrome){
           if (isWindows) {
                System.setProperty("webdriver.chrome.driver", "." + sep + "ChromeDriver" + sep + "chromedriver.exe");
            } else {
                System.setProperty("webdriver.chrome.driver", "." + sep + "ChromeDriver" + sep + "chromedriver");
            } 
        }
        
        
    
    }
    
    public WebDriver TestRun(MessageBox mb){
        WebDriver wd;
        if (useFirefox){
           wd = TestRunFirefox(mb);
        }
        else {
           wd = TestRunChrome(mb);
        }
        return(wd);
    }
    
    private WebDriver TestRunFirefox(MessageBox mb){
        mb.addLog("Checking this version of GeckoDriver for problems...");
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
        private WebDriver TestRunChrome(MessageBox mb){
            mb.addLog("Checking this version of ChromeDriver for problems...");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        try{
            driver = new ChromeDriver(options);
            driver.quit();
            return(driver);
        }
        catch (Exception e){
            mb.addLog("Error loading ChromeDriver: " + e.toString());
            return null;
        }
}
    public void saveCookies(){
        File file = new File(cookiesFile);							
        try		
        {	  
            // Delete old file if exists
			file.delete();		
            file.createNewFile();			
            FileWriter fileWrite = new FileWriter(file);							
            BufferedWriter Bwrite = new BufferedWriter(fileWrite);							
            // loop for getting the cookie information 		
            	
            // loop for getting the cookie information 		
            for(Cookie ck : driver.manage().getCookies())							
            {			
                Bwrite.write((ck.getName()+";"+ck.getValue()+";"+ck.getDomain()+";"+ck.getPath()+";"+ck.getExpiry()+";"+ck.isSecure()));																									
                Bwrite.newLine();             
            }			
            Bwrite.close();			
            fileWrite.close();	
            
        }
        catch(Exception ex)					
        {		
            ex.printStackTrace();			
        }		
    }
    public void loadCookies(){
        try{			
     
        File file = new File(cookiesFile);							
        FileReader fileReader = new FileReader(file);							
        BufferedReader Buffreader = new BufferedReader(fileReader);							
        String strline;			
        while((strline=Buffreader.readLine())!=null){									
            StringTokenizer token = new StringTokenizer(strline,";");									
            while(token.hasMoreTokens()){					
                String name = token.nextToken();					
                String value = token.nextToken();					
                String domain = token.nextToken();					
                String path = token.nextToken();					
                Date expiry = null;					

                String val;			
                if(!(val=token.nextToken()).equals("null"))
                        {		
                        expiry = new Date(val);					
                }		
                Boolean isSecure = new Boolean(token.nextToken()).								
                booleanValue();		
                Cookie ck = new Cookie(name,value,domain,path,expiry,isSecure);			
                driver.manage().addCookie(ck); // This will add the stored cookie to your current session					
                }		
            }		
        }
        catch(Exception ex){					
            ex.printStackTrace();			
        }		
    }
    
    public void Start() throws DDOSException {
        if (useFirefox){
            StartFirefox();
        }
        else {
            StartChrome();
        }
    }
    
    private void StartChrome() throws DDOSException{
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        
        //profile.setPreference("javascript.enabled", false);
        //options.setProfile(profile);
        driver = new ChromeDriver(options);
        //loadCookies();
        ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"); //Removes navigator webdriver telltale sign
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
        //initialPages();
        try{
            driver.get("https://sede.administracionespublicas.gob.es/icpplus/index.html");
        }
        catch (org.openqa.selenium.TimeoutException e){
            throw new DDOSException("We have likely triggered DDOS Protection. Hibernating for an hour.");
        }
        
    }
    private void StartFirefox() throws DDOSException{
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        //profile.setPreference("javascript.enabled", false);
        //options.setProfile(profile);
        driver = new FirefoxDriver(options);
        //loadCookies();
        ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"); //Removes navigator webdriver telltale sign
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
        //initialPages();
        try{
            driver.get("https://sede.administracionespublicas.gob.es/icpplus/index.html");
        }
        catch (org.openqa.selenium.TimeoutException e){
            throw new DDOSException("We have likely triggered DDOS Protection. Hibernating for an hour.");
        }
        
    }
    
    public void scroll(float xmin, float xmax, float ymin, float ymax){
        Random r = new Random();
        float xrandom = xmin + (float) (Math.random() * (xmax - xmin));
        float yrandom = ymin + (float) (Math.random() * (ymax - ymin));
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(" + String.valueOf(xrandom) + "," + String.valueOf(yrandom) + ")");
        try {
            randTypeDelay(600L, 800L);
        } catch (InterruptedException ex) {
            Logger.getLogger(DriverHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void Restart() throws DDOSException{
        if(useFirefox){
            RestartFirefox();
        }
        else{
            RestartChrome();
        }
    }
    
    private void RestartChrome() throws DDOSException{
        driver.quit(); 
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);;
        
        
        driver = new ChromeDriver(options);
        ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"); //Removes navigator webdriver telltale sign
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
        //initialPages();
        try{
            driver.get("https://sede.administracionespublicas.gob.es/icpplus/index.html");
        }
        catch (org.openqa.selenium.TimeoutException e){
            throw new DDOSException("We have likely triggered DDOS Protection. Hibernating for an hour.");
        }
    }
    
    private void RestartFirefox() throws DDOSException{
        driver.quit();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        driver = new FirefoxDriver(options);
        //loadCookies();
        ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"); //Removes navigator webdriver telltale sign
        driver.manage().timeouts().pageLoadTimeout(TIMEOUT, TimeUnit.SECONDS);
        //initialPages();
        try{
            driver.get("https://sede.administracionespublicas.gob.es/icpplus/index.html");
        }
        catch (org.openqa.selenium.TimeoutException e){
            throw new DDOSException("We have likely triggered DDOS Protection. Hibernating for an hour.");
        }
    }
    
    private void initialPages(){
        driver.get("https://sede.administracionespublicas.gob.es/");
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        Actions a = new Actions(driver);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("mf-appmenu--submenu__0")));
        try {
            randTypeDelay(600L, 1400L);
        } catch (InterruptedException ex) {
            Logger.getLogger(DriverHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebElement menu = driver.findElement(By.id("mf-appmenu--submenu__0"));
        a.moveToElement(menu).perform();
        try {
            randTypeDelay(300L, 400L);
        } catch (InterruptedException ex) {
            Logger.getLogger(DriverHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebElement extranjeria = driver.findElement(By.className("cat_extranjeria"));
        extranjeria.click();
        try {
            randTypeDelay(400L, 600L);
        } catch (InterruptedException ex) {
            Logger.getLogger(DriverHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebElement citaprevia = driver.findElement(By.xpath("/html/body/div/div[1]/main/div[2]/div/section/div[2]/ul/li/ul/li[1]/div[1]/p/a"));
        wait.until(ExpectedConditions.elementToBeClickable(citaprevia));
        try {
            randTypeDelay(400L, 600L);
        } catch (InterruptedException ex) {
            Logger.getLogger(DriverHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        citaprevia.click();
        WebElement submit = driver.findElement(By.id("submit"));
        wait.until(ExpectedConditions.elementToBeClickable(submit));
        try {
            randTypeDelay(400L, 600L);
        } catch (InterruptedException ex) {
            Logger.getLogger(DriverHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        submit.click();
        
       
        
    }
    
    private static void randTypeDelay(long min, long max) throws InterruptedException{
        Random r = new Random();
        long random = min + (long) (Math.random() * (max - min));
        Thread.sleep(random, 0);
    }
    
    private void decideDriver(){
        PrefFile pf = new PrefFile();
        String driverName=pf.getSettings("driver");
        if (driverName.toLowerCase().contains("firefox")){
            useFirefox = true;
        }
        else {
            useChrome = true;
        }
    }
    
}
