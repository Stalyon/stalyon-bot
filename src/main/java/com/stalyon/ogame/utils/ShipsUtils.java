package com.stalyon.ogame.utils;

import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.ShipsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipsUtils {

    @Autowired
    private OgameProperties ogameProperties;

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

    public List<String> prepareExpeditionShip() {
        List<String> toReturn = new ArrayList<>();

        if (this.ogameProperties.EXPEDITION_SMALL_CARGO > 0) {
            toReturn.add(OgameCst.SMALL_CARGO_ID + "," + this.ogameProperties.EXPEDITION_SMALL_CARGO);
        }

        if (this.ogameProperties.EXPEDITION_LARGE_CARGO > 0) {
            toReturn.add(OgameCst.LARGE_CARGO_ID + "," + this.ogameProperties.EXPEDITION_LARGE_CARGO);
        }

        if (this.ogameProperties.EXPEDITION_LIGHT_FIGHTER > 0) {
            toReturn.add(OgameCst.LIGHT_FIGHTER_ID + "," + this.ogameProperties.EXPEDITION_LIGHT_FIGHTER);
        }

        if (this.ogameProperties.EXPEDITION_HEAVY_FIGHTER > 0) {
            toReturn.add(OgameCst.HEAVY_FIGHTER_ID + "," + this.ogameProperties.EXPEDITION_HEAVY_FIGHTER);
        }

        if (this.ogameProperties.EXPEDITION_CRUISER > 0) {
            toReturn.add(OgameCst.CRUISER_ID + "," + this.ogameProperties.EXPEDITION_CRUISER);
        }

        if (this.ogameProperties.EXPEDITION_BATTLESHIP > 0) {
            toReturn.add(OgameCst.BATTLESHIP_ID + "," + this.ogameProperties.EXPEDITION_BATTLESHIP);
        }

        if (this.ogameProperties.EXPEDITION_ESPIONAGE_PROBE > 0) {
            toReturn.add(OgameCst.ESPIONAGE_PROBE_ID + "," + this.ogameProperties.EXPEDITION_ESPIONAGE_PROBE);
        }

        if (this.ogameProperties.EXPEDITION_BOMBER > 0) {
            toReturn.add(OgameCst.BOMBER_ID + "," + this.ogameProperties.EXPEDITION_BOMBER);
        }

        if (this.ogameProperties.EXPEDITION_DESTROYER > 0) {
            toReturn.add(OgameCst.DESTROYER_ID + "," + this.ogameProperties.EXPEDITION_DESTROYER);
        }

        if (this.ogameProperties.EXPEDITION_BATTLECRUISER > 0) {
            toReturn.add(OgameCst.BATTLECRUISER_ID + "," + this.ogameProperties.EXPEDITION_BATTLECRUISER);
        }

        if (this.ogameProperties.EXPEDITION_REAPER > 0) {
            toReturn.add(OgameCst.REAPER_ID + "," + this.ogameProperties.EXPEDITION_REAPER);
        }

        if (this.ogameProperties.EXPEDITION_PATHFINDER > 0) {
            toReturn.add(OgameCst.PATHFINDER_ID + "," + this.ogameProperties.EXPEDITION_PATHFINDER);
        }

        return toReturn;
    }
}
