package mediaSoft.storage.service.search.criteria;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import mediaSoft.storage.service.search.enums.OperationType;
import mediaSoft.storage.service.search.strategy.PredicateStrategy;
import mediaSoft.storage.service.search.strategy.StringPredicateStrategy;

@Data
@Builder
public class StringSearchCriteria implements SearchCriteria<String> {

  private static final PredicateStrategy<String> strategy = new StringPredicateStrategy();

  private final String field;

  @NotBlank
  private final String value;

  private final OperationType operation;

  @Override
  public PredicateStrategy<String> getStrategy() {
    return strategy;
  }

  public String getOperationSymbol() {
    return operation.getSymbol();
  }
}