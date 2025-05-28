package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamInsertDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @MockitoBean
    private  TeamRepository teamRepository;

    @MockitoBean
    private ChampionshipRepository championshipRepository;

    @Test
    @DisplayName("Should bring all registered teams when everythin is correct")
    void findAll_ReturnListOfTeamsDto(){
        Player player1 = Player.builder()
                .name("João Silva")
                .build();
        Player player2 = Player.builder()
                .name("Carlos Souza")
                .build();
        Player player3 = Player.builder()
                .name("Eduardo Santos")
                .build();
        Player player4 = Player.builder()
                .name("Rafael Barbosa")
                .build();
        Championship championship = Championship.builder()
                .name("Campeonato 1")
                .numberOfTeams(10)
                .startDate(LocalDate.of(2025,11,11))
                .city("Maringá")
                .build();

        Team team1 = Team.builder()
                .name("Time 1")
                .championship(championship)
                .players(List.of(player1,player2))
                .build();
        Team team2 = Team.builder()
                .name("Time 2")
                .championship(championship)
                .players(List.of(player3,player4))
                .build();

        List<Team> teamList = Arrays.asList(team1,team2);

        when(teamRepository.findAll()).thenReturn(teamList);

        List<TeamDto> resultList = teamService.findAll();

        assertEquals(teamList.size(),resultList.size());
        assertEquals(teamList.get(0).getName(),resultList.get(0).name());
        assertEquals(teamList.get(0).getId(),resultList.get(0).id());
        assertEquals(teamList.get(0).getChampionship().getId(),resultList.get(0).championship().id());
        assertEquals(teamList.get(0).getPlayers().size(),resultList.get(0).players().size());
        assertEquals(teamList.get(1).getName(),resultList.get(1).name());
        assertEquals(teamList.get(1).getId(),resultList.get(1).id());
        assertEquals(teamList.get(1).getChampionship().getId(),resultList.get(1).championship().id());
        assertEquals(teamList.get(1).getPlayers().size(),resultList.get(1).players().size());


    }

    @Test
    @DisplayName("Should bring an specifi Team when ID exist and everything is ok")
    void findById_ReturnSpecifTeamDto(){
        Player player1 = Player.builder()
                .id(2L)
                .name("João Silva")
                .build();
        Player player2 = Player.builder()
                .id(3L)
                .name("Carlos Souza")
                .build();
        Championship championship = Championship.builder()
                .id(3L)
                .name("Campeonato 1")
                .numberOfTeams(10)
                .startDate(LocalDate.of(2025,11,11))
                .city("Maringá")
                .build();
        Team team1 = Team.builder()
                .id(1L)
                .name("Time 1")
                .championship(championship)
                .players(List.of(player1,player2))
                .build();
        Long idToFind = team1.getId();

        when(teamRepository.findById(idToFind)).thenReturn(Optional.of(team1));

        TeamDto resultDto = teamService.findById(idToFind);

        assertNotNull(resultDto);
        assertEquals(team1.getId(),resultDto.id());
        assertEquals(team1.getName(),resultDto.name());
        assertEquals(team1.getChampionship().getId(),resultDto.championship().id());
        assertEquals(team1.getPlayers().size(),resultDto.players().size());
        assertEquals(team1.getPlayers().get(0).getId(),resultDto.players().get(0).id());
        assertEquals(team1.getPlayers().get(0).getName(),resultDto.players().get(0).name());
        assertEquals(team1.getPlayers().get(0).getRegistrations(),resultDto.players().get(0).registrations());
        assertEquals(team1.getPlayers().get(1).getId(),resultDto.players().get(1).id());
        assertEquals(team1.getPlayers().get(1).getName(),resultDto.players().get(1).name());
        assertEquals(team1.getPlayers().get(1).getRegistrations(),resultDto.players().get(1).registrations());

    }

    @Test
    @DisplayName("Should inser a new team when everything is ok")
    void insert_ReturnNewTeamDTO(){
        Championship championship = Championship.builder()
                .id(1L)
                .name("Campeonato 1")
                .numberOfTeams(10)
                .startDate(LocalDate.of(2025,11,11))
                .city("Maringá")
                .build();

        TeamInsertDto insertDto = new TeamInsertDto(
                "O melhor time",
                1L
        );

        Team team = Team.builder()
                .id(1L)
                .name("O melhor time")
                .championship(championship)
                .build();

        when(championshipRepository.findById(1L)).thenReturn(Optional.of(championship));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        TeamDto result = teamService.insert(insertDto);

        assertNotNull(result);
        assertEquals(result.id(),team.getId());
        assertEquals(result.players().size(),team.getPlayers().size());
        assertEquals(result.name(),team.getName());
        assertEquals(result.championship().id(),team.getChampionship().getId());
        assertEquals(result.championship().name(),team.getChampionship().getName());
        assertEquals(result.championship().numberOfTeams(),team.getChampionship().getNumberOfTeams());
        assertEquals(result.championship().city(),team.getChampionship().getCity());
        assertEquals(result.championship().startDate(),team.getChampionship().getStartDate());

        verify(championshipRepository,times(1)).findById(insertDto.championshipId());
        verify(teamRepository, times(1)).save(any(Team.class));

    }

    @Test
    @DisplayName("Should delete an specific team when Id exist and everythin ok")
    void delete_VoidDeleteAnTeam(){
        Championship championship = Championship.builder()
                .id(1L)
                .name("Campeonato 1")
                .numberOfTeams(10)
                .startDate(LocalDate.of(2025,11,11))
                .city("Maringá")
                .build();
        Team teamToDelete = Team.builder()
                .id(1L)
                .name("O time para deletar")
                .championship(championship)
                .build();
        Long idToDelete = teamToDelete.getId();
        when(teamRepository.findById(idToDelete)).thenReturn(Optional.of(teamToDelete));
        doNothing().when(teamRepository).delete(teamToDelete);

        assertDoesNotThrow(()->teamService.delete(idToDelete));

        verify(teamRepository,times(1)).findById(idToDelete);
        verify(teamRepository,times(1)).delete(teamToDelete);


    }
}
