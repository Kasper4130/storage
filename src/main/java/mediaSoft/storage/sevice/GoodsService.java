package mediaSoft.storage.sevice;

import mediaSoft.storage.controller.dto.GoodsRequestDto;
import mediaSoft.storage.controller.dto.GoodsResponseDto;
import mediaSoft.storage.model.Goods;
import mediaSoft.storage.reposiory.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * Retrieve all goods
     * @return list of goods
     */
    public List<GoodsResponseDto> getAllGoods() {
        return goodsRepository.findAll().stream()
                .map(this::goodsToResponseDto).collect(Collectors.toList());
    }

    /**
     * Retrieve a good by id
     * @param id of a good
     * @return good
     */
    public GoodsResponseDto getGoodById(UUID id) {
        return goodsToResponseDto(
                goodsRepository.findById(id).orElseThrow()
        );
    }

    /**
     * Create a good
     * @param requestDto with updated fields
     * @return good
     */
    public GoodsResponseDto createGood(GoodsRequestDto requestDto) {
        return goodsToResponseDto(
                goodsRepository.save(requestDtoToGoods(requestDto))
        );
    }

    /**
     * Update a good by id
     * @param id of a good
     * @param requestDto with updated fields
     * @return good
     */
    public GoodsResponseDto updateGood(UUID id, GoodsRequestDto requestDto) {
        Goods existingGoods = goodsRepository.findById(id).orElseThrow();
        Goods newGoods = requestDtoToGoods(requestDto);
        newGoods.setId(existingGoods.getId());
        return goodsToResponseDto(
                goodsRepository.save(newGoods)
        );
    }

    /**
     * Delete a good by id
     * @param id of a good
     */
    public void deleteGoodById(UUID id) {
        goodsRepository.deleteById(id);
    }

    private GoodsResponseDto goodsToResponseDto(Goods goods) {
        return GoodsResponseDto.builder()
                .id(goods.getId().toString())
                .name(goods.getName())
                .article(goods.getArticle())
                .description(goods.getDescription())
                .goodCategory(goods.getGoodCategory())
                .price(goods.getPrice())
                .quantity(goods.getQuantity())
                .lastQuantityChange(goods.getLastQuantityChange())
                .createdAt(goods.getCreatedAt())
                .build();
    }

    private Goods requestDtoToGoods(GoodsRequestDto requestDto) {
        return Goods.builder()
                .name(requestDto.getName())
                .article(requestDto.getArticle())
                .description(requestDto.getDescription())
                .goodCategory(requestDto.getGoodCategory())
                .price(requestDto.getPrice())
                .quantity(requestDto.getQuantity())
                .lastQuantityChange(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
