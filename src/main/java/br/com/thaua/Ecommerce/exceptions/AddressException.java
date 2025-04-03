package br.com.thaua.Ecommerce.exceptions;

public class AddressException extends RuntimeException{
    public AddressException(String message, Throwable cause) {
        super(message, cause);
    }
}
