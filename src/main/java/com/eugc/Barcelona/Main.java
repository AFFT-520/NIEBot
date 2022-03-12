/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eugc.Barcelona;


/**
 *
 * @author 520
 */

//I included a few additional functions that are not available in the GUI. Upon further analysis, botting for these functions was deemed unneeded, thus functinality was not added to the GUI
public class Main {
    public Main(String function){
        switch(function){
            case "CUE":
                new CUE();
                break;
            case "TIEFingerprint":
                new TIEFingerprint();
                break;
            case "TIECollection":
                new TIECollection();
                break;
            case "TIEIssue":
                new TIEIssue();
                break;
            case "NIE":
                new NIE();
                break;
            case "Certificados":
                new Certificados();
                break;
        }
    }  
}
