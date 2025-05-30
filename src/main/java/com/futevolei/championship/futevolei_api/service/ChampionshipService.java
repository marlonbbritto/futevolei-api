package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertTeamDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipUpdateDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.exception.ResourceNotFoundException;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
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
    public ChampionshipDto insertTeamInChampionship (Long idChampionship, ChampionshipInsertTeamDto championshipInsertTeamDto){
        Championship championshipDbToInsertTeam = championshipRepository
                .findById(idChampionship)
                .orElseThrow(()->new ResourceNotFoundException("Championship","ID",idChampionship));

        Team teamToInsertInChampionship = teamRepository
                .findById(championshipInsertTeamDto.id())
                .orElseThrow(()-> new ResourceNotFoundException("Team","ID",championshipDbToInsertTeam.getId()));

        championshipDbToInsertTeam.getTeams().add(teamToInsertInChampionship);

        updateNumberOfTeamsInChampionship(championshipDbToInsertTeam);

        teamToInsertInChampionship.setChampionship(championshipDbToInsertTeam);

        teamRepository.save(teamToInsertInChampionship);

        championshipRepository.save(championshipDbToInsertTeam);

        return convertEntityToDto(championshipDbToInsertTeam);
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
        return new ChampionshipDto(
                championship.getId(),
                championship.getName(),
                championship.getStartDate(),
                championship.getCity(),
                championship.getNumberOfTeams()

        );
    }

}
