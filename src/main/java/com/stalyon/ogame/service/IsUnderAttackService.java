package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.AttackDto;
import com.stalyon.ogame.dto.CoordinateDto;
import com.stalyon.ogame.dto.PlanetDto;
import com.stalyon.ogame.dto.PlanetsResourcesDto;
import com.stalyon.ogame.utils.MessageService;
import com.stalyon.ogame.utils.ShipsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Profile("!noIsUnderAttack")
public class IsUnderAttackService {

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MessageService messageService;

    @Scheduled(cron = "20 * * * * *") // every minute
    public void isUnderAttack() throws IOException {
        if (this.ogameApiService.isUnderAttack()) {

            List<AttackDto> attacks = this.ogameApiService.getAttacks();

            for (AttackDto attack : attacks) {
                if (ShipsUtils.countAttackShips(attack.getShips()) > this.ogameProperties.IS_UNDER_ATTACKENEMY_VAISSEAUX_MIN || attack.getShips().getDeathstar() > 0) {
                    this.messageService.logInfo("/!\\ /!\\ /!\\ Alerte générale ! (Attaque de " + attack.getAttackerName() + ") /!\\ /!\\ /!\\", Boolean.TRUE, Boolean.TRUE);

                    long diffSeconds = (attack.getArrivalTime().getTime() - new Date().getTime()) / 1000;
                    if (diffSeconds < 4 * 60) {
                        // Si l'attaque est dans moins de 4min, sauvetage de la flotte et des ressources
                        this.saveFleet(attack.getDestination());
                    }
                } else if (attack.getMissionType().equals(OgameCst.SPY)) {
                    this.messageService.logInfo("Espionnage de la part de " + attack.getAttackerName(), Boolean.FALSE, Boolean.FALSE);
                }
            }
        }
    }

    private void saveFleet(CoordinateDto destination) {
        PlanetDto planet = this.ogameApiService.getPlanet(destination.getGalaxy(), destination.getSystem(), destination.getPosition());
        Integer destinationId = destination.getType().equals(OgameCst.MOON_TYPE) ? planet.getMoon().getId() : planet.getId();

        PlanetsResourcesDto resources = this.ogameApiService
                .getPlanetsResources(new PlanetDto(destinationId, null, null, null, null));

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        ShipsUtils.allShips().forEach(ship -> formData.add("ships", ship));
        formData.add("mission", OgameCst.RECYCLE_DEBRIS_FIELD.toString());
        formData.add("speed", OgameCst.TWENTY_PERCENT.toString());
        formData.add("galaxy", destination.getGalaxy().toString());
        formData.add("system", destination.getSystem().toString());
        formData.add("position", destination.getPosition().toString());
        formData.add("type", OgameCst.RECYCLE_DEBRIS_FIELD.toString());
        formData.add("metal", resources.getMetal().toString());
        formData.add("crystal", resources.getCrystal().toString());
        formData.add("deuterium", resources.getDeuterium().toString());

        this.ogameApiService.sendFleet(destinationId, formData);
        this.messageService.logInfo("Sauvetage de la flotte : envoyée à 20% sur CDR en " + destination.getGalaxy() + ":"
                + destination.getSystem() + ":" + destination.getPosition(), Boolean.FALSE, Boolean.FALSE);
    }
}
