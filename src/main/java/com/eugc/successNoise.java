package com.eugc;

import javax.sound.sampled.LineUnavailableException;

public class successNoise extends Thread {
    public void run(){
        try {
            synWave.success();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
