package mediaSoft.storage.service.search.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import mediaSoft.storage.service.search.enums.OperationType;
import mediaSoft.storage.service.search.strategy.PredicateStrategy;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        include = JsonTypeInfo.As.PROPERTY,
        property = "field"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "name"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "description"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "goodCategory"),
        @JsonSubTypes.Type(value = BigDecimalSearchCriteria.class, name = "price"),
        @JsonSubTypes.Type(value = IntegerSearchCriteria.class, name = "quantity"),
        @JsonSubTypes.Type(value = LocalDateTimeSearchCriteria.class, name = "lastQuantityChange"),
        @JsonSubTypes.Type(value = LocalDateTimeSearchCriteria.class, name = "createdAt"),
})
public interface SearchCriteria<T> {

  String getField();

  @NotNull
  OperationType getOperation();

  @NotNull
  T getValue();

  PredicateStrategy<T> getStrategy();
}