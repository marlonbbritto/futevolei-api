package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.MatchDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipSummaryDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamSummaryDto;
import com.futevolei.championship.futevolei_api.model.Match;
import com.futevolei.championship.futevolei_api.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public List<MatchDto> findAll(){
        List<Match> matches = matchRepository.findAll();
        return matches
                .stream()
                .map(this::convertEntityToMatch)
                .collect(Collectors.toList());
    }

    public MatchDto convertEntityToMatch(Match match){

        ChampionshipSummaryDto championshipSummaryDtoToConvert = new ChampionshipSummaryDto(
                match.getChampionship().getId(),
                match.getChampionship().getName()
        );

        TeamSummaryDto team1SummaryToConvert = null;
        if(match.getTeam1()!=null){
            team1SummaryToConvert = new TeamSummaryDto(
                    match.getTeam1().getId(),
                    match.getTeam1().getName()
            );
        }

        TeamSummaryDto team2SummaryToConvert = null;
        if(match.getTeam2()!=null){
            team2SummaryToConvert = new TeamSummaryDto(
                    match.getTeam2().getId(),
                    match.getTeam2().getName()
            );
        }

        TeamSummaryDto teamWinnerSummaryToConvert = null;
        if(match.getWinner()!=null){
            teamWinnerSummaryToConvert = new TeamSummaryDto(
                    match.getWinner().getId(),
                    match.getWinner().getName()
            );
        }

        TeamSummaryDto teamLoserSummaryToConvert = null;
        if(match.getLoser()!=null){
            teamLoserSummaryToConvert = new TeamSummaryDto(
                    match.getLoser().getId(),
                    match.getLoser().getName()
            );
        }

        MatchDto resultMatchDto = new MatchDto(
                match.getId(),
                match.getRound(),
                match.getKeyType(),
                match.getMatchStatus(),
                match.getScoreTeam1(),
                match.getScoreTeam2(),
                championshipSummaryDtoToConvert,
                team1SummaryToConvert,
                team2SummaryToConvert,
                teamWinnerSummaryToConvert,
                teamLoserSummaryToConvert
        );
        return resultMatchDto;
    }
}
