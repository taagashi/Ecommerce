package br.com.thaua.Ecommerce.controllers.handler;

import br.com.thaua.Ecommerce.exceptions.AddressException;
import br.com.thaua.Ecommerce.exceptions.ErrorResponse;
import br.com.thaua.Ecommerce.exceptions.ProdutoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(AddressException.class)
    public ResponseEntity<ErrorResponse> addressException(AddressException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex.getMessage(), ex.getFields());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ProdutoException.class)
    public ResponseEntity<ErrorResponse> produtoException(ProdutoException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex.getMessage(), ex.getErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ErrorResponse createErrorResponse(String message, Map<String, String> fieldsErrors) {
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .fieldsErrors(fieldsErrors)
                .build();
    }
}
