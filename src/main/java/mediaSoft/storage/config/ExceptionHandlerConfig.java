package mediaSoft.storage.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mediaSoft.storage.exception.ArticleAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerConfig {

    /**
     * Handle validation exceptions and wrap it with {@link HttpStatus}.BAD_REQUEST
     * @return error dto with message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDTO handle(MethodArgumentNotValidException exception, HttpServletResponse response) {
        log.warn("Validation problem", exception);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ErrorResponseDTO.builder().message(exception.getMessage()).build();
    }

    /**
     * Handle not found exceptions and wrap it with {@link HttpStatus}.NOT_FOUND
     * @return error dto with message
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponseDTO handle(NoSuchElementException exception, HttpServletResponse response) {
        log.warn("Not found problem", exception);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return ErrorResponseDTO.builder().message(exception.getMessage()).build();
    }

    /**
     * Handle article already exists exceptions and wrap it with {@link HttpStatus}.CONFLICT
     * @return error dto with message
     */
    @ExceptionHandler(ArticleAlreadyExistsException.class)
    public ErrorResponseDTO handle(ArticleAlreadyExistsException exception, HttpServletResponse response) {
        log.warn("Such an article is exists: ", exception);
        response.setStatus(HttpStatus.CONFLICT.value());
        return ErrorResponseDTO.builder().message(exception.getMessage()).build();
    }

    /**
     * Handle any other exceptions except described above and wrap it with {@link HttpStatus}.INTERNAL_SERVER_ERROR
     * @return error dto without message
     */
    @ExceptionHandler(Exception.class)
    public ErrorResponseDTO handle(Exception exception, HttpServletResponse response) {
        log.warn("Internal error problem", exception);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ErrorResponseDTO.builder().build();
    }
}
