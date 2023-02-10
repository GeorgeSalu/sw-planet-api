package com.example.swplanetapi.domain;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {

	@Autowired
	private PlanetService planetService;

	@MockBean
	private PlanetRepository planetRepository;
	
	@Test
	public void createPlanet_WithValidaData_ReturnsPlanet() {
		when(planetRepository.save(PLANET)).thenReturn(PLANET);
		
		Planet sut = planetService.create(PLANET);
		
		assertThat(sut).isEqualTo(PLANET);
	}
}
