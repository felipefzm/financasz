package br.com.financasz.exceptions;

public class InvalidLoginException extends RuntimeException {

    public InvalidLoginException(String message) {
        super(message);
    }

    public InvalidLoginException() {
        super("Login ou senha inválidos");
    }

}
