package com.stalyon.ogame.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OgameProperties {

    // Ogamed
    @Value("${bot.url}")
    public String BOT_URL;

    // Infos
    @Value("${infos.planets.id.enable}")
    public Boolean INFOS_PLANETS_ID_ENABLE;

    @Value("${infos.compte.enable}")
    public Boolean INFOS_COMPTE_ENABLE;

    // Attaques automatiques
    @Value("${attaques.auto.coord.galaxy}")
    public List<Integer> ATTAQUES_AUTO_COORD_GALAXY;

    @Value("${attaques.auto.coord.system.min}")
    public List<Integer> ATTAQUES_AUTO_COORD_SYSTEM_MIN;

    @Value("${attaques.auto.coord.system.max}")
    public List<Integer> ATTAQUES_AUTO_COORD_SYSTEM_MAX;

    @Value("${attaques.auto.enable}")
    public Boolean ATTAQUES_AUTO_ENABLE;

    @Value("${attaques.auto.planet}")
    public List<Integer> ATTAQUES_AUTO_AUTO_PLANET_ID;

    @Value("${attaques.auto.planet.system}")
    public List<Integer> ATTAQUES_AUTO_PLANET_SYSTEM;

    @Value("${attaques.auto.cargo.stockage}")
    public Integer ATTAQUES_AUTO_CARGO_STOCKAGE;

    @Value("${attaques.auto.cargo.id}")
    public Integer ATTAQUES_AUTO_CARGO_ID;

    @Value("${attaques.auto.minimal.resources}")
    public Integer ATTAQUES_AUTO_MINIMAL_RESOURCES;

    @Value("${attaques.auto.spy.sondes.nb}")
    public Integer ATTAQUES_AUTO_NB_SONDES;

    // Vol de CDR d'expédition
    @Value("${expedebristheft.galaxy}")
    public List<Integer> EXPE_THEFT_COORD_GALAXY;

    @Value("${expedebristheft.system.min}")
    public List<Integer> EXPE_THEFT_COORD_SYSTEM_MIN;

    @Value("${expedebristheft.system.max}")
    public List<Integer> EXPE_THEFT_COORD_SYSTEM_MAX;

    @Value("${expedebristheft.planet}")
    public List<Integer> EXPE_THEFT_PLANET_ID;

    @Value("${expedebristheft.planet.system}")
    public List<Integer> EXPE_THEFT_PLANET_SYSTEM;

    @Value("${expedebristheft.pathfinder.min}")
    public Integer EXPE_THEFT_PATHFINDER_MIN;

    @Value("${expedebristheft.auto}")
    public Boolean EXPE_THEFT_AUTO;

    // Expéditions
    @Value("${expedition.enable}")
    public Boolean EXPEDITION_ENABLE;

    @Value("${expedition.planet}")
    public Integer EXPEDITION_PLANET;

    @Value("${expedition.galaxy}")
    public Integer EXPEDITION_GALAXY;

    @Value("${expedition.system}")
    public Integer EXPEDITION_SYSTEM;

    @Value("${expedition.vaisseau.petittransporteur}")
    public Integer EXPEDITION_SMALL_CARGO;

    @Value("${expedition.vaisseau.grostransporteur}")
    public Integer EXPEDITION_LARGE_CARGO;

    @Value("${expedition.vaisseau.chasseurleger}")
    public Integer EXPEDITION_LIGHT_FIGHTER;

    @Value("${expedition.vaisseau.chasseurlourd}")
    public Integer EXPEDITION_HEAVY_FIGHTER;

    @Value("${expedition.vaisseau.croiseur}")
    public Integer EXPEDITION_CRUISER;

    @Value("${expedition.vaisseau.vaisseaubataille}")
    public Integer EXPEDITION_BATTLESHIP;

    @Value("${expedition.vaisseau.sonde}")
    public Integer EXPEDITION_ESPIONAGE_PROBE;

    @Value("${expedition.vaisseau.bombardier}")
    public Integer EXPEDITION_BOMBER;

    @Value("${expedition.vaisseau.destructeur}")
    public Integer EXPEDITION_DESTROYER;

    @Value("${expedition.vaisseau.traqueur}")
    public Integer EXPEDITION_BATTLECRUISER;

    @Value("${expedition.vaisseau.faucheur}")
    public Integer EXPEDITION_REAPER;

    @Value("${expedition.vaisseau.eclaireur}")
    public Integer EXPEDITION_PATHFINDER;

    // Is under attack
    @Value("${isunderattack.enemy.vaisseaux.min}")
    public Integer IS_UNDER_ATTACK_ENEMY_VAISSEAUX_MIN;

    @Value("${isunderattack.escape.park.coord.galaxy}")
    public List<Integer> IS_UNDER_ATTACK_ESCAPE_COORD_GALAXY;

    @Value("${isunderattack.escape.park.coord.system}")
    public List<Integer> IS_UNDER_ATTACK_ESCAPE_COORD_SYSTEM;

    @Value("${isunderattack.escape.park.coord.position}")
    public List<Integer> IS_UNDER_ATTACK_ESCAPE_COORD_POSITION;

    @Value("${isunderattack.escape.park.coord.moon}")
    public List<Boolean> IS_UNDER_ATTACK_ESCAPE_COORD_MOON;

    // Mines
    @Value("${mines.auto.build.enable}")
    public Boolean MINES_AUTO_BUILD_ENABLE;

    @Value("${mines.auto.build.transport}")
    public Boolean MINES_AUTO_BUILD_TRANSPORT;

    @Value("${mines.auto.build.planets.excluded}")
    public List<Integer> MINES_EXCLUDED_PLANETS;

    @Value("${mines.auto.build.planets.clusterized}")
    public List<Integer> MINES_CLUSTERIZED_PLANETS;

    @Value("${mines.auto.max.minemetal}")
    public Integer MINES_MINE_METAL_MAX;

    @Value("${mines.auto.max.minecristal}")
    public Integer MINES_MINE_CRISTAL_MAX;

    @Value("${mines.auto.max.synthedeut}")
    public Integer MINES_SYNTHE_DEUT_MAX;

    @Value("${mines.auto.max.solarplant}")
    public Integer MINES_SOLAR_PLANT_MAX;

    @Value("${mines.auto.max.reactorfusion}")
    public Integer MINES_REACTOR_FUSION_MAX;

    @Value(("${mines.auto.build.transport.stockage}"))
    public Integer MINES_TRANSPORT_STOCKAGE;

    // Facilities
    @Value("${buildings.facilities.robots.min}")
    public Integer BUILDINGS_FACILITIES_ROBOTS_MIN;

    @Value("${buildings.facilities.nanite.min}")
    public Integer BUILDINGS_FACILITIES_NANITE_MIN;

    @Value("${buildings.facilities.hangar.metal.min}")
    public Integer BUILDINGS_FACILITIES_HANGAR_METAL_MIN;

    @Value("${buildings.facilities.hangar.cristal.min}")
    public Integer BUILDINGS_FACILITIES_HANGAR_CRISTAL_MIN;

    @Value("${buildings.facilities.hangar.deut.min}")
    public Integer BUILDINGS_FACILITIES_HANGAR_DEUT_MIN;

    @Value("${buildings.facilities.labo.min}")
    public Integer BUILDINGS_FACILITIES_LABO_MIN;

    @Value("${buildings.facilities.chantierspatial.min}")
    public Integer BUILDINGS_FACILITIES_CHANTIER_SPATIAL_MIN;

    // Compte
    @Value("${compte.geologue}")
    public Boolean HAS_GEOLOGUE;

    @Value("${compte.server.speed}")
    public Integer SERVER_SPEED;

    @Value("${compte.tech.energy}")
    public Integer ENERGY_TECH;

    @Value("${compte.alert.mail}")
    public Boolean ALERT_MAIL;

    @Value("${compte.alert.mail.to}")
    public String ALERT_MAIL_TO;

    // Espionnage
    @Value("${spy.enable}")
    public Boolean SPY_ENABLE;

    @Value("${spy.coord.galaxy}")
    public Integer SPY_COORD_GALAXY;

    @Value("${spy.coord.system.min}")
    public Integer SPY_COORD_SYSTEM_MIN;

    @Value("${spy.coord.system.max}")
    public Integer SPY_COORD_SYSTEM_MAX;

    @Value("${spy.planet}")
    public Integer SPY_PLANET_ID;

    @Value("${spy.sondes.nb}")
    public Integer SPY_NB_SONDES;

    @Value("${spy.filter.player.rank.min}")
    public Integer SPY_PLAYER_RANK_MIN;

    @Value("${spy.filter.planet}")
    public Boolean SPY_FILTER_PLANET;

    @Value("${spy.filter.moon}")
    public Boolean SPY_FILTER_MOON;

    @Value("${spy.filter.activity.last.min}")
    public Integer SPY_LAST_ACTIVITY_MIN;

    @Value("${spy.filter.allys.excluded}")
    public List<Integer> SPY_ALLYS_EXCLUDED;

    @Value("${spy.filter.players.excluded}")
    public List<Integer> SPY_PLAYERS_EXCLUDED;

    // Discord
    @Value("${discord.bot.enable}")
    public Boolean DISCORD_BOT_ENABLE;

    @Value("${discord.bot.token}")
    public String DISCORD_BOT_TOKEN;

    @Value("${discord.bot.channel.id}")
    public String DISCORD_BOT_CHANNEL;

    @Value("${discord.bot.guild.id}")
    public String DISCORD_BOT_GUILD;

    // Recherches
    @Value("${recherches.build.auto}")
    public Boolean RECHERCHES_BUILD_AUTO;

    @Value("${recherches.build.planet}")
    public Integer RECHERCHES_BUILD_PLANET_ID;

    @Value("${recherches.build.queue.techno.id}")
    public List<Integer> RECHERCHES_BUILD_QUEUE_TECHNOS_ID;

    @Value("${recherches.build.queue.techno.lvl}")
    public List<Integer> RECHERCHES_BUILD_QUEUE_TECHNOS_LVL;

    @Value("${recherches.transport.auto}")
    public Boolean RECHERCHES_TRANSPORT_AUTO;

    @Value("${recherches.transport.planets.clusterized}")
    public List<Integer> RECHERCHES_TRANSPORT_PLANETS_CLUSTERIZED;

    @Value("${recherches.transport.stockage}")
    public Integer RECHERCHES_TRANSPORT_STOCKAGE;
}
