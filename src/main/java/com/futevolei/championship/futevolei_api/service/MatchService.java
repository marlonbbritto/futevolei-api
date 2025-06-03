package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.MatchDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipSummaryDto;
import com.futevolei.championship.futevolei_api.dto.match.MatchUpdateDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamSummaryDto;
import com.futevolei.championship.futevolei_api.exception.BusinessException;
import com.futevolei.championship.futevolei_api.exception.ResourceNotFoundException;
import com.futevolei.championship.futevolei_api.model.Match;
import com.futevolei.championship.futevolei_api.model.enums.MatchStatus;
import com.futevolei.championship.futevolei_api.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    public MatchDto findById(Long id){
        Optional<Match> match = matchRepository.findById(id);
        return match
                .map(this::convertEntityToMatch)
                .orElseThrow(()->new ResourceNotFoundException("Match","ID",id));
    }

    @Transactional
    public MatchDto updateMatch(Long idMatch, MatchUpdateDto matchUpdateDto){
        Match matchInDb = matchRepository.findById(idMatch)
                .orElseThrow(()->new ResourceNotFoundException("Match","ID",idMatch));

        if(matchUpdateDto.matchStatus()!=null){
            matchInDb.setMatchStatus(matchUpdateDto.matchStatus());
        }

        if(matchUpdateDto.scoreTeam1()!=null){
            matchInDb.setScoreTeam1(matchUpdateDto.scoreTeam1());
        }

        if(matchUpdateDto.scoreTeam2()!=null){
            matchInDb.setScoreTeam2(matchUpdateDto.scoreTeam2());
        }

        if(matchInDb.getMatchStatus()== MatchStatus.COMPLETED){
            matchInDb=defineLoserAndWinnerOfMatch(matchInDb);
        }

        matchRepository.save(matchInDb);

        return convertEntityToMatch(matchInDb);
    }

    private Match defineLoserAndWinnerOfMatch (Match match){
        if(!(match.getScoreTeam1()>=0)||!(match.getScoreTeam2()>=0)){
            throw new BusinessException("Para uma partida ser concluída, os placares de ambos os times devem ser informados. Partida ID "+ match.getId());
        }
        if (match.getScoreTeam1()==match.getScoreTeam2()){
            throw new BusinessException("Uma partida de futevolei não pode ser concluída em empate. ID partida: " + match.getId());
        }
        if(match.getScoreTeam1()>match.getScoreTeam2()){
            match.setWinner(match.getTeam1());
            match.setLoser(match.getTeam2());
        }
        if(match.getScoreTeam1()<match.getScoreTeam2()){
            match.setWinner(match.getTeam2());
            match.setLoser(match.getTeam1());
        }
        return match;
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
