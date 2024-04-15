package mediaSoft.storage.exception;

public class ArticleAlreadyExistsException extends RuntimeException {

  public ArticleAlreadyExistsException(String message) {
    super(message);
  }

  public ArticleAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
