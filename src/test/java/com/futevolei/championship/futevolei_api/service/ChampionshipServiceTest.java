package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ChampionshipServiceTest {

    @Autowired
    private ChampionshipService championshipService;

    @MockitoBean
    private ChampionshipRepository championshipRepository;

    @Test
    @DisplayName("Should bring all registered championships when everything is correct.")
    void findAll_ReturnsListOfChampionshipDtos(){
        Championship championship1 = Championship.builder()
                .id(1L)
                .name("Teste 1")
                .city("Maringá")
                .startDate(LocalDate.now())
                .numberOfTeams(3)
                .build();
        Championship championship2 = Championship.builder()
                .id(2L)
                .name("Teste 2")
                .city("Londrina")
                .startDate(LocalDate.now().plusDays(7))
                .numberOfTeams(16)
                .build();
        List<Championship> championships = Arrays.asList(championship1,championship2);

        when(championshipRepository.findAll()).thenReturn(championships);

        List<ChampionshipDto> dtoList = championshipService.findAll();

        assertEquals(2,dtoList.size());
        assertEquals("Teste 1",dtoList.get(0).name());
        assertEquals("Teste 2",dtoList.get(1).name());
    }

    @Test
    @DisplayName("Should bring a specific championship when everything is right")
    void findById_ReturnsSpecificChampionshipDtos(){

        Championship championship1 = Championship.builder()
                .id(1L)
                .name("Teste 1")
                .city("Maringá")
                .startDate(LocalDate.now())
                .numberOfTeams(3)
                .build();
        Long idToFind = championship1.getId();

        when(championshipRepository.findById(idToFind)).thenReturn(Optional.of(championship1));

        ChampionshipDto resultDto = championshipService.findById(idToFind);

        assertNotNull(resultDto);

        assertEquals(championship1.getName(),resultDto.name());
        assertEquals(championship1.getCity(),resultDto.city());
        assertEquals(championship1.getStartDate(),resultDto.startDate());
        assertEquals(championship1.getNumberOfTeams(),resultDto.numberOfTeams());
    }
}
