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

import com.eugc.DriverHelper;
import com.eugc.GetChromeDriver;
import com.eugc.GetGeckoDriver;
import com.eugc.MessageBox;
import com.eugc.PrefFile;
import com.eugc.emulateHuman;
import com.eugc.errorChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 520
 */
public class CUE {
    public static int TIMEOUT;
    private static AlertHandler ah;
    private static errorChecker ec;
    private static int reqLimit=8000;
    private ArrayList reqsSent = new ArrayList();
    
    private void newReq(MessageBox mb){
        Date now = new Date();
        int timeout = 360; //in seconds
        Long longTime = now.getTime()/1000;
        reqsSent.add(longTime);
        for(int i = 0; i < reqsSent.size(); i++){
            Long time = (Long) reqsSent.get(i);
            if (longTime > (time + timeout)){
                reqsSent.remove(i);
                i = i-1;
            }
        }
        if (reqsSent.size() > reqLimit){
            mb.addLog("Over " + String.valueOf(reqLimit) + " requests sent within the last " + String.valueOf(timeout/60) + " minutes. Slowing down before we get booted.");
            while (reqsSent.size() > 50){
                now = new Date();
                longTime = now.getTime()/1000;
                for(int i = 0; i < reqsSent.size(); i++){
                    Long time = (Long) reqsSent.get(i);
                    if (longTime > (time + timeout)){
                        reqsSent.remove(i);
                        i = i-1;
                    }
                }
                try { 
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CUE.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            mb.addLog("Continuing bot - we should be good to go again");
        }
    }
    


    static {
        
            TIMEOUT = com.eugc.mainClass.TIMEOUT;
        
    }
    
    
    private static MessageBox mb;
    private static final emulateHuman eh = new emulateHuman(PrefFile.getSettings("RetryMode"));
    public CUE() {
        
        mb = new MessageBox("NIEBot - CUE");
        mb.introText();
        mb.setVisible(true);
        
        
        ec = new errorChecker();
        try{
            if (PrefFile.getSettings("driver").toLowerCase().contains("firefox")){
                GetGeckoDriver.getFiles(mb);
            }
            else{
                GetChromeDriver.getFiles(mb);
            }
        } catch (Exception ex) {
            Logger.getLogger(CUE.class.getName()).log(Level.SEVERE, null, ex);
            return;
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
                e.printStackTrace();
                try {
                    ah.alert("error");
                } catch (IOException ex) {
                    Logger.getLogger(CUE.class.getName()).log(Level.SEVERE, null, ex);
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
 
  
    public boolean mainLoop(DriverHelper dh) throws Exception {
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

    public void firstpage(DriverHelper dh) throws Exception {
        dh.saveCookies();
        cookieprompt(dh);
        ec.checkForErrors(dh, mb, ah);
        eh.selectByVisibleText(dh.driver.findElement(By.name("form")), "Barcelona", dh);
        WebElement s = dh.driver.findElement(By.id("btnAceptar"));
        eh.click(s, dh);
        newReq(mb);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("tramiteGrupo[0]")));
        ec.checkForErrors(dh, mb, ah);
        
    }
    public void secondpage(DriverHelper dh) throws Exception {
        dh.saveCookies();
        dh.scroll(0,0,202,206);
        eh.selectByVisibleText(dh.driver.findElement(By.id("tramiteGrupo[0]")), "POLICIA-CERTIFICADO DE REGISTRO DE CIUDADANO DE LA U.E.", dh);
        WebElement s = dh.driver.findElement(By.id("btnAceptar"));
        eh.click(s, dh);
        newReq(mb);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("btnEntrar")));
        ec.checkForErrors(dh, mb, ah);

    }
    public void thirdpage(DriverHelper dh) throws Exception {
        dh.saveCookies();
        WebElement s = dh.driver.findElement(By.id("btnEntrar"));
        eh.click(s, dh);
        newReq(mb);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("rdbTipoDocPas")));
        ec.checkForErrors(dh, mb, ah);

    }

    public void fourthpage(DriverHelper dh) throws Exception {
        dh.saveCookies();
        String id;
        try{
        if(PrefFile.getCUE("idType").equals("NIE")){
            id=PrefFile.getCUE("NIENumber");
        }
        else {
            id=PrefFile.getCUE("PassportNumber");
        }
        }
        catch (Exception e){
            id=PrefFile.getCUE("PassportNumber");
        }
        if(!PrefFile.getCUE("idType").equals("NIE")){
            WebElement r = dh.driver.findElement(By.cssSelector(".w100"));
            eh.click(r, dh);
        }
        
        String name = PrefFile.getCUE("NameAndSurname");
        WebElement e1 = dh.driver.findElement(By.id("txtIdCitado"));
        eh.sendKeys(e1, id, dh);
        WebElement e2 = dh.driver.findElement(By.id("txtDesCitado"));
        eh.sendKeys(e2, name, dh);
        WebElement s = dh.driver.findElement(By.id("btnEnviar"));
        s.click();
        newReq(mb);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        eh.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.id("btnEnviar")));
        ec.checkForErrors(dh, mb, ah);

    }
    public boolean fifthpage(DriverHelper dh) throws Exception {
        dh.saveCookies();
        WebElement s = dh.driver.findElement(By.id("btnEnviar"));
        eh.click(s, dh);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSalir")));
        String body = dh.driver.getPageSource();
        String retryMode = PrefFile.getSettings("RetryMode").toLowerCase();
        
        if (retryMode.equals("refresh")){
                    
            while (body.contains("En este momento no hay citas disponibles")){
                
                eh.refresh(dh, "btnSalir");
                newReq(mb);
                
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
            ec.checkForErrors(dh, mb, ah);
            return true;
        }
        return false;
        }
    }

    public Boolean sixthpage(DriverHelper dh) throws Exception {
        dh.saveCookies();
         //Time is of the essence at this stage. Do not bother emuating a human
        WebElement oficina = dh.driver.findElement(By.id("idSede"));
        Select e = new Select(oficina);
        if (e.getFirstSelectedOption().getText().contains("Seleccionar")){
            e.selectByIndex(1);
        }
        WebElement s = dh.driver.findElement(By.id("btnSiguiente"));
        s.click();
        newReq(mb);
        WebDriverWait wait = new WebDriverWait(dh.driver, TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("txtTelefonoCitado")));
        String body = dh.driver.getPageSource();
        if (body.contains("En este momento no hay citas disponibles")){
            ec.checkForErrors(dh, mb, ah);
            eh.delay("short");
            return false;
        }
        ec.checkForErrors(dh, mb, ah);
        return true;
        

    }

    public boolean seventhpage(DriverHelper dh) throws Exception {
        dh.saveCookies();
        String phoneNumber = PrefFile.getCUE("SpanishTelephoneNumber");
        String email = PrefFile.getCUE("EmailAddress");
        WebElement tel = dh.driver.findElement(By.id("txtTelefonoCitado"));
        //Time is of the essence at this stage. Do not bother emuating a human
        tel.sendKeys(phoneNumber);
        WebElement email1 = dh.driver.findElement(By.id("emailUNO"));
        email1.sendKeys(email);
        WebElement email2 = dh.driver.findElement(By.id("emailDOS"));
        email2.sendKeys(email);
        WebElement s = dh.driver.findElement(By.id("btnSiguiente"));
        s.click();
        newReq(mb);
        TimeUnit.SECONDS.sleep(2);
        String body = dh.driver.getPageSource();
        if (body.contains("En este momento no hay citas disponibles")){
            ec.checkForErrors(dh, mb, ah);
            WebElement submit = dh.driver.findElement(By.id("btnSubmit"));
            eh.click(submit, dh);
            newReq(mb);
            return false;
        }
        ec.checkForErrors(dh, mb, ah);
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
