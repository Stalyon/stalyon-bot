package com.stalyon.ogame.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AlertService {

    public void runAlarm() throws IOException {
        InputStream inputStream = new ClassPathResource("alarm.wav").getInputStream();
        AudioStream audioStream = new AudioStream(inputStream);
        AudioPlayer.player.start(audioStream);
    }
}
