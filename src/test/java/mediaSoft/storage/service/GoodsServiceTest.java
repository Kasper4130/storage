package mediaSoft.storage.service;

import mediaSoft.storage.controller.dto.GoodsRequestDto;
import mediaSoft.storage.controller.dto.GoodsResponseDto;
import mediaSoft.storage.model.Goods;
import mediaSoft.storage.reposiory.GoodsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class GoodsServiceTest {

    @Mock
    private GoodsRepository goodsRepository;

    @InjectMocks
    private GoodsService goodsService;

    private Goods good;
    private GoodsRequestDto requestDto;
    private UUID goodId;

    @BeforeEach
    void setUp() {
        goodId = UUID.randomUUID();
        good = new Goods();
        good.setId(goodId);
        requestDto = GoodsRequestDto.builder()
                .name("good")
                .article("123321")
                .description("A good good")
                .goodCategory("Furniture")
                .price(BigDecimal.TEN)
                .quantity(1)
                .build();
    }

    @Test
    void getAllGoods_ShouldReturnListOfGoods() {
        when(goodsRepository.findAll()).thenReturn(Collections.singletonList(good));
        List<GoodsResponseDto> result = goodsService.getAllGoods();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(goodId.toString(), result.get(0).getId());
    }

    @Test
    void getGoodById_ShouldReturnGood() {
        when(goodsRepository.findById(goodId)).thenReturn(Optional.of(good));
        GoodsResponseDto result = goodsService.getGoodById(goodId);
        assertEquals(goodId.toString(), result.getId());
    }

    @Test
    void createGood_ShouldSaveAndReturnGood() {
        when(goodsRepository.save(any(Goods.class))).thenReturn(good);
        GoodsResponseDto result = goodsService.createGood(requestDto);
        assertEquals(goodId.toString(), result.getId());
    }

    @Test
    void updateGood_ShouldUpdateSaveAndReturnGood() {
        when(goodsRepository.findById(goodId)).thenReturn(Optional.of(good));
        when(goodsRepository.save(any(Goods.class))).thenReturn(good);
        GoodsResponseDto result = goodsService.updateGood(goodId, requestDto);
        assertEquals(goodId.toString(), result.getId());
    }

    @Test
    void deleteGoodById_ShouldCallDeleteMethod() {
        doNothing().when(goodsRepository).deleteById(goodId);
        goodsService.deleteGoodById(goodId);
        verify(goodsRepository).deleteById(goodId);
    }
}