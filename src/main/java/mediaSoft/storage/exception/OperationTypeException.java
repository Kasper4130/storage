package mediaSoft.storage.exception;

public class OperationTypeException extends RuntimeException {

  public OperationTypeException(String message) {
    super(message);
  }

  public OperationTypeException(String message, Throwable cause) {
    super(message, cause);
  }
}
