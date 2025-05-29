package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.MatchDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Match;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.KeyType;
import com.futevolei.championship.futevolei_api.model.enums.MatchStatus;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.MatchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @MockitoBean
    private MatchRepository matchRepository;

    @MockitoBean
    private ChampionshipRepository championshipRepository;

    @Test
    @DisplayName("Should bring all registered matches when everything is correct")
    void findAllMatches_ReturnListOfAllMatchesDto(){

        Championship championshipTest = Championship.builder()
                .name("Campeonato Teste")
                .city("Maringá")
                .startDate(LocalDate.of(2025,11,11))
                .numberOfTeams(2)
                .id(1L)
                .build();

        Player player1 = Player.builder()
                .id(1L)
                .name("Marlon Britto")
                .registrations(Registrations.PAID)
                .build();

        Player player2 = Player.builder()
                .id(2L)
                .name("Carlos Silva")
                .registrations(Registrations.PAID)
                .build();

        Player player3 = Player.builder()
                .id(3L)
                .name("Eduardo Santos")
                .registrations(Registrations.PAID)
                .build();

        Player player4 = Player.builder()
                .id(4L)
                .name("João Silva")
                .registrations(Registrations.PAID)
                .build();

        Player player5 = Player.builder()
                .id(5L)
                .name("Player 5")
                .registrations(Registrations.PAID)
                .build();

        Player player6 = Player.builder()
                .id(6L)
                .name("Player 6")
                .registrations(Registrations.PAID)
                .build();

        Player player7 = Player.builder()
                .id(7L)
                .name("Player 7")
                .registrations(Registrations.PAID)
                .build();

        Player player8 = Player.builder()
                .id(8L)
                .name("Player 8")
                .registrations(Registrations.PAID)
                .build();

        Team team1 = Team.builder()
                .id(1L)
                .name("Team 1")
                .build();

        Team team2 = Team.builder()
                .id(2L)
                .name("Team 2")
                .build();

        Team team3 = Team.builder()
                .id(3L)
                .name("Team 3")
                .build();

        Team team4 = Team.builder()
                .id(4L)
                .name("Team 4")
                .build();

        championshipTest.setTeams(List.of(team1,team2));
        team1.setChampionship(championshipTest);
        team2.setChampionship(championshipTest);
        team3.setChampionship(championshipTest);
        team4.setChampionship(championshipTest);
        team1.setPlayers(List.of(player1,player2));
        team2.setPlayers(List.of(player3,player4));
        team3.setPlayers(List.of(player5,player6));
        team4.setPlayers(List.of(player7,player8));

        Match match1 = Match.builder()
                .id(1L)
                .matchStatus(MatchStatus.IN_PROGRESS)
                .round(1)
                .team1(team1)
                .team2(team2)
                .championship(championshipTest)
                .scoreTeam1(18)
                .scoreTeam2(12)
                .winner(team1)
                .loser(team2)
                .keyType(KeyType.WINNERS)
                .build();

        Match match2 = Match.builder()
                .id(2L)
                .matchStatus(MatchStatus.IN_PROGRESS)
                .round(2)
                .team1(team3)
                .team2(team4)
                .championship(championshipTest)
                .scoreTeam1(18)
                .scoreTeam2(12)
                .winner(team3)
                .loser(team4 )
                .keyType(KeyType.WINNERS)
                .build();

        List<Match> matchesInDb = Arrays.asList(match1,match2);

        when(matchRepository.findAll()).thenReturn(matchesInDb);

        List<MatchDto> resultDtoList = matchService.findAll();

        assertNotNull(resultDtoList);
        assertEquals(matchesInDb.size(),resultDtoList.size());

        assertEquals(matchesInDb.get(0).getId(),resultDtoList.get(0).id());
        assertEquals(matchesInDb.get(0).getRound(),resultDtoList.get(0).round());
        assertEquals(matchesInDb.get(0).getKeyType(),resultDtoList.get(0).keyType());
        assertEquals(matchesInDb.get(0).getMatchStatus(),resultDtoList.get(0).matchStatus());
        assertEquals(matchesInDb.get(0).getScoreTeam1(),resultDtoList.get(0).scoreTeam1());
        assertEquals(matchesInDb.get(0).getScoreTeam2(),resultDtoList.get(0).scoreTeam2());
        assertEquals(matchesInDb.get(0).getChampionship().getId(),resultDtoList.get(0).championship().id());
        assertEquals(matchesInDb.get(0).getTeam1().getId(),resultDtoList.get(0).team1().id());
        assertEquals(matchesInDb.get(0).getTeam2().getId(),resultDtoList.get(0).team2().id());
        assertEquals(matchesInDb.get(0).getWinner().getId(),resultDtoList.get(0).winner().id());
        assertEquals(matchesInDb.get(0).getLoser().getId(),resultDtoList.get(0).loser().id());

        assertEquals(matchesInDb.get(1).getId(),resultDtoList.get(1).id());
        assertEquals(matchesInDb.get(1).getRound(),resultDtoList.get(1).round());
        assertEquals(matchesInDb.get(1).getKeyType(),resultDtoList.get(1).keyType());
        assertEquals(matchesInDb.get(1).getMatchStatus(),resultDtoList.get(1).matchStatus());
        assertEquals(matchesInDb.get(1).getScoreTeam1(),resultDtoList.get(1).scoreTeam1());
        assertEquals(matchesInDb.get(1).getScoreTeam2(),resultDtoList.get(1).scoreTeam2());
        assertEquals(matchesInDb.get(1).getChampionship().getId(),resultDtoList.get(1).championship().id());
        assertEquals(matchesInDb.get(1).getTeam1().getId(),resultDtoList.get(1).team1().id());
        assertEquals(matchesInDb.get(1).getTeam2().getId(),resultDtoList.get(1).team2().id());
        assertEquals(matchesInDb.get(1).getWinner().getId(),resultDtoList.get(1).winner().id());
        assertEquals(matchesInDb.get(1).getLoser().getId(),resultDtoList.get(1).loser().id());

        verify(matchRepository,times(1)).findAll();

    }

}
