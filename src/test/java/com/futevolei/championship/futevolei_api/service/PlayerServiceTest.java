package com.futevolei.championship.futevolei_api.service;

import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerInsertDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
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

    @Test
    @DisplayName("Shoul insert new Player when everthing is correct")
    void insertPlayer_ReturnNewPlayerDto(){
        PlayerInsertDto playerInsertDto = new PlayerInsertDto(
                "Juliana Cardoso"
        );
        Player entityPlayer = Player.builder()
                .id(1L)
                .name("Juliana Cardoso")
                .build();

        when(playerRepository.save(any(Player.class))).thenReturn(entityPlayer);

        PlayerDto resultDto = playerService.insert(playerInsertDto);

        assertEquals(entityPlayer.getName(),resultDto.name());
        assertEquals(entityPlayer.getTeam(),resultDto.team());
        assertEquals(entityPlayer.getRegistrations(),resultDto.registrations());
        assertEquals(entityPlayer.getId(),resultDto.id());

    }

    @Test
    @DisplayName("Should delete an specific Player when Id exist and everything is correct")
    void deletePlayer_ReturnNoContent(){
        Player entityPlayer = Player.builder()
                .id(1L)
                .name("Juliana Cardoso")
                .build();
        Long idToFind = entityPlayer.getId();


        when(playerRepository.findById(idToFind)).thenReturn(Optional.of(entityPlayer));
        doNothing().when(playerRepository).deleteById(idToFind);

        assertDoesNotThrow(() -> playerService.delete(idToFind));

        verify(playerRepository,times(1)).findById(idToFind);
        verify(playerRepository,times(1)).deleteById(idToFind);


    }

    @Test
    @DisplayName("Should update a specific Player when Id exist and everything is correct")
    void update_ReturnUpdateDto(){
        Player entityPlayer = Player.builder()
                .id(1L)
                .name("Juliana Cardoso")
                .registrations(Registrations.TO_PAY)
                .team(null)
                .build();
        Long idToFind = entityPlayer.getId();

        PlayerUpdateDto updatedDto = new PlayerUpdateDto(
                "Juliana Cardoso Britto",
                null,
                Registrations.PAID
        );

        Player entityPlayerUpdated = Player.builder()
                        .id(idToFind)
                        .name(updatedDto.name())
                        .registrations(updatedDto.registrations())
                        .team(null)
                        .build();

        when(playerRepository.findById(idToFind)).thenReturn(Optional.of(entityPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(entityPlayerUpdated);
        PlayerDto result = playerService.update(idToFind,updatedDto);

        assertNotNull(result);
        assertEquals(result.id(),entityPlayerUpdated.getId());
        assertEquals(result.name(),entityPlayerUpdated.getName());
        assertEquals(result.team(),entityPlayerUpdated.getTeam());
        assertEquals(result.registrations(),entityPlayerUpdated.getRegistrations());

        verify(playerRepository, times(1)).findById(idToFind);
        verify(playerRepository, times(1)).save(any(Player.class));


    }
}
