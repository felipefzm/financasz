package br.com.financasz.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException() {
        super("Categoria não encontrada");
    }

}
