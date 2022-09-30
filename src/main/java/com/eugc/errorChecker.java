/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author conor
 */
public class errorChecker {
    private static int STANDBY_MINS = 60;
    private static int SHORT_STANDBY_MINS = 15;
    public static void checkForErrors(DriverHelper dh, MessageBox mb, AlertHandler ah) throws Exception {
        int errorCode = -99;
        while (errorCode != 0){
            errorCode = errorConditions(dh);
            switch(errorCode){
                case 1:
                    mb.addLog("The site has thrown an error!");
                    ah.alert("error");
                    dh.Restart();
                    break;
                case 2:
                    mb.addLog("We've likely triggered a cooldown timer! These seem to be unavoidable. Hibernating the bot for " + String.valueOf(SHORT_STANDBY_MINS) + " minutes.");
                    Thread.sleep(1000 * 60 * SHORT_STANDBY_MINS);  
                    dh.Restart();
                    break;
                    
            }
        }
    }
    
    public static void DDOSChecker(DriverHelper dh, MessageBox mb, AlertHandler ah){
        Boolean DDOSCheck= true; //We wouldn't be calling this if we didn't get a timeout (which indicates we triggered DDOS protections), so set this as true right off the bat.
        while (DDOSCheck){
            mb.addLog("Got a Timeout. This is indicative that we have triggered DDOS protections. Hibernating the bot for " + String.valueOf(STANDBY_MINS) + " minutes.");
            try {
                Thread.sleep(1000 * 60 * STANDBY_MINS);
            } catch (InterruptedException ex) {
                Logger.getLogger(errorChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                dh.Restart();
            } catch (DDOSException ex) {
                DDOSChecker(dh, mb, ah);
            }
        }
    }
    
    public static int errorConditions(DriverHelper dh){
        int errorCode = 0;
        String source = dh.driver.getPageSource();
        if ( source.contains("Se ha producido un error en el sistema, por favor inténtelo de nuevo. En el caso de que el error persista, puede obtener ayuda a través del siguiente") || source.contains("Su sesión ha caducado por permanecer demasiado tiempo inactiva.")){
            errorCode = 1;
        }
        else if (source.contains("Ha ocurrido un error, vuelva a intentarlo más tarde.")){
           errorCode = 2;
        }
        else if (dh.driver.getTitle().equals("429 Too Many Requests")){
           errorCode = 2;
        }
        else {
            errorCode =0;
        }
        return errorCode;

    }
    
    
}
