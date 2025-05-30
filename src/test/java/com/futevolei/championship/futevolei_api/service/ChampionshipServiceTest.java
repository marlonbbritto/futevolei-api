package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertTeamDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipUpdateDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
public class ChampionshipServiceTest {

    @Autowired
    private ChampionshipService championshipService;

    @MockitoBean
    private ChampionshipRepository championshipRepository;

    @MockitoBean
    private TeamRepository teamRepository;

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

    @Test
    @DisplayName("Should insert a new championship when everything is ok")
    void insert_ReturnsNewChampionshipDto(){

        ChampionshipInsertDto insertDto = new ChampionshipInsertDto(
                "Novo Campeonato de Futevôlei",
                LocalDate.of(2025,7,1),
                "Curitiba"
        );

        Championship savedChampionship = Championship.builder()
                .id(100L)
                .name(insertDto.name())
                .city(insertDto.city())
                .startDate(insertDto.startDate())
                .build();

        when(championshipRepository.save(any(Championship.class))).thenReturn(savedChampionship);

        Championship result = championshipService.insert(insertDto);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(insertDto.name(), result.getName());
        assertEquals(insertDto.city(),result.getCity());
        assertEquals(insertDto.startDate(),result.getStartDate());

        verify(championshipRepository).save(any(Championship.class));

    }

    @Test
    @DisplayName("Should delete a Championship when id exist")
    void delete_ExistingID_DeleteChampionshipSucessfully(){
        Long idToDelete = 1L;

        when(championshipRepository.existsById(idToDelete)).thenReturn(true);

        doNothing().when(championshipRepository).deleteById(idToDelete);

        assertDoesNotThrow(()-> championshipService.delete(idToDelete));

        verify(championshipRepository,times(1)).existsById(idToDelete);

        verify(championshipRepository, times(1)).deleteById(idToDelete);


    }

    @Test
    @DisplayName("Should insert an specific Team in an specific Championship when IDs exists and everything is correct")
    void insertTeamInChampionship_ReturnUpdatedChampionshipDto_withNumberOfTeamsUpdated(){

        Championship existingChampionshipInDb = Championship.builder()
                .id(100L)
                .name("Championship Test")
                .city("Maringá")
                .startDate(LocalDate.of(2025,11,11))
                .build();

        Player player1 = Player.builder()
                .id(1L)
                .name("João Silva")
                .build();

        Player player2 = Player.builder()
                .id(2L)
                .name("Carlos Souza")
                .build();

        Team team1 = Team.builder()
                .id(1L)
                .name("Time 1")
                .players(List.of(player1,player2))
                .build();

        Championship expectedUpdatedChampionshipInDb = Championship.builder()
                .id(100L)
                .name("Championship Test")
                .city("Maringá")
                .startDate(LocalDate.of(2025,11,11))
                .numberOfTeams(1)
                .teams(List.of(team1))
                .build();

        ChampionshipInsertTeamDto championshipInsertTeamDto = new ChampionshipInsertTeamDto(team1.getId());

        Long idOfChampionshipToFind = existingChampionshipInDb.getId();
        Long idOfTeamToFind = team1.getId();

        when(championshipRepository.findById(idOfChampionshipToFind)).thenReturn(Optional.of(existingChampionshipInDb));
        when(teamRepository.findById(idOfTeamToFind)).thenReturn(Optional.of(team1));

        ChampionshipDto resultDtoChampionshipUpdated = championshipService.insertTeamInChampionship(idOfChampionshipToFind,championshipInsertTeamDto);

        assertNotNull(resultDtoChampionshipUpdated);
        assertEquals(resultDtoChampionshipUpdated.id(),expectedUpdatedChampionshipInDb.getId());
        assertEquals(resultDtoChampionshipUpdated.name(),expectedUpdatedChampionshipInDb.getName());
        assertEquals(resultDtoChampionshipUpdated.startDate(),expectedUpdatedChampionshipInDb.getStartDate());
        assertEquals(resultDtoChampionshipUpdated.city(),expectedUpdatedChampionshipInDb.getCity());
        assertEquals(resultDtoChampionshipUpdated.numberOfTeams(),expectedUpdatedChampionshipInDb.getNumberOfTeams());

        verify(championshipRepository,times(1)).findById(idOfChampionshipToFind);
        verify(teamRepository,times(1)).findById(idOfTeamToFind);
        verify(teamRepository,times(1)).save(team1);
        verify(championshipRepository,times(1)).save(existingChampionshipInDb);



    }




}
