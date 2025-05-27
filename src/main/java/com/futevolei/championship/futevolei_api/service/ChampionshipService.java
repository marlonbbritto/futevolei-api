package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipUpdateDto;
import com.futevolei.championship.futevolei_api.exception.ResourceNotFoundException;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChampionshipService {
    @Autowired
    ChampionshipRepository championshipRepository;

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

    public Championship insert(ChampionshipInsertDto championship){
        return championshipRepository.save(convertDtoToEntity(championship));
    }

    public void delete(Long id){
        if(!championshipRepository.existsById(id)){
            throw new ResourceNotFoundException("Championship","ID", id);
        }
        championshipRepository.deleteById(id);
    }

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
            if(championshipUpdateDto.numberOfTeams()!=null){
                existingChampionship.setNumberOfTeams(championshipUpdateDto.numberOfTeams());
            }
            Championship updatedChampionship = championshipRepository.save(existingChampionship);
            return convertEntityToDto(updatedChampionship);
        }).orElseThrow(()-> new  ResourceNotFoundException("Championship","ID", id));

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
