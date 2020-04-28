package com.stalyon.ogame;

import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.dto.*;
import com.stalyon.ogame.dto.reponse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OgameApiService {

    private static Logger LOGGER = LoggerFactory.getLogger(OgameApiService.class);

    @Autowired
    private OgameProperties ogameProperties;

    private RestTemplate restTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    public Boolean isUnderAttack() {
        ResponseEntity<IsUnderAttackResponseDto> response = this.restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/is-under-attack", IsUnderAttackResponseDto.class);

        return response.getBody().getResult();
    }

    public SlotsDto getSlots() {
        ResponseEntity<SlotsResponseDto> response = this.restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/fleets/slots", SlotsResponseDto.class);

        return response.getBody().getResult();
    }

    public GalaxyInfosDto getGalaxyInfos(Integer galaxy, Integer system) {
        ResponseEntity<GalaxyInfosResponseDto> response = this.restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/galaxy-infos/" + galaxy + "/" + system, GalaxyInfosResponseDto.class);

        return response.getBody().getResult();
    }

    public List<FleetDto> getFleets() {
        ResponseEntity<FleetsResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/fleets", FleetsResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetDto getPlanet(Integer galaxy, Integer system, Integer position) {
        String coordinate = "" + galaxy + "/" + system + "/" + position;
        ResponseEntity<PlanetsPlanetResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/planets/" + coordinate, PlanetsPlanetResponseDto.class);

        return response.getBody().getResult();
    }

    public List<PlanetDto> getPlanets() {
        ResponseEntity<PlanetsResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/planets", PlanetsResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetsResourcesBuildingsDto getPlanetsResourcesBuildings(PlanetDto planetDto) {
        ResponseEntity<PlanetsResourcesBuildingsResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/planets/" + planetDto.getId()
                        + "/resources-buildings", PlanetsResourcesBuildingsResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetsResourcesDto getPlanetsResources(PlanetDto planetDto) {
        ResponseEntity<PlanetsResourcesResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/planets/" + planetDto.getId()
                        + "/resources", PlanetsResourcesResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetsConstructionsDto getPlanetsConstructions(PlanetDto planetDto) {
        ResponseEntity<PlanetsConstructionsResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/planets/" + planetDto.getId()
                        + "/constructions", PlanetsConstructionsResponseDto.class);

        return response.getBody().getResult();
    }

    public FacilitiesDto getPlanetsFacilities(PlanetDto planetDto) {
        ResponseEntity<FacilitiesResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/planets/" + planetDto.getId()
                        + "/facilities", FacilitiesResponseDto.class);

        return response.getBody().getResult();
    }

    public UserInfosDto getUserInfos() {
        ResponseEntity<UserInfosResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/user-infos", UserInfosResponseDto.class);

        return response.getBody().getResult();
    }

    public EspionageReportDto getEspionageReport(Integer galaxy, Integer system, Integer position) throws HttpServerErrorException {
        ResponseEntity<EspionageReportResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/espionage-report/" + galaxy + "/" + system + "/" + position, EspionageReportResponseDto.class);

        return response.getBody().getResult();
    }

    public ShipsDto getShips(Integer planetId) {
        ResponseEntity<PlanetsShipsResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/planets/" + planetId + "/ships", PlanetsShipsResponseDto.class);

        return response.getBody().getResult();
    }

    public List<AttackDto> getAttacks() {
        ResponseEntity<AttacksResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/attacks", AttacksResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetsResourcesDto getPrice(Integer buildingId, Integer lvl) {
        ResponseEntity<PlanetsResourcesResponseDto> response = restTemplate.getForEntity(
                this.ogameProperties.BOT_URL + "/price/" + buildingId + "/" + lvl, PlanetsResourcesResponseDto.class);

        return response.getBody().getResult();
    }

    public void buildBuilding(Integer destinationId, Integer buildingId, MultiValueMap<String, String> formData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        try {
            restTemplate.postForEntity(this.ogameProperties.BOT_URL + "/planets/" + destinationId + "/build/building/" + buildingId,
                    request, SendFleetResponseDto.class);
        } catch (Exception e) {
            Pattern pattern = Pattern.compile("\"Message\":\"(.*)\",\"Result");
            Matcher matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                LOGGER.error("Erreur lors de la construction d'un bâtiment : " + matcher.group(1));
            } else {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void sendFleet(Integer destinationId, MultiValueMap<String, String> formData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        try {
            restTemplate.postForEntity(this.ogameProperties.BOT_URL + "/planets/" + destinationId + "/send-fleet",
                    request, SendFleetResponseDto.class);
        } catch (Exception e) {
            Pattern pattern = Pattern.compile("\"Message\":\"(.*)\",\"Result");
            Matcher matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                LOGGER.error("Erreur lors d'un envoi de flotte : " + matcher.group(1));
            } else {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
