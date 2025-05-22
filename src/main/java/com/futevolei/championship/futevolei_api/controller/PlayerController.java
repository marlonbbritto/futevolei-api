package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Operation(summary = "Find all players",
    description = "Endpoint toshow all players registered")
    @ApiResponse(responseCode = "200",
    description = "Sucess to requisition")
    @GetMapping
    public ResponseEntity<List<PlayerDto>> findAll(){
        List<PlayerDto> playerDtosList = playerService.listAll();
        return ResponseEntity.ok().body(playerDtosList);
    }

    @Operation(summary = "Find a specific player",
    description = "Endpoint to show data of a specific player registered based on its ID")

    @ApiResponse(responseCode = "200",
            description = "Success: Player found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PlayerDto.class)))

    @GetMapping(value = "/{id}")
    public ResponseEntity<PlayerDto> findById(@PathVariable Long id){
        PlayerDto resultDto = playerService.findById(id);
        return ResponseEntity.ok().body(resultDto);
    }
}
