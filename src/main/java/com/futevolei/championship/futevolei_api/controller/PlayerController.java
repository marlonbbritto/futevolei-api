package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<PlayerDto>> findAll(){
        List<PlayerDto> playerDtosList = playerService.listAll();
        return ResponseEntity.ok().body(playerDtosList);
    }
}
