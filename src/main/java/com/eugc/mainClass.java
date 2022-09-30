package com.eugc;



import static com.eugc.Barcelona.CUE.TIMEOUT;
import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;


import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;


public class mainClass {
    
    public static int TIMEOUT;
        

    public static boolean ERROR_NOISE;
    private static Object lock = new Object();
    public static void main(String[] args){
    
    PrefFile pf = new PrefFile();
    if(!pf.exists()){
        CreateSettingsFile csf = new CreateSettingsFile();
        csf.setVisible(true);
        while (csf.isVisible()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(mainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        try {
            Thread.sleep(1000); // This is to allow the config file time to be written
        } catch (InterruptedException ex) {
            Logger.getLogger(mainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    else {
        String msg = "Existing config file detected. Would you like to resume the last appointment hunting session?";
        String title = "NIEBot";
        int db = JOptionPane.showConfirmDialog(null, msg, title, YES_NO_CANCEL_OPTION);
        switch(db){
            case 0:
                break;
            case 1:
                File f = new File(PrefFile.PREFS_FILE);
                f.delete();
                break;
            case 2:
                System.exit(0);
                break;
        }
    }
    
    String regionName;
    String function;
    
    //error checking the config file - at least the core settings part anyway
    try{
        TIMEOUT = parseInt(PrefFile.getSettings("Timeout"));
        regionName = PrefFile.getSettings("Region");
        function = PrefFile.getSettings("AppointmentType");
    }
    catch(Exception e){
        CreateSettingsFile csf = new CreateSettingsFile();
        csf.setVisible(true);
        while (csf.isVisible()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(mainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (TIMEOUT == 0){
            try{
                TIMEOUT = parseInt(PrefFile.getSettings("Timeout"));
            }
            catch (Exception ex){
                TIMEOUT = 15;
            }
        }
        regionName = PrefFile.getSettings("Region");
        function = PrefFile.getSettings("AppointmentType");
    }
    
    //Barcelona is the only province in scope at this time
    switch (regionName.toLowerCase()){
        case "barcelona":
            com.eugc.Barcelona.Main region = new com.eugc.Barcelona.Main(function);
    }
} 
    
            
          

}

