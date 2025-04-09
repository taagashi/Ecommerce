package br.com.thaua.Ecommerce.controllers.handler;

import br.com.thaua.Ecommerce.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerClass {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> clienteException(ClienteException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex.getMessage(), ex.getFields());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> addressException(AddressException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex.getMessage(), ex.getFields());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> produtoException(ProdutoException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex.getMessage(), ex.getFields());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = createErrorResponse(ex.getMessage(), ex.getFields());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException ex) {
        ErrorResponse errorResponse = createErrorResponse("Erro de validação", ConstructorErrors.returnMapErrors());

        for(ConstraintViolation violation : ex.getConstraintViolations()) {
                    errorResponse.getFieldsErrors().put("Erro de validação: ", violation.getMessage());
        }

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
