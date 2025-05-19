package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/championship")
public class ChampionshipController {
    @Autowired
    private ChampionshipService championshipService;

    @PostMapping
    public ResponseEntity<Championship> insert(@RequestBody ChampionshipInsertDto championship){
        Championship newChampionship = championshipService.insert(championship);
        return ResponseEntity.status(HttpStatus.CREATED).body(newChampionship);
    }
}
