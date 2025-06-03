package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipIdTeamDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipUpdateDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamSummaryDto;
import com.futevolei.championship.futevolei_api.exception.BusinessException;
import com.futevolei.championship.futevolei_api.exception.ResourceNotFoundException;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Match;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChampionshipService {
    @Autowired
    ChampionshipRepository championshipRepository;

    @Autowired
    TeamRepository teamRepository;

    public List<ChampionshipDto> findAll(){
        List<Championship> championships = championshipRepository.findAll();
        return championships
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

    }

    public ChampionshipDto findById (Long id){
            Optional<Championship> championship = championshipRepository.findById(id);
            return championship
                    .map(this::convertEntityToDto)
                    .orElseThrow(()-> new ResourceNotFoundException("Championship","ID", id));

    }

    @Transactional
    public Championship insert(ChampionshipInsertDto championship){
        return championshipRepository.save(convertDtoToEntity(championship));
    }

    @Transactional
    public void delete(Long id){
        if(!championshipRepository.existsById(id)){
            throw new ResourceNotFoundException("Championship","ID", id);
        }
        championshipRepository.deleteById(id);
    }

    @Transactional
    public ChampionshipDto partialUpdateChampionship(Long id, ChampionshipUpdateDto championshipUpdateDto){
        Optional<Championship> championshipOptional = championshipRepository.findById(id);

        return championshipOptional.map(existingChampionship->{
            if(championshipUpdateDto.name()!=null){
                existingChampionship.setName(championshipUpdateDto.name());
            }
            if(championshipUpdateDto.startDate()!=null){
                existingChampionship.setStartDate(championshipUpdateDto.startDate());
            }
            if(championshipUpdateDto.city()!=null){
                existingChampionship.setCity(championshipUpdateDto.city());
            }
            Championship updatedChampionship = championshipRepository.save(existingChampionship);
            return convertEntityToDto(updatedChampionship);
        }).orElseThrow(()-> new  ResourceNotFoundException("Championship","ID", id));

    }

    @Transactional
    public ChampionshipDto insertTeamInChampionship (Long idChampionship, ChampionshipIdTeamDto championshipIdTeamDto){
        Championship championshipDbToInsertTeam = championshipRepository
                .findById(idChampionship)
                .orElseThrow(()->new ResourceNotFoundException("Championship","ID",idChampionship));

        Team teamToInsertInChampionship = teamRepository
                .findById(championshipIdTeamDto.id())
                .orElseThrow(()-> new ResourceNotFoundException("Team","ID", championshipIdTeamDto.id()));

        if(championshipDbToInsertTeam.getTeams().contains(teamToInsertInChampionship)){
            throw new BusinessException("O time: "+teamToInsertInChampionship.getName()+ "já esta no campeonato");
        }

        championshipDbToInsertTeam.getTeams().add(teamToInsertInChampionship);

        updateNumberOfTeamsInChampionship(championshipDbToInsertTeam);

        teamToInsertInChampionship.setChampionship(championshipDbToInsertTeam);

        teamRepository.save(teamToInsertInChampionship);

        championshipRepository.save(championshipDbToInsertTeam);

        return convertEntityToDto(championshipDbToInsertTeam);
    }

    @Transactional
    public ChampionshipDto removeTeamInChampionship(Long idChampionship, Long idTeam){
        Championship championshipDbToRemove = championshipRepository.findById(idChampionship)
                .orElseThrow(()->new ResourceNotFoundException("Championship", "ID",idChampionship));

        Team teamDbToRemove = teamRepository.findById(idTeam)
                .orElseThrow(()->new ResourceNotFoundException("Team","ID", idTeam));

        championshipDbToRemove.getTeams().remove(teamDbToRemove);

        updateNumberOfTeamsInChampionship(championshipDbToRemove);

        for(Player p:teamDbToRemove.getPlayers()){
            p.setTeam(null);
        }

        championshipRepository.save(championshipDbToRemove);

        return convertEntityToDto(championshipDbToRemove);

    }

    /*private List<Match> makeMatchesForChampionship(Long idChampionship){
        Championship championship = championshipRepository.findById(idChampionship)
                .orElseThrow(()->new ResourceNotFoundException("Championship", "ID", idChampionship));

        updateNumberOfTeamsInChampionship(championship); // guarantee of updating the number of teams in the championship

        if (championship.getNumberOfTeams()<8){
            throw new BusinessException("Campeonatos de dupla eliminatoria faz sentido quando existem 8 ou mais duplas e seu campeonato tem: " + championship.getTeams().size() + " duplas");
        }

        List<Integer> numberOfMatchesInEachPhaseOfWinnersBracket = numberOfMatchesInWinnersBracket(championship.getNumberOfTeams());






    }*/

    private List<Integer> numberOfMatchesInWinnersBracket (Integer numberOfTeamsInChampionship) {

        List<Integer> numberOfMatchesInEachPhaseOfWinnersBracket = new ArrayList<>();

        Double exponentPotenceMatchCalculation = Math.ceil(Math.log(numberOfTeamsInChampionship) / Math.log(2));

        Integer potenceMatchCalculation = (int) Math.pow(2, exponentPotenceMatchCalculation);

        Integer numberOfMatchesFirstRoundInWinnerBracket = (numberOfTeamsInChampionship * 2 - potenceMatchCalculation) / 2;

        Integer numberOfMatcheSecondRoundWinnerBracket = (numberOfMatchesFirstRoundInWinnerBracket +
                (potenceMatchCalculation - numberOfTeamsInChampionship))/2;

        numberOfMatchesInEachPhaseOfWinnersBracket.add(numberOfMatchesFirstRoundInWinnerBracket);
        numberOfMatchesInEachPhaseOfWinnersBracket.add(numberOfMatcheSecondRoundWinnerBracket);

        while (numberOfMatchesInEachPhaseOfWinnersBracket.getLast() > 2) {
            Integer numberOfMatchesNexRound = numberOfMatchesInEachPhaseOfWinnersBracket.getLast() / 2;
            numberOfMatchesInEachPhaseOfWinnersBracket.add(numberOfMatchesNexRound);
        }

        return numberOfMatchesInEachPhaseOfWinnersBracket;
    }



    private void updateNumberOfTeamsInChampionship(Championship championship){
        championship.setNumberOfTeams(championship.getTeams().size());

    }

    private Championship convertDtoToEntity(ChampionshipInsertDto championship){
        Championship championship1 = Championship.builder()
                .name(championship.name())
                .city(championship.city())
                .startDate(championship.startDate())
                .build();
        return championship1;
    }

    private ChampionshipDto convertEntityToDto(Championship championship){
        List<TeamSummaryDto> summaryListOfTeams = new ArrayList<>();
        if (championship.getTeams() != null) {
            summaryListOfTeams = championship.getTeams().stream()
                    .map(team -> new TeamSummaryDto(team.getId(), team.getName()))
                    .collect(Collectors.toList());
        }
        return new ChampionshipDto(
                championship.getId(),
                championship.getName(),
                championship.getStartDate(),
                championship.getCity(),
                championship.getNumberOfTeams(),
                summaryListOfTeams

        );
    }

}
