package com.eugc;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class synWave {
    //
    protected static final int SAMPLE_RATE = 16 * 1024;


    public static byte[] createSinWaveBuffer(double freq, int ms) {
        int samples = (int)((ms * SAMPLE_RATE) / 1000);
        byte[] output = new byte[samples];
        //
        double period = (double)SAMPLE_RATE / freq;
        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * i / period;
            output[i] = (byte)(Math.sin(angle) * 127f);  }

        return output;
    }



    public static void success() throws LineUnavailableException, InterruptedException {
        final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, SAMPLE_RATE);
        line.start();


        for (int i = 0; i < 5; i++) {
            byte[] toneBuffer = createSinWaveBuffer(400, 150);
            int count = line.write(toneBuffer, 0, toneBuffer.length);
            Thread.sleep(300);
        }

        line.drain();
        line.close();
    }

    public static void error() throws LineUnavailableException {
        final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, SAMPLE_RATE);
        line.start();


        byte[] toneBuffer = createSinWaveBuffer(300, 2000);
        int count = line.write(toneBuffer, 0, toneBuffer.length);

        line.drain();
        line.close();

    }



}