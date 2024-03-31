package mediaSoft.storage.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class GoodsRequestDto {

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
}
