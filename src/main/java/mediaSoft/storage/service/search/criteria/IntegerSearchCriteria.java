package mediaSoft.storage.service.search.criteria;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import mediaSoft.storage.service.search.enums.OperationType;
import mediaSoft.storage.service.search.strategy.IntegerPredicateStrategy;
import mediaSoft.storage.service.search.strategy.PredicateStrategy;

@Data
@Builder
public class IntegerSearchCriteria implements SearchCriteria<Integer> {

  private static final PredicateStrategy<Integer> strategy = new IntegerPredicateStrategy();

  private final String field;

  @PositiveOrZero
  private final Integer value;

  private final OperationType operation;

  @Override
  public PredicateStrategy<Integer> getStrategy() {
    return strategy;
  }

  public String getOperationSymbol() {
    return operation.getSymbol();
  }
}