package com.example.swplanetapi.domain;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class PlanetRepositoryTest {
	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@AfterEach
	public void afterEach() {
		PLANET.setId(null);
	}
	
	@Test
	public void createPlanet_WithValidData_ReturnsPlanet() {
		Planet planet = planetRepository.save(PLANET);
		
		Planet sut = testEntityManager.find(Planet.class, planet.getId());
		
		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(PLANET.getName());
		assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
		assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
	}
	
	@Test
	public void createPlanet_withInvalidData_throwsException() {
		Planet emptyPlanet = new Planet();
		Planet invalidPlanet = new Planet("","","");
		
		assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void createPlanet_WithExistingName_ThrowsException() {
		Planet planet = testEntityManager.persistFlushFind(PLANET);
		testEntityManager.detach(planet);
		planet.setId(null);
		
		assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void getPlanet_ByExistingId_ReturnsPlanet() {
		Planet planet = testEntityManager.persistFlushFind(PLANET);
		
		Optional<Planet> planetOpt = planetRepository.findById(planet.getId());
		
		assertThat(planetOpt).isNotEmpty();
		assertThat(planetOpt.get()).isEqualTo(planet);
	}
	
	@Test
	public void getPlanet_ByUnexistingId_ReturnsEmpty() {
		Optional<Planet> planetOpt = planetRepository.findById(1l);
		
		assertThat(planetOpt).isEmpty();
	}
	
	@Test
	public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
		Planet planet = testEntityManager.persistFlushFind(PLANET);
		
		Optional<Planet> planetOpt = planetRepository.findByName(planet.getName());
		
		assertThat(planetOpt).isNotEmpty();
		assertThat(planetOpt.get()).isEqualTo(planet);
	}
	
	@Test
	public void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception {
		Optional<Planet> planetOpt = planetRepository.findByName("name");
		
		assertThat(planetOpt).isEmpty();
	}
}


