package mediaSoft.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mediaSoft.storage.controller.dto.GoodsRequestDto;
import mediaSoft.storage.controller.dto.GoodsResponseDto;
import mediaSoft.storage.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(GoodsController.class)
public class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsService goodsService;

    @Test
    public void getAllGoods_Success_ShouldReturnListOfGoods() throws Exception {
        GoodsResponseDto dto1 = createResponseGood1(UUID.randomUUID().toString());
        GoodsResponseDto dto2 = createResponseGood2(UUID.randomUUID().toString());
        List<GoodsResponseDto> goodsList = Arrays.asList(dto1, dto2);

        given(goodsService.getAllGoods()).willReturn(goodsList);

        mockMvc.perform(get("/goods"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Good1"))
                .andExpect(jsonPath("$[1].name").value("Good2"));
    }

    @Test
    public void getGoodById_Success_ShouldReturnGood() throws Exception {
        UUID id = UUID.randomUUID();
        GoodsResponseDto dto = createResponseGood1(id.toString());
        when(goodsService.getGoodById(id)).thenReturn(dto);

        mockMvc.perform(get("/goods/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Good1"));
    }

    @Test
    public void getGoodById_Failure_ShouldReturnNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(goodsService.getGoodById(any(UUID.class))).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/goods/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createGood_Success_ShouldReturnCreatedGood() throws Exception {
        GoodsRequestDto requestDto = createRequestGood1();
        GoodsResponseDto responseDto =createResponseGood1(UUID.randomUUID().toString());

        when(goodsService.createGood(any(GoodsRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/goods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Good1"));
    }

    @Test
    public void createGood_Failure_ShouldReturnBadRequest() throws Exception {
        GoodsRequestDto requestDto = GoodsRequestDto.builder().build();
        mockMvc.perform(post("/goods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateGood_Success_ShouldReturnUpdatedGood() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        GoodsRequestDto requestDto = createRequestGood1();
        GoodsResponseDto responseDto = createResponseGood1(id.toString());

        when(goodsService.updateGood(any(UUID.class), any(GoodsRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/goods/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Good1"));
    }

    @Test
    public void updateGood_Failure_ShouldReturnNotFound() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        GoodsRequestDto requestDto = createRequestGood1();
        requestDto.setName("Non-existent Good");

        when(goodsService.updateGood(any(UUID.class), any(GoodsRequestDto.class)))
                .thenThrow(new NoSuchElementException());

        // Act & Assert
        mockMvc.perform(put("/goods/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteGood_Success_ShouldReturnNoContent() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(goodsService).deleteGoodById(id);

        // Act & Assert
        mockMvc.perform(delete("/goods/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteGood_Failure_ShouldReturnNotFound() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new NoSuchElementException()).when(goodsService).deleteGoodById(any(UUID.class));

        // Act & Assert
        mockMvc.perform(delete("/goods/{id}", id))
                .andExpect(status().isNotFound());
    }

    private GoodsRequestDto createRequestGood1() {
        return GoodsRequestDto.builder()
                .name("Good1")
                .article("Article1")
                .description("Description1")
                .goodCategory("Category1")
                .price(BigDecimal.TEN)
                .quantity(5)
                .build();
    }

    private GoodsResponseDto createResponseGood1(String id) {
        return GoodsResponseDto.builder()
                .id(id)
                .name("Good1")
                .article("Article1")
                .description("Description1")
                .goodCategory("Category1")
                .price(BigDecimal.TEN)
                .quantity(5)
                .lastQuantityChange(null)
                .createdAt(null)
                .build();
    }

    private GoodsResponseDto createResponseGood2(String id) {
        return GoodsResponseDto.builder()
                .id(id)
                .name("Good2")
                .article("Article2")
                .description("Description2")
                .goodCategory("Category2")
                .price(BigDecimal.valueOf(15))
                .quantity(5)
                .lastQuantityChange(null)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
