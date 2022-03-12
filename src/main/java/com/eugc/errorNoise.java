package com.eugc;

import javax.sound.sampled.LineUnavailableException;

public class errorNoise extends Thread{
    public void run() {
        try {
            synWave.error();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
