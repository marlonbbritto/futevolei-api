package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerInsertDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerPaymentUpdateDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerUpdateDto;
import com.futevolei.championship.futevolei_api.exception.ResourceNotFoundException;
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
                .orElseThrow(()-> new ResourceNotFoundException("Player","ID",id));
    }

    public PlayerDto paymentStatusUpdate(Long id, PlayerPaymentUpdateDto playerPaymentUpdateDto){
        Optional<Player> player = playerRepository.findById(id);
        return player
                .map(existingPlayer->{
                    if(playerPaymentUpdateDto.registrations()!=null){
                        existingPlayer.setRegistrations(playerPaymentUpdateDto.registrations());
                    }
                    Player updatedPlayer = playerRepository.save(existingPlayer);
                    return convertEntityToDto(updatedPlayer);
                }).orElseThrow(()->new ResourceNotFoundException("Player","ID",id));

    }

    public PlayerDto insert(PlayerInsertDto playerInsertDto){
        Player player = Player.builder()
                .name(playerInsertDto.name())
                .build();
        Player savedPlayer = playerRepository.save(player);
        return convertEntityToDto(savedPlayer);

    }

    public void delete(Long id){
        Optional<Player> player = playerRepository.findById(id);
        if (player.isEmpty()){
            throw new ResourceNotFoundException("Player","ID",id);
        }
        playerRepository.deleteById(id);
    }

    public PlayerDto update(Long id, PlayerUpdateDto playerUpdateDto){
        Optional<Player> playerDto = playerRepository.findById(id);

        return playerDto.map(existingPlayer->{
            if(playerUpdateDto.name()!=null){
                existingPlayer.setName(playerUpdateDto.name());
            }
            if(playerUpdateDto.registrations()!=null){
                existingPlayer.setRegistrations(playerUpdateDto.registrations());
            }
            if(playerUpdateDto.team()!=null){
                existingPlayer.setTeam(playerUpdateDto.team());
            }
            Player updatedPlayer = playerRepository.save(existingPlayer);
            return convertEntityToDto(updatedPlayer);
        }).orElseThrow(()->new ResourceNotFoundException("Player","ID",id));
    }

    private PlayerDto convertEntityToDto(Player player){
        return new PlayerDto(
                player.getId(),
                player.getName(),
                player.getTeam(),
                player.getRegistrations()
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
