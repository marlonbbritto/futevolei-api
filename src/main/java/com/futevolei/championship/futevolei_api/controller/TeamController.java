package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamUpdateDto;
import com.futevolei.championship.futevolei_api.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Find a specific team",
            description = "Endpoint to show data of a specific team registered based on its ID")

    @ApiResponse(responseCode = "200",
            description = "Success: team found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDto.class)))

    @GetMapping(value = "/{id}")
    public ResponseEntity<TeamDto> findById(@PathVariable Long id){
        TeamDto resultDto = teamService.findById(id);
        return ResponseEntity.ok().body(resultDto);
    }

    @Operation(summary = "Update an specific team",
            description = "Endpoint to update an of a specific team registered based on its ID")

    @ApiResponse(responseCode = "200",
            description = "Success: team updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDto.class)))

    @PatchMapping(value = "/{id}")
    public ResponseEntity<TeamDto> partialUpdateTeam(@PathVariable Long id,@Valid @RequestBody TeamUpdateDto teamUpdateDto){
        TeamDto resultDto = teamService.updateTeam(id, teamUpdateDto);
        return ResponseEntity.ok().body(resultDto);
    }

    @Operation(summary = "Delete an specific team",
            description = "Endpoint to delete an specific team registered based on its ID")

    @ApiResponse(responseCode = "No Content",
            description = "Success: team deleted",
            content = @Content(mediaType = "application/json"))

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id){
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
