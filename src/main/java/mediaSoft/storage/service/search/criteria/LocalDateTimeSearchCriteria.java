package mediaSoft.storage.service.search.criteria;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import mediaSoft.storage.service.search.enums.OperationType;
import mediaSoft.storage.service.search.strategy.LocalDateTimePredicateStrategy;
import mediaSoft.storage.service.search.strategy.PredicateStrategy;

import java.time.LocalDateTime;

@Data
@Builder
public class LocalDateTimeSearchCriteria implements SearchCriteria<LocalDateTime> {

  private static final PredicateStrategy<LocalDateTime> strategy = new LocalDateTimePredicateStrategy();

  private final String field;

  @PastOrPresent
  private final LocalDateTime value;

  private final OperationType operation;

  @Override
  public PredicateStrategy<LocalDateTime> getStrategy() {
    return strategy;
  }

  public String getOperationSymbol() {
    return operation.getSymbol();
  }
}