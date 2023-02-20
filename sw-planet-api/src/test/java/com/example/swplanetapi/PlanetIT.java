package com.example.swplanetapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.swplanetapi.domain.Planet;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PlanetIT {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void createPlanet_ReturnsCreated() {
		ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);
		
		assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(sut.getBody().getId()).isNotNull();
		assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
		assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
		assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
	}
}
