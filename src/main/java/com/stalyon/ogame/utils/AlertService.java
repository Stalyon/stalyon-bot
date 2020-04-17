package com.stalyon.ogame.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Service
public class AlertService {

    private static Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    public void runAlarm() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new ClassPathResource("alarm.wav").getInputStream()));
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
