package mediaSoft.storage.service.search.criteria;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import mediaSoft.storage.service.search.enums.OperationType;
import mediaSoft.storage.service.search.strategy.BigDecimalPredicateStrategy;
import mediaSoft.storage.service.search.strategy.PredicateStrategy;

import java.math.BigDecimal;

@Data
@Builder
public class BigDecimalSearchCriteria implements SearchCriteria<BigDecimal> {

  private static final PredicateStrategy<BigDecimal> strategy = new BigDecimalPredicateStrategy();

  private final String field;

  @PositiveOrZero
  private final BigDecimal value;

  private final OperationType operation;

  @Override
  public PredicateStrategy<BigDecimal> getStrategy() {
    return strategy;
  }

  public String getOperationSymbol() {
    return operation.getSymbol();
  }
}