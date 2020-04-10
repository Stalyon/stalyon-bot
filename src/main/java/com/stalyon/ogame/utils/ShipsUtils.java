package com.stalyon.ogame.utils;

import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.ShipsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipsUtils {

    @Value("${expedition.vaisseau.petittransporteur}")
    private Integer SMALL_CARGO;

    @Value("${expedition.vaisseau.grostransporteur}")
    private Integer LARGE_CARGO;

    @Value("${expedition.vaisseau.chasseurleger}")
    private Integer LIGHT_FIGHTER;

    @Value("${expedition.vaisseau.chasseurlourd}")
    private Integer HEAVY_FIGHTER;

    @Value("${expedition.vaisseau.croiseur}")
    private Integer CRUISER;

    @Value("${expedition.vaisseau.vaisseaubataille}")
    private Integer BATTLESHIP;

    @Value("${expedition.vaisseau.sonde}")
    private Integer ESPIONAGE_PROBE;

    @Value("${expedition.vaisseau.bombardier}")
    private Integer BOMBER;

    @Value("${expedition.vaisseau.destructeur}")
    private Integer DESTROYER;

    @Value("${expedition.vaisseau.traqueur}")
    private Integer BATTLECRUISER;

    @Value("${expedition.vaisseau.faucheur}")
    private Integer REAPER;

    @Value("${expedition.vaisseau.eclaireur}")
    private Integer PATHFINDER;

    public List<String> prepareExpeditionShip() {
        List<String> toReturn = new ArrayList<>();

        if (this.SMALL_CARGO > 0) {
            toReturn.add(OgameCst.SMALL_CARGO_ID + "," + this.SMALL_CARGO);
        }

        if (this.LARGE_CARGO > 0) {
            toReturn.add(OgameCst.LARGE_CARGO_ID + "," + this.LARGE_CARGO);
        }

        if (this.LIGHT_FIGHTER > 0) {
            toReturn.add(OgameCst.LIGHT_FIGHTER_ID + "," + this.LIGHT_FIGHTER);
        }

        if (this.HEAVY_FIGHTER > 0) {
            toReturn.add(OgameCst.HEAVY_FIGHTER_ID + "," + this.HEAVY_FIGHTER);
        }

        if (this.CRUISER > 0) {
            toReturn.add(OgameCst.CRUISER_ID + "," + this.CRUISER);
        }

        if (this.BATTLESHIP > 0) {
            toReturn.add(OgameCst.BATTLESHIP_ID + "," + this.BATTLESHIP);
        }

        if (this.ESPIONAGE_PROBE > 0) {
            toReturn.add(OgameCst.ESPIONAGE_PROBE_ID + "," + this.ESPIONAGE_PROBE);
        }

        if (this.BOMBER > 0) {
            toReturn.add(OgameCst.BOMBER_ID+ "," + this.BOMBER);
        }

        if (this.DESTROYER > 0) {
            toReturn.add(OgameCst.DESTROYER_ID + "," + this.DESTROYER);
        }

        if (this.BATTLECRUISER > 0) {
            toReturn.add(OgameCst.BATTLECRUISER_ID + "," + this.BATTLECRUISER);
        }

        if (this.REAPER > 0) {
            toReturn.add(OgameCst.REAPER_ID+ "," + this.REAPER);
        }

        if (this.PATHFINDER > 0) {
            toReturn.add(OgameCst.PATHFINDER_ID + "," + this.PATHFINDER);
        }

        return toReturn;
    }

    public static List<String> allShips() {
        List<String> toReturn = new ArrayList<>();

        toReturn.add(OgameCst.LIGHT_FIGHTER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.HEAVY_FIGHTER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.CRUISER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.BATTLESHIP_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.BATTLECRUISER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.BOMBER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.DESTROYER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.DEATHSTAR_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.SMALL_CARGO_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.LARGE_CARGO_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.COLONY_SHIP_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.RECYCLER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.ESPIONAGE_PROBE_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.SOLAR_SATELLITE_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.CRAWLER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.REAPER_ID + "," + Integer.MAX_VALUE);
        toReturn.add(OgameCst.PATHFINDER_ID + "," + Integer.MAX_VALUE);

        return toReturn;
    }

    public static Integer countAttackShips(ShipsDto ships) {
        return ships.getLightFighter() + ships.getHeavyFighter() + ships.getCruiser() + ships.getBattleship()
                + ships.getBattlecruiser() + ships.getBomber() + ships.getDestroyer() + ships.getSmallCargo()
                + ships.getLargeCargo() + ships.getCrawler() + ships.getPathfinder() + ships.getReaper();
    }
}
