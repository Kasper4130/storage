package mediaSoft.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mediaSoft.storage.controller.dto.GoodsRequestDto;
import mediaSoft.storage.controller.dto.GoodsResponseDto;
import mediaSoft.storage.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/goods")
@Tag(name = "GoodsController", description = "Controller for managing goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Operation(summary = "Get a list of all goods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<GoodsResponseDto>> getAllGoods() {
        List<GoodsResponseDto> goodsList = goodsService.getAllGoods();
        return ResponseEntity.ok(goodsList);
    }

    @Operation(summary = "Get a single good by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved good"),
            @ApiResponse(responseCode = "404", description = "Good not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GoodsResponseDto> getGoodById(@Parameter(description = "ID of the good to retrieve", required = true)
                                                            @PathVariable UUID id) {
        return ResponseEntity.ok(goodsService.getGoodById(id));
    }

    @Operation(summary = "Create a new good")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Good successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid parameters or validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An unexpected error occurred")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GoodsResponseDto> createGood(@Parameter(description = "Good object to store", required = true)
                                                           @Valid
                                                           @RequestBody GoodsRequestDto goodsRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goodsService.createGood(goodsRequestDto));
    }

    @Operation(summary = "Update an existing good")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated good"),
            @ApiResponse(responseCode = "404", description = "Good not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid parameters or validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - An unexpected error occurred")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GoodsResponseDto> updateGood(@Parameter(description = "ID of the good to update", required = true)
                                                            @PathVariable UUID id,
                                                       @Parameter(description = "Updated good object", required = true)
                                                            @Valid
                                                            @RequestBody GoodsRequestDto goodsRequestDto) {
        return ResponseEntity.ok(goodsService.updateGood(id, goodsRequestDto));
    }

    @Operation(summary = "Delete a good")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted good"),
            @ApiResponse(responseCode = "404", description = "Good not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGood(@Parameter(description = "ID of the good to delete", required = true) @PathVariable UUID id) {
        goodsService.deleteGoodById(id);
    }
}
