package com.futevolei.championship.futevolei_api.controller;

import com.futevolei.championship.futevolei_api.dto.MatchDto;
import com.futevolei.championship.futevolei_api.dto.match.MatchUpdateDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerDto;
import com.futevolei.championship.futevolei_api.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @Operation(summary = "Find all matches",
            description = "Endpoint to show all matches registered")
    @ApiResponse(responseCode = "200",
            description = "Successfully retrieved all matches")
    @GetMapping
    public ResponseEntity<List<MatchDto>> findAll(){
        List<MatchDto> resultMatchDtoList = matchService.findAll();
        return ResponseEntity.ok().body(resultMatchDtoList);
    }

    @Operation(summary = "Find an specific match",
            description = "Endpoint to show an specific match registered")
    @ApiResponse(responseCode = "200",
            description = "Successfully retrieved an specific match")
    @GetMapping(value = "/{id}")
    public ResponseEntity<MatchDto> findById(@PathVariable Long id){
        MatchDto resultMatchDtoToFind = matchService.findById(id);
        return ResponseEntity.ok().body(resultMatchDtoToFind);
    }

    @Operation(summary = "Update an specific match",
            description = "Endpoint to update an specific match")
    @ApiResponse(responseCode = "200",
            description = "Successfully updated an specific match",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MatchDto.class)))
    @PatchMapping(value = "/{id}")
    public ResponseEntity<MatchDto> updateMatch(@PathVariable Long id, @RequestBody MatchUpdateDto matchUpdateDto){
        MatchDto resultMatchDtoToUpdate = matchService.updateMatch(id, matchUpdateDto);
        return ResponseEntity.ok().body(resultMatchDtoToUpdate);
    }
}
