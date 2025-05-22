package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<PlayerDto> listAll (){
        List<Player> players = playerRepository.findAll();
        return players
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

    }

    private PlayerDto convertEntityToDto(Player player){
        return new PlayerDto(
                player.getId(),
                player.getName(),
                player.getTeam()
        );
    }

}
