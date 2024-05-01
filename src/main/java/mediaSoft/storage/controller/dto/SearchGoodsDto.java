package mediaSoft.storage.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchGoodsDto {

  @NotBlank
  private String field;

  @NotNull
  private String value;

  @NotBlank
  private String operation;
}