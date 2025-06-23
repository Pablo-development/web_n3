package br.org.catolicasc.Exception.global;

import br.org.catolicasc.product.exception.NoStockException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(NoStockException.class)
    public ResponseEntity<String> handleNoStockException(NoStockException ex) {
        String body = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
