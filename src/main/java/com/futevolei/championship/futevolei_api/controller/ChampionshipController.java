package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipInsertDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.service.ChampionshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/championship")
public class ChampionshipController {
    @Autowired
    private ChampionshipService championshipService;

    @Operation(summary = "Find all championship",
    description = "Endpoint to show all championship registered")

    @ApiResponse(responseCode = "200",
    description = "Success to requisition")
    @GetMapping
    public ResponseEntity<List<ChampionshipDto>> findAll(){
        List<ChampionshipDto> championshipDtoList = championshipService.findAll();
        return ResponseEntity.ok().body(championshipDtoList);
    }

    @Operation(summary = "Find a specific championship",
            description = "Endpoint to show data of a specific championship registered based on its ID")
    @ApiResponse(responseCode = "200",
            description = "Success: Championship found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ChampionshipDto.class)))
    @ApiResponse(responseCode = "404", description = "Error: Championship not found with the provided ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ChampionshipDto> findById(@PathVariable Long id){
        ChampionshipDto championshipDto = championshipService.findById(id);
        return ResponseEntity.ok().body(championshipDto);
    }

    @Operation(summary = "Create a new championship",
            description = "Endpoint pto register a new championship in the system")
    @ApiResponse(responseCode = "201", description = "Success to create a new championship",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Championship.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping
    public ResponseEntity<Championship> insert(@RequestBody ChampionshipInsertDto championship){
        Championship newChampionship = championshipService.insert(championship);
        return ResponseEntity.status(HttpStatus.CREATED).body(newChampionship);
    }

    @Operation(summary = "Delete a specific championship",
            description = "Endpoint to show delete a specific championship registered based on its ID")
    @ApiResponse(responseCode = "204",
            description = "Success: Championship deleted")
    @ApiResponse(responseCode = "404", description = "Error: Championship not found with the provided ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        championshipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
