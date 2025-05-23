package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerPaymentUpdateDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerUpdateDto;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;
import com.futevolei.championship.futevolei_api.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @MockitoBean
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("Should bring all registered players when everything is correct")
    void findAll_ReturnListOfPlayersDto(){
        Player player1 = Player.builder()
                .id(1L)
                .name("João Silva")
                .build();

        Player player2 = Player.builder()
                .id(2L)
                .name("Carlos Barbosa")
                .build();

        List<Player> players = Arrays.asList(player1,player2);

        when(playerRepository.findAll()).thenReturn(players);

        List<PlayerDto> dtoList = playerService.listAll();

        assertEquals(2, dtoList.size());
        assertEquals("João Silva",dtoList.get(0).name());
        assertEquals("Carlos Barbosa",dtoList.get(1).name());
    }

    @Test
    @DisplayName("Should bring specific registered player when Id exist")
    void findById_ReturnsSpecifPlayerDto(){
        Player player1 = Player.builder()
                .id(1L)
                .name("João Silva")
                .build();

        Long idToFind = player1.getId();

        when(playerRepository.findById(idToFind)).thenReturn(Optional.of(player1));

        PlayerDto resultDto = playerService.findById(idToFind);

        assertNotNull(resultDto);
        assertEquals(player1.getId(),resultDto.id());
        assertEquals(player1.getName(),resultDto.name());
        assertEquals(player1.getTeam(),resultDto.team());
        assertEquals(player1.getRegistrations(),resultDto.registrations());
    }

    @Test
    @DisplayName("Should update payment status when Id exist and everything is correct")
    void paymentStatusUpdate_ReturnsPlayDtoWithUpdatedPayment(){
        Player playerEntity = Player.builder()
                .id(1L)
                .name("João Silva")
                .build();
        Long playerId = playerEntity.getId();

        PlayerPaymentUpdateDto playerUpdatDto = new PlayerPaymentUpdateDto(Registrations.PAID);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(playerEntity));
        when(playerRepository.save(any(Player.class))).thenAnswer((InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        });

        PlayerDto resultDto = playerService.paymentStatusUpdate(playerId,playerUpdatDto);

        assertNotNull(resultDto);
        assertEquals(Registrations.PAID,resultDto.registrations());
        assertEquals(playerId,resultDto.id());
        assertEquals("João Silva",resultDto.name());
        assertNull(resultDto.team());
    }

}
