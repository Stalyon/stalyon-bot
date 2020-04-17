package com.stalyon.ogame.utils;

import com.stalyon.ogame.config.OgameProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OgameProperties ogameProperties;

    public void sendMail(String content) {
        if (this.ogameProperties.ALERT_MAIL) {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();

                msg.setTo(this.ogameProperties.ALERT_MAIL_TO);
                msg.setSubject("Ogame - Compte-rendu");
                msg.setText(content);

                javaMailSender.send(msg);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
