package com.translate;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Recorder {
    private TargetDataLine line;
    private Thread recordingThread;
    private File wavFile;

    public Recorder(String filePath) {
        this.wavFile = new File(filePath);
    }

    public void Start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Line not supported");
                return;
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Run recording in a separate thread
            recordingThread = new Thread(() -> {
                try (AudioInputStream ais = new AudioInputStream(line)) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            recordingThread.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void Stop() {
        if (line != null) {
            line.stop();
            line.close();
        }
        try {
            if (recordingThread != null) {
                recordingThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;       // 16 kHz
        int sampleSizeInBits = 16;         // 16 bits per sample
        int channels = 1;                  // Mono
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
