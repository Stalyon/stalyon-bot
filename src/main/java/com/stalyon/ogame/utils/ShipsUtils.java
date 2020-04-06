package com.stalyon.ogame.utils;

import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.ShipsDto;

import java.util.ArrayList;
import java.util.List;

public class ShipsUtils {

    private ShipsUtils() {
        // Do nothing
    }

    public static List<String> prepareExpeditionShip(ShipsDto ships) {
        List<String> toReturn = new ArrayList<>();

        if (ships.getSmallCargo() > 0) {
            toReturn.add(OgameCst.SMALL_CARGO_ID + "," + ships.getSmallCargo());
        }

        if (ships.getLargeCargo() > 0) {
            toReturn.add(OgameCst.LARGE_CARGO_ID + "," + ships.getLargeCargo());
        }

        if (ships.getPathfinder() > 0) {
            toReturn.add(OgameCst.PATHFINDER_ID + "," + ships.getPathfinder());
        }

        toReturn.add(OgameCst.REAPER_ID + "," + 1);

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
