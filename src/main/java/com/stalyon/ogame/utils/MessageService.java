package com.stalyon.ogame.utils;

import com.stalyon.ogame.config.OgameProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private AlertService alertService;

    @Autowired
    private DiscordService discordService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MailService mailService;

    public void logInfo(String message, Boolean withAlert, Boolean withMail) {
        LOGGER.info(message);

        if (withAlert) {
            this.alertService.runAlarm();
        }

        if (withMail) {
            this.mailService.sendMail(message);
        }

        if (this.ogameProperties.DISCORD_BOT_ENABLE) {
            this.discordService.sendMessage(message);
        }
    }
}
