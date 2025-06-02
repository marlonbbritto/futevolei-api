package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerSummaryDto;
import com.futevolei.championship.futevolei_api.dto.team.*;
import com.futevolei.championship.futevolei_api.exception.BusinessException;
import com.futevolei.championship.futevolei_api.exception.ResourceNotFoundException;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.PlayerRepository;
import com.futevolei.championship.futevolei_api.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ChampionshipRepository championshipRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChampionshipService championshipService;

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

    @Transactional
    public TeamDto insert(TeamInsertDto teamInsertDto){
        Team newTeam = Team.builder()
                .name(teamInsertDto.name())
                .build();
        Team savedTeam = teamRepository.save(newTeam);
        return convertEntityToDto(savedTeam);

    }

    @Transactional
    public void delete(Long id){
        Team teamToDelete = teamRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Team", "ID",id));
        if(teamToDelete.getChampionship()==null){
            teamRepository.delete(teamToDelete);
        }else {
            championshipService.removeTeamInChampionship(teamToDelete.getChampionship().getId(),teamToDelete.getId());
        }


    }

    @Transactional
    public TeamDto updateTeam(Long id, TeamUpdateDto teamUpdateDto){
        Team teamToUpdate = teamRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Team","ID", id));
        if (teamUpdateDto.name()!=null && !teamUpdateDto.name().isBlank()){
            teamToUpdate.setName(teamUpdateDto.name());
        }
        teamRepository.save(teamToUpdate);
        return convertEntityToDto(teamToUpdate);
    }

    @Transactional
    public TeamDto addPlayer(Long idTeam, Long idPlayer){
        Player playerInDb = playerRepository.findById(idPlayer)
                .orElseThrow(()->new ResourceNotFoundException("Player","ID",idPlayer));
        if (playerInDb.getTeam()!=null){
            throw new BusinessException("O jogador "+playerInDb.getName()+" ja esta atribuido ao time "+playerInDb.getTeam().getName());
        }

        Team teamInDb = teamRepository.findById(idTeam)
                .orElseThrow(()-> new ResourceNotFoundException("Team","ID",idTeam));

        if(teamInDb.getPlayers().size()>=2){
            throw  new BusinessException("Esse time ja tem dois jogadores inclusos, logo não pode ser incluido um novo jogador");
        }

        teamInDb.getPlayers().add(playerInDb);
        playerInDb.setTeam(teamInDb);

        teamRepository.save(teamInDb);
        playerRepository.save(playerInDb);

        return convertEntityToDto(teamInDb);

    }

    @Transactional
    public void removePlayer(Long idTeam, Long idPlayer){
        Team teamToRemovePlayer = teamRepository.findById(idTeam)
                .orElseThrow(()->new ResourceNotFoundException("Team","ID",idTeam));

        Player playerToRemove = playerRepository.findById(idPlayer)
                .orElseThrow(()->new ResourceNotFoundException("Player","ID",idPlayer));

        if (!teamToRemovePlayer.getPlayers().contains(playerToRemove)){
            throw new BusinessException("O jogador "+ playerToRemove.getName() + " não pertence a este time");
        }

        teamToRemovePlayer.getPlayers().remove(playerToRemove);
        playerToRemove.setTeam(null);

        teamRepository.save(teamToRemovePlayer);
        playerRepository.save(playerToRemove);
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

            List<TeamSummaryDto> summariesOfChampionship = new ArrayList<>();
            if (champEntity.getTeams() != null) {
                summariesOfChampionship = champEntity.getTeams().stream()
                        .map(teamOfChampionship -> new TeamSummaryDto(teamOfChampionship.getId(), teamOfChampionship.getName()))
                        .collect(Collectors.toList());
            }
            championshipDto = new ChampionshipDto(
                    champEntity.getId(),
                    champEntity.getName(),
                    champEntity.getStartDate(),
                    champEntity.getCity(),
                    champEntity.getNumberOfTeams(),
                    summariesOfChampionship
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
