/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc;
import es.blackleg.jlibnotify.core.DefaultLibNotifyLoader;
import es.blackleg.jlibnotify.LibNotify;
import es.blackleg.jlibnotify.Notification;
import es.blackleg.jlibnotify.ServerInfo;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 520
 */
public class Notify {
    private static String os = System.getProperty("os.name");
    public void alert(String title, String text){
        if (os.toLowerCase().contains("linux")){
            alertLinux(title, text);
        }
        else if (os.toLowerCase().contains("windows")){
            
            try {
                alertWindows(title, text);
            } catch (AWTException ex) {
                Logger.getLogger(Notify.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void alertLinux(String title, String text ){
        try{
        DefaultLibNotifyLoader ln = new DefaultLibNotifyLoader();
        LibNotify test = ln.load();
        test.init("NIEBot");
        Notification testnot = test.createNotification(title, text , os); //having an enpty string in the third argument, despite not actually being used, crashes the entire JVM, so using os as a substitute value
        test.showNotification(testnot);
        }
        catch(RuntimeException e){
            Logger.getLogger(Notify.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void alertWindows(String title, String text) throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "NIEBot");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip(title);
        tray.add(trayIcon);

        trayIcon.displayMessage(title, text, java.awt.TrayIcon.MessageType.INFO);
    }
    

}
