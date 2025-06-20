package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamAddPlayerIdDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamInsertDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamUpdateDto;
import com.futevolei.championship.futevolei_api.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Insert an team",
            description = "Endpoint to insert an team")

    @ApiResponse(responseCode = "201",
            description = "Success: team inserted",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDto.class)))

    @PostMapping
    public ResponseEntity<TeamDto> insertTeam (@Valid @RequestBody TeamInsertDto teamInsertDto){
        TeamDto resultDto = teamService.insert(teamInsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultDto);
    }

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

    @ApiResponse(responseCode = "204",
            description = "Success: team deleted",
            content = @Content(mediaType = "application/json"))

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id){
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "insert an Player in a specific team",
            description = "Endpoint to insert an Player an specific team registered based on its ID")

    @ApiResponse(responseCode = "200",
            description = "Success: Player add the team",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeamDto.class)))

    @PostMapping(value = "/{id}/players")
    public ResponseEntity<TeamDto> addPlayerInAnTeam(@PathVariable Long id,@Valid @RequestBody TeamAddPlayerIdDto idPlayer){
        TeamDto resultDto = teamService.addPlayer(id, idPlayer.id());
        return ResponseEntity.ok().body(resultDto);
    }

    @Operation(summary = "remove an Player in a specific team",
            description = "Endpoint to remove an Player an specific team registered based on its ID")

    @ApiResponse(responseCode = "204",
            description = "Success: Player remove the team",
            content = @Content(mediaType = "application/json"))

    @DeleteMapping(value = "/{id}/players/{idPlayer}")
    public ResponseEntity<Void> removePlayerInAnTeam(@PathVariable Long id,@PathVariable Long idPlayer){
        teamService.removePlayer(id, idPlayer);
        return ResponseEntity.noContent().build();
    }


}
