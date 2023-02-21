package com.example.swplanetapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.example.swplanetapi.domain.Planet;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static com.example.swplanetapi.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class PlanetIT {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	public void createPlanet_ReturnsCreated() {
		ResponseEntity<Planet> sut = testRestTemplate.postForEntity("/planets", PLANET, Planet.class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(sut.getBody().getId()).isNotNull();
		assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
		assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
		assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
	}
	
	@Test
	public void getPlanets_returnsPlanets() {
		ResponseEntity<Planet> sut = testRestTemplate.getForEntity("/planets/1", Planet.class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).isEqualTo(TATOOINE);
	}
	
	@Test
	public void getPlanetsbyName_ReturnsPlanet() {
		ResponseEntity<Planet> sut = testRestTemplate.getForEntity("/planets/name/"+TATOOINE.getName(), Planet.class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).isEqualTo(TATOOINE);
	}
	
	@Test
	public void listPlantes_ReturnsAllPlanets() {
		ResponseEntity<Planet[]> sut = testRestTemplate.getForEntity("/planets", Planet[].class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).hasSize(3);
		assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
	}
	
	@Test
	public void listPlantes_ByClimate_ReturnsAllPlanets() {
		ResponseEntity<Planet[]> sut = testRestTemplate.getForEntity("/planets?climate="+TATOOINE.getClimate(), Planet[].class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()).hasSize(1);
		assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
	}
	
	@Test
	public void listPlantes_ByTerrain_ReturnsAllPlanets() {
		ResponseEntity<Planet[]> sut = testRestTemplate.getForEntity("/planets?terrains="+TATOOINE.getTerrain(), Planet[].class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
	}
}


