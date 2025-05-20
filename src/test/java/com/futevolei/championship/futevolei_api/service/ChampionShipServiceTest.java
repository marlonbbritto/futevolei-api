package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ChampionShipServiceTest {

    @Autowired
    private ChampionshipService championshipService;

    @MockitoBean
    private ChampionshipRepository championshipRepository;

    @Test
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
}
