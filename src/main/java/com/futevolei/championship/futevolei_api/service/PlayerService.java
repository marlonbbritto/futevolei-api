package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerInsertDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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

    public PlayerDto findById(Long id){
        Optional<Player> playerDto = playerRepository.findById(id);
        return playerDto
                .map(this::convertEntityToDto)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Not found championship with this ID: "+id));
    }

    public PlayerDto insert(PlayerInsertDto playerInsertDto){
        Player player = Player.builder()
                .name(playerInsertDto.name())
                .build();
        playerRepository.save(player);
        return convertEntityToDto(player);

    }

    public void delete(Long id){
        Optional<Player> player = playerRepository.findById(id);
        if (player.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND
                    ,"Not found player with this ID: "+id);
        }
        playerRepository.deleteById(id);
    }

    private PlayerDto convertEntityToDto(Player player){
        return new PlayerDto(
                player.getId(),
                player.getName(),
                player.getTeam()
        );
    }

    private Player convertDtoToEntity(PlayerDto playerDto){
        return Player.builder()
                .id(playerDto.id())
                .name(playerDto.name())
                .team(playerDto.team())
                .build();
    }

}
