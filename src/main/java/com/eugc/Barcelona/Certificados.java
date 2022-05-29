/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc.Barcelona;

import com.eugc.AlertHandler;
import com.eugc.DDOSException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.eugc.AppointmentGlobals;
import com.eugc.DriverHelper;
import com.eugc.GetChromeDriver;
import com.eugc.GetGeckoDriver;
import com.eugc.MessageBox;
import com.eugc.PrefFile;
import com.eugc.emulateHuman;
import com.eugc.errorChecker;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 520
 */
public class Certificados extends AppointmentGlobals {
    public static int TIMEOUT;

    public static boolean ERROR_NOISE;
    


    static {
        
            TIMEOUT = com.eugc.mainClass.TIMEOUT;
        
    }


    
    
    private static MessageBox mb;
    private static AlertHandler ah;
    private static final emulateHuman eh = new emulateHuman(PrefFile.getSettings("RetryMode"));
    public Certificados() {
        
        mb = new MessageBox("NIEBot - Certificados");
        mb.introText();
        mb.setVisible(true);
        try{
            if (PrefFile.getSettings("driver").toLowerCase().contains("firefox")){
                GetGeckoDriver.getFiles(mb);
            }
            else{
                GetChromeDriver.getFiles(mb);
            }
        } catch (Exception ex) {
            Logger.getLogger(Certificados.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        
        DriverHelper dh = new DriverHelper();
        
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
        public void run(){
            dh.driver.close();
        }
        });
        dh.Init();
        WebDriver test = dh.TestRun(mb);
        
        if (test == null){
            return;
        }
        test.quit();

        /*
        errorNoise test1 = new errorNoise();
        test1.start();
        successNoise test2 = new successNoise();
        test2.start();
         */

        boolean breakloop = false;
        mb.addLog("Starting the Cita Previa bot.");
         try{
            dh.Start();
        }
        catch (DDOSException e){
            errorChecker.DDOSChecker(dh, mb, ah);
        }
        
        while(!breakloop){
            try {
                breakloop = mainLoop(dh);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                try {
                    ah.alert("error");
                } catch (IOException ex) {
                    Logger.getLogger(Certificados.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!breakloop){
                     try{
                            dh.Restart();
                        }
                        catch (DDOSException f){
                            errorChecker.DDOSChecker(dh, mb, ah);
                        }
                }

            }
        }
    }
 
    
    public static void checkForErrors(DriverHelper dh) throws Exception {
        String source = dh.driver.getPageSource();
        if ( source.contains("Se ha producido un error en el sistema, por favor inténtelo de nuevo. En el caso de que el error persista, puede obtener ayuda a través del siguiente") || source.contains("Su sesión ha caducado por permanecer demasiado tiempo inactiva.")){
            mb.addLog("The site has thrown an error!");
            ah.alert("error");
            dh.Restart();
        }
    }
    public static boolean mainLoop(DriverHelper dh) throws Exception {
        boolean breakloop = false;
       

        while (!breakloop) {
            firstpage(dh);
            secondpage(dh);
            thirdpage(dh);
            fourthpage(dh);
            breakloop=fifthpage(dh);
            if (breakloop){
                mb.addLog("Found an appointment! Dropping all pretense of being human to lock in this appointment quickly");
                breakloop=sixthpage(dh);
                if (breakloop){
                    breakloop=seventhpage(dh);
                        if (breakloop){
                            mb.addLog("You have 5 minutes to book your slot! GO GO GO!");
                            ah.alert("success");

                        }
                }
                else{
                mb.addLog("Darn! The bot wasn't able to lock it in. It'll keep trying!"); 
                }


            }


        }
        return(breakloop);
    }

    public static void firstpage(DriverHelper dh) throws Exception {
        if (!dh.driver.getCurrentUrl().equals(URL)){
            dh.driver.get(URL);
        }
        cookieprompt(dh);
        checkForErrors(dh);
        eh.selectByVisibleText(dh.driver.findElement(By.name("form")), "Barcelona", dh);
        WebElement s = dh.driver.findElement(By.id("btnAceptar"));
        eh.click(s, dh);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("tramiteGrupo[0]")));
        checkForErrors(dh);
        
    }
    public static void secondpage(DriverHelper dh) throws Exception {
        eh.selectByVisibleText(dh.driver.findElement(By.id("tramiteGrupo[0]")), "POLICIA-CERTIFICADOS (DE RESIDENCIA, DE NO RESIDENCIA Y DE CONCORDANCIA)", dh);
        WebElement s = dh.driver.findElement(By.id("btnAceptar"));
        eh.click(s,dh);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("btnEntrar")));
        checkForErrors(dh);

    }
    public static void thirdpage(DriverHelper dh) throws Exception {
        WebElement s = dh.driver.findElement(By.id("btnEntrar"));
        eh.click(s,dh);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("rdbTipoDocPas")));
        checkForErrors(dh);

    }

    public static void fourthpage(DriverHelper dh) throws Exception {
        String id;
        try{
        if(PrefFile.getCertificados("idType").equals("NIE")){
            id=PrefFile.getCertificados("NIENumber");
        }
        else {
            id=PrefFile.getCertificados("PassportNumber");
        }
        }
        catch (Exception e){
            id=PrefFile.getCertificados("PassportNumber");
        }
        if(PrefFile.getCertificados("idType").equals("Passport")){
            WebElement r = dh.driver.findElement(By.id("rdbTipoDocPas"));
            eh.click(r,dh);
        }
        
        String name = PrefFile.getCertificados("NameAndSurname");
        WebElement e1 = dh.driver.findElement(By.id("txtIdCitado"));
        eh.sendKeys(e1, id,dh);
        WebElement e2 = dh.driver.findElement(By.id("txtDesCitado"));
        eh.sendKeys(e2, name,dh);
        WebElement s = dh.driver.findElement(By.id("btnEnviar"));
        s.click();
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("btnEnviar")));
        checkForErrors(dh);

    }
    public static boolean fifthpage(DriverHelper dh) throws Exception {
        WebElement s = dh.driver.findElement(By.id("btnEnviar"));
        eh.click(s,dh);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSalir")));
        String body = dh.driver.getPageSource();
        String retryMode = PrefFile.getSettings("RetryMode").toLowerCase();
        
        if (retryMode.equals("refresh")){
            ExpectedCondition < Boolean > pageLoad = new ExpectedCondition < Boolean > () {
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                }
            };

            
            while (body.contains("En este momento no hay citas disponibles")){
                
                dh.driver.navigate().refresh();
                Alert alert = dh.driver.switchTo().alert();
                alert.accept();
                wait.until(ExpectedConditions.stalenessOf(dh.driver.findElement(By.id("btnSalir"))));
                wait.until(pageLoad);
                body=dh.driver.getPageSource();
                if (body.contains("ATENCIÓN, LEA ATENTAMENTE ANTES DE ACEPTAR UNA CITA:")){
                    return(false);
                }
                /*
                else if (body.contains("En este momento no hay citas disponibles.")){
                    eh.delay("medium");
                }*/
                else if (body.contains("Seleccione la oficina donde solicitar la cita")){
                    return true;
                }
                wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSalir")));
                
                
                

            }
            return true;
        }
        else {
        if (!body.contains("En este momento no hay citas disponibles")){
            checkForErrors(dh);
            return true;
        }
        return false;
        }
    }

    public static Boolean sixthpage(DriverHelper dh) throws Exception {
         //Time is of the essence at this stage. Do not bother emuating a human
        WebElement oficina = dh.driver.findElement(By.id("idSede"));
        Select e = new Select(oficina);
        if (e.getFirstSelectedOption().getText().contains("Seleccionar")){
            e.selectByIndex(1);
        }
        WebElement s = dh.driver.findElement(By.id("btnSiguiente"));
        s.click();
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("txtTelefonoCitado")));
        String body = dh.driver.getPageSource();
        if (body.contains("En este momento no hay citas disponibles")){
            checkForErrors(dh);
            return false;
        }
        checkForErrors(dh);
        return true;
        

    }

    public static boolean seventhpage(DriverHelper dh) throws Exception {
        String phoneNumber = PrefFile.getCertificados("SpanishTelephoneNumber");
        String email = PrefFile.getCertificados("EmailAddress");
        WebElement tel = dh.driver.findElement(By.id("txtTelefonoCitado"));
        //Time is of the essence at this stage. Do not bother emuating a human
        tel.sendKeys(phoneNumber);
        WebElement email1 = dh.driver.findElement(By.id("emailUNO"));
        email1.sendKeys(email);
        WebElement email2 = dh.driver.findElement(By.id("emailDOS"));
        email2.sendKeys(email);
        WebElement s = dh.driver.findElement(By.id("btnSiguiente"));
        s.click();
        TimeUnit.SECONDS.sleep(2);
        String body = dh.driver.getPageSource();
        if (body.contains("En este momento no hay citas disponibles")){
            checkForErrors(dh);
            return false;
        }
        checkForErrors(dh);
        return true;

    }



    public static void cookieprompt(DriverHelper dh){
        try {
            WebElement e = dh.driver.findElement(By.id("cookie_action_close_header"));
            e.click();
        }
        catch (Exception e){

        }
    }
}
