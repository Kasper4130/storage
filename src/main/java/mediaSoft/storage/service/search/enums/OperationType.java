package mediaSoft.storage.service.search.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import mediaSoft.storage.exception.OperationTypeException;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public enum OperationType {

  EQUAL("="),
  GREATER_THAN_OR_EQ(">="),
  LESS_THAN_OR_EQ("<="),
  LIKE("~");

  private final String symbol;

  OperationType(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

  public OperationType fromSymbol(String symbol) {
    for (OperationType operationType : values()) {
      if (operationType.symbol.equals(symbol)) {
        return operationType;
      }
    }
    throw new OperationTypeException("Unknown operation symbol: " + symbol);
  }

  @JsonCreator
  public OperationType forValue(String value) {
    return fromSymbol(value);
  }
}