package com.stalyon.ogame;

import com.stalyon.ogame.dto.*;
import com.stalyon.ogame.dto.reponse.*;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class OgameApiService {
    
    @Value("${bot.url}")
    private String BOT_ULR;

    private RestTemplate restTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    public Boolean isUnderAttack() {
        ResponseEntity<IsUnderAttackResponseDto> response = this.restTemplate.getForEntity(
                this.BOT_ULR + "/is-under-attack", IsUnderAttackResponseDto.class);

        return response.getBody().getResult();
    }

    public SlotsDto getSlots() {
        ResponseEntity<SlotsResponseDto> response = this.restTemplate.getForEntity(
                this.BOT_ULR + "/fleets/slots", SlotsResponseDto.class);

        return response.getBody().getResult();
    }

    public GalaxyInfosDto getGalaxyInfos(Integer galaxy, Integer system) {
        ResponseEntity<GalaxyInfosResponseDto> response = this.restTemplate.getForEntity(
                this.BOT_ULR + "/galaxy-infos/" + galaxy + "/" + system, GalaxyInfosResponseDto.class);

        return response.getBody().getResult();
    }

    public List<FleetDto> getFleets() {
        ResponseEntity<FleetsResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/fleets", FleetsResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetDto getPlanet(Integer galaxy, Integer system, Integer position) {
        String coordinate = "" + galaxy + "/" + system + "/" + position;
        ResponseEntity<PlanetsPlanetResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/planets/" + coordinate, PlanetsPlanetResponseDto.class);

        return response.getBody().getResult();
    }

    public List<PlanetDto> getPlanets() {
        ResponseEntity<PlanetsResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/planets", PlanetsResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetsResourcesBuildingsDto getPlanetsResourcesBuildings(PlanetDto planetDto) {
        ResponseEntity<PlanetsResourcesBuildingsResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/planets/" + planetDto.getId()
                        + "/resources-buildings", PlanetsResourcesBuildingsResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetsResourcesDto getPlanetsResources(PlanetDto planetDto) {
        ResponseEntity<PlanetsResourcesResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/planets/" + planetDto.getId()
                        + "/resources", PlanetsResourcesResponseDto.class);

        return response.getBody().getResult();
    }

    public PlanetsConstructionsDto getPlanetsConstructions(PlanetDto planetDto) {
        ResponseEntity<PlanetsConstructionsResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/planets/" + planetDto.getId()
                        + "/constructions", PlanetsConstructionsResponseDto.class);

        return response.getBody().getResult();
    }

    public UserInfosDto getUserInfos() {
        ResponseEntity<UserInfosResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/user-infos", UserInfosResponseDto.class);

        return response.getBody().getResult();
    }

    public EspionageReportDto getEspionageReport(Integer galaxy, Integer system, Integer position) throws HttpServerErrorException {
        ResponseEntity<EspionageReportResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/espionage-report/" + galaxy + "/" + system + "/" + position, EspionageReportResponseDto.class);

        return response.getBody().getResult();
    }

    public ShipsDto getShips(Integer planetId) {
        ResponseEntity<PlanetsShipsResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/planets/" + planetId + "/ships", PlanetsShipsResponseDto.class);

        return response.getBody().getResult();
    }

    public List<AttackDto> getAttacks() {
        ResponseEntity<AttacksResponseDto> response = restTemplate.getForEntity(
                this.BOT_ULR + "/attacks", AttacksResponseDto.class);

        return response.getBody().getResult();
    }

    public void buildBuilding(Integer destinationId, Integer buildingId, MultiValueMap<String, String> formData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        restTemplate.postForEntity(this.BOT_ULR + "/planets/" + destinationId + "/build/building/" + buildingId,
                request, SendFleetResponseDto.class);
    }

    public void sendFleet(Integer destinationId, MultiValueMap<String, String> formData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        restTemplate.postForEntity(this.BOT_ULR + "/planets/" + destinationId + "/send-fleet",
                request, SendFleetResponseDto.class);
    }
}
