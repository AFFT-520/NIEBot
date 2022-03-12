/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import static com.eugc.PrefFile.PREFS_FILE;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.util.prefs.Preferences;
import org.ini4j.ConfigParser;

/**
 *
 * @author 520
 */

public class CreateSettingsFileBackend extends Thread {
    public static final String PREFS_FILE= "niebot.ini";
    private JPanel panel;
    private String appointmentName;
    private ArrayList<String> coreData;
    private ArrayList<Object> appointmentData;
    
    public CreateSettingsFileBackend(String appointmentName, ArrayList<String> CoreData, ArrayList<Object> AppointmentData){
        this.appointmentName = appointmentName;
        this.coreData=CoreData;
        this.appointmentData=AppointmentData;
    }
    public void clear(){
        try {
            ConfigParser cp = new ConfigParser();
            cp.write(new File(PREFS_FILE));
        } catch (IOException ex) {
            Logger.getLogger(CreateSettingsFileBackend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addCore(){
        String[] settings = {"Timeout", "AlertMode", "RetryMode", "IgnoreOpeningHours", "Region", "AppointmentType"};
        ConfigParser cp = new ConfigParser();
        try {
            
            cp.addSection("Settings");
            for (int i = 0; i < settings.length; i++){
                if (i == 0){
                cp.set("Settings", settings[i], Integer.parseInt(this.coreData.get(i)));
                }
                else{
                cp.set("Settings", settings[i], this.coreData.get(i));
                }
            }
        } catch (ConfigParser.DuplicateSectionException ex) {
            Logger.getLogger(CreateSettingsFileBackend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfigParser.NoSectionException ex) {
            Logger.getLogger(CreateSettingsFileBackend.class.getName()).log(Level.SEVERE, null, ex);
        } 
        addAppointmentType(cp);
        

    }
    
    public void addAppointmentType(ConfigParser cp){
        try {
            String[] CUESettings = {"idType", "PassportNumber", "NIENumber", "NameAndSurname", "SpanishTelephoneNumber", "EmailAddress"};
            String[] TIEFingerprintSettings = {"idType", "PassportNumber", "NIENumber", "NameAndSurname", "SpanishTelephoneNumber", "EmailAddress"};
            String[] TIECollectionSettings = {"NIENumber", "NameAndSurname", "SpanishTelephoneNumber", "EmailAddress"};
            String[] TIEIssuanceSettings = {"NIENumber", "NameAndSurname", "SpanishTelephoneNumber", "EmailAddress"};
            String[] headings;
            switch(appointmentName){
                case "TIEFingerprint":
                    headings = TIEFingerprintSettings;
                    break;
                case "TIECollection":
                    headings = TIECollectionSettings;
                    break;
                case "TIEIssue":
                    headings = TIEIssuanceSettings;
                    break;
                default:
                    headings = CUESettings;
                    break;
            }
            File file = new File(PREFS_FILE);
            cp.addSection(appointmentName);
            for (int i = 0; i < headings.length; i++){
                cp.set(appointmentName, headings[i], appointmentData.get(i));
            }
            cp.write(file);
        } catch (IOException ex) {
            Logger.getLogger(CreateSettingsFileBackend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfigParser.NoSectionException ex) {
            Logger.getLogger(CreateSettingsFileBackend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfigParser.DuplicateSectionException ex) {
            Logger.getLogger(CreateSettingsFileBackend.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void run(){
        clear();
        addCore();
        
    }
    
    
}
