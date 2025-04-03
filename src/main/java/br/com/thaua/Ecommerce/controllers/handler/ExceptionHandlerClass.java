package br.com.thaua.Ecommerce.controllers.handler;

import br.com.thaua.Ecommerce.exceptions.AddressException;
import br.com.thaua.Ecommerce.exceptions.ErrorResponse;
import jdk.jshell.Snippet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(AddressException.class)
    public ResponseEntity<ErrorResponse> addressException(AddressException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()).fieldsErrors(null).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
