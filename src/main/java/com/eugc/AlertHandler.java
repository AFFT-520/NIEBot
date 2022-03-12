/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;


/**
 *
 * @author 520
 */
public class AlertHandler {
    private Boolean firstRun = true;

    
    
    public static void alert(String status) throws IOException{
        String alertMode = PrefFile.getSettings("AlertMode").toLowerCase();
        if (alertMode.equals("noise")){
            if (!status.toLowerCase().equals("error")){
                successNoise sn = new successNoise();
                sn.start();
            }
            else if (status.toLowerCase().equals("error")){
                errorNoise en = new errorNoise();
                en.start();
            }
        }
        else if (alertMode.equals("notify")){
            Notify n = new Notify();
            String statusText;
            if (status.toLowerCase().equals("error")){
                statusText = "The bot ran into an issue. Restart the bot app if there appears to be no activity onscreen";
            }
            else if (status.toLowerCase().equals("success")){
                statusText = "The bot has locked in a potential appointment! Hurry and fill out the rest of the form! You only have 5 minutes!";
        }
            else {
                statusText = "Something went horribly wrong and I am stumped. Sorry!";
            }
            n.alert("NIEBot - " + status, statusText);
        }
        
        
    }

 public static void alertTest(String method) throws IOException{
        String alertMode = method.toLowerCase();
        if (alertMode.equals("noise")){
            successNoise sn = new successNoise();
            sn.start();
            
        }
        else if (alertMode.equals("os notify")){
            Notify n = new Notify();
            n.alert("NIEBot - Test Alert", "This is a test alert");
        }
        
        
    }    
}
