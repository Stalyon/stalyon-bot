package com.stalyon.ogame.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AlertService {

    @Value("${compte.alert.mail}")
    private Boolean ALERT_MAIL;

    @Value("${compte.alert.mail.to}")
    private String ALERT_MAIL_TO;

    private static Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void runAlarm() throws IOException {
        InputStream inputStream = new ClassPathResource("alarm.wav").getInputStream();
        AudioStream audioStream = new AudioStream(inputStream);
        AudioPlayer.player.start(audioStream);
    }

    public void sendMail(String content) {
        if (this.ALERT_MAIL) {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();

                msg.setTo(this.ALERT_MAIL_TO);
                msg.setSubject("Ogame - Compte-rendu");
                msg.setText(content);

                javaMailSender.send(msg);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
