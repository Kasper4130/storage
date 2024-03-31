package mediaSoft.storage.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class GoodsResponseDto {

    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String article;

    private String description;

    @NotBlank
    private String goodCategory;

    @PositiveOrZero
    private BigDecimal price;

    @PositiveOrZero
    private Integer quantity;

    private LocalDateTime lastQuantityChange;

    private LocalDateTime createdAt;
}
