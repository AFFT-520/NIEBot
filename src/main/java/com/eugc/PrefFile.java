/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

/**
 *
 * @author 520
 */
public class PrefFile {
    public static final String PREFS_FILE= "niebot.ini";
    
    public static final Boolean exists(){
        File f = new File(PREFS_FILE);
        return(f.exists());
    }
    
    public static final String getCUE(String cat) throws IOException {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("CUE").get(cat, null);
            return(detail);

    }
    
    public static final String getNIE(String cat) throws IOException {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("NIE").get(cat, null);
            return(detail);

    }
        public static final String getTIEFingerprint(String cat) throws IOException {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("TIEFingerprint").get(cat, null);
            return(detail);
        }   
        public static final String getTIEIssue(String cat) throws IOException {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("TIEIssue").get(cat, null);
            return(detail);

    }
        public static final String getTIECollection(String cat) throws IOException {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("TIECollection").get(cat, null);
            return(detail);

    }
    public static final String getCertificados(String cat) throws IOException {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("Certificados").get(cat, null);
            return(detail);

    }
    public static final String getEmail(String cat) throws IOException {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("Email").get(cat, null);
            return(detail);

    }
    public static final String getSettings(String cat) {
        try {
            Preferences prefs = new IniPreferences(new Ini(new File(PREFS_FILE)));
            String detail=prefs.node("Settings").get(cat, null);
            return(detail);
        } catch (IOException ex) {
            Logger.getLogger(mainClass.class.getName()).log(Level.SEVERE, null, ex);
            return("");
        }

    }
    
}
