package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/teams")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Operation(summary = "Find all teams",
            description = "Endpoint to show all teams registered")
    @ApiResponse(responseCode = "200",
            description = "Successfully retrieved all teams")
    @GetMapping
    public ResponseEntity<List<TeamDto>> findAll(){
        List<TeamDto> teamDtoList = teamService.findAll();
        return ResponseEntity.ok().body(teamDtoList);
    }
}
