package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerSummaryDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamInsertDto;
import com.futevolei.championship.futevolei_api.exception.ResourceNotFoundException;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ChampionshipRepository championshipRepository;

    public List<TeamDto> findAll(){
        List<Team> teamsList = teamRepository.findAll();
        return teamsList
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

    }

    public TeamDto findById(Long id){
        Optional<Team> team = teamRepository.findById(id);
        return team
                .map(this::convertEntityToDto)
                .orElseThrow(()->new ResourceNotFoundException("Team", "ID",id));
    }

    public TeamDto insert(TeamInsertDto teamInsertDto){
        Championship championship = championshipRepository.findById(teamInsertDto.championshipId())
                .orElseThrow(()-> new ResourceNotFoundException("Championship", "ID",teamInsertDto.championshipId()));
        Team newTeam = Team.builder()
                .name(teamInsertDto.name())
                .championship(championship)
                .build();
        Team savedTeam = teamRepository.save(newTeam);
        return convertEntityToDto(savedTeam);

    }

    public TeamDto convertEntityToDto(Team team){
        List<PlayerSummaryDto> playerSummaryDtosList = team.getPlayers()
                .stream()
                .map(player -> new PlayerSummaryDto(
                        player.getId(),
                        player.getName(),
                        player.getRegistrations()
                ))
                .collect(Collectors.toList());

        ChampionshipDto championshipDto = null;
        if (team.getChampionship() != null) {
            Championship champEntity = team.getChampionship();
            championshipDto = new ChampionshipDto(
                    champEntity.getId(),
                    champEntity.getName(),
                    champEntity.getStartDate(),
                    champEntity.getCity(),
                    champEntity.getNumberOfTeams()
            );
        }

        return new TeamDto(
                team.getId(),
                team.getName(),
                playerSummaryDtosList,
                championshipDto
        );
    }

}
