package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @MockitoBean
    private  TeamRepository teamRepository;

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
}
