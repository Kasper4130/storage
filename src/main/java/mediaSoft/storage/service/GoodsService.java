package mediaSoft.storage.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import mediaSoft.storage.controller.dto.GoodsRequestDto;
import mediaSoft.storage.controller.dto.GoodsResponseDto;
import mediaSoft.storage.exception.ArticleAlreadyExistsException;
import mediaSoft.storage.model.Goods;
import mediaSoft.storage.reposiory.GoodsRepository;
import mediaSoft.storage.service.search.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    EntityManager em;

    /**
     * Retrieve all goods
     * @Param Pageable with page and size
     * @return Page of goods
     */
    public Page<GoodsResponseDto> getAllGoods(Pageable pageable) {
        Page<Goods> goodsPage = goodsRepository.findAll(pageable);
        return goodsPage.map(this::goodsToResponseDto);
    }

    /**
     * Retrieve goods by requests
     * @Param list of SearchCriteria
     * @Param PageRequest with page and size
     * @return List of goods
     */
    public <T> List<GoodsResponseDto> searchGoods(List<SearchCriteria<T>> searchCriteriaList, PageRequest pageRequest) {

        Specification<Goods> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteria<T> sc : searchCriteriaList) {

                switch (sc.getOperation()) {
                    case EQUAL ->
                            predicates.add(sc.getStrategy().getEqPattern(root.get(sc.getField()), sc.getValue(), criteriaBuilder));
                    case GREATER_THAN_OR_EQ ->
                            predicates.add(sc.getStrategy().getLeftLimitPattern(root.get(sc.getField()), sc.getValue(), criteriaBuilder));
                    case LESS_THAN_OR_EQ ->
                            predicates.add(sc.getStrategy().getRightLimitPattern(root.get(sc.getField()), sc.getValue(), criteriaBuilder));
                    case LIKE ->
                            predicates.add(sc.getStrategy().getLikePattern(root.get(sc.getField()), sc.getValue(), criteriaBuilder));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Goods> goods = goodsRepository.findAll(specification, pageRequest);

        return goods.getContent().stream()
                .map(this::goodsToResponseDto)
                .collect(Collectors.toList());
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

        Goods goods = goodsRepository.findByArticle(requestDto.getArticle());
        if (goods == null) {
            return goodsToResponseDto(
                    goodsRepository.save(requestDtoToGoods(requestDto))
            );
        }
        throw new ArticleAlreadyExistsException(requestDto.getArticle());
    }

    /**
     * Update a good by id
     * @param id of a good
     * @param requestDto with updated fields
     * @return good
     */
    public GoodsResponseDto updateGood(UUID id, GoodsRequestDto requestDto) {

        Goods goods = goodsRepository.findByArticle(requestDto.getArticle());
        if (goods == null) {

            Goods existingGoods = goodsRepository.findById(id).orElseThrow();
            Goods newGoods = requestDtoToGoods(requestDto);
            newGoods.setId(existingGoods.getId());
            return goodsToResponseDto(
                    goodsRepository.save(newGoods)
            );
        }
        throw new ArticleAlreadyExistsException(requestDto.getArticle());
    }

    /**
     * Delete a good by id
     * @param id of a good
     */
    public void deleteGoodById(UUID id) {
        goodsRepository.findById(id).orElseThrow();
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