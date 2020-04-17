package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.utils.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Profile("saveFleet")
public class SaveFleetService {

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private MessageService messageService;

    @Scheduled(cron = "30 14 13 * * *")
    public void saveFleet() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("ships", OgameCst.LARGE_CARGO_ID + "," + "2");
        formData.add("mission", OgameCst.TRANSPORT.toString());
        formData.add("speed", OgameCst.TEN_PERCENT.toString());
        formData.add("galaxy", "3");
        formData.add("system", "310");
        formData.add("position", "11");
        formData.add("metal", "0");
        formData.add("crystal", "0");
        formData.add("deuterium", "0");
        this.ogameApiService.sendFleet(33630004, formData);

        this.messageService.logInfo("Flotte envoyée", Boolean.FALSE, Boolean.FALSE);
    }
}
