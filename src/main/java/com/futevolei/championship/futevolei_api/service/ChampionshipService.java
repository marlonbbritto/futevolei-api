package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChampionshipService {
    @Autowired
    ChampionshipRepository championshipRepository;

    public Championship insert(ChampionshipInsertDto championship){
        return championshipRepository.save(convertDtoToEntity(championship));
    }

    public Championship convertDtoToEntity(ChampionshipInsertDto championship){
        Championship championship1 = Championship.builder()
                .name(championship.name())
                .city(championship.city())
                .startDate(championship.startDate())
                .build();
        return championship1;
    }

}
