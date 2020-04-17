package com.stalyon.ogame.utils;

import com.stalyon.ogame.config.OgameProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public class DiscordService extends ListenerAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(DiscordService.class);

    @Autowired
    private OgameProperties ogameProperties;

    private JDA discordAPI;

    @EventListener(ApplicationReadyEvent.class)
    public void initDiscordBot() {
        if (this.ogameProperties.DISCORD_BOT_ENABLE) {
            try {
                this.discordAPI = JDABuilder.createDefault(this.ogameProperties.DISCORD_BOT_TOKEN)
                        .addEventListeners(new DiscordService())
                        .build()
                        .awaitReady();

                this.sendMessage("Démarrage du bot");
            } catch (LoginException | InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (content.startsWith("!stalyon-bot")) {
            event.getChannel()
                    .sendMessage("Bot en cours de développement...")
                    .queue();
        }
    }

    public void sendMessage(String message) {
        if (this.ogameProperties.DISCORD_BOT_ENABLE && this.discordAPI != null) {
            try {
                this.discordAPI
                        .getGuildById(this.ogameProperties.DISCORD_BOT_GUILD)
                        .getTextChannelById(this.ogameProperties.DISCORD_BOT_CHANNEL)
                        .sendMessage(message)
                        .queue();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
