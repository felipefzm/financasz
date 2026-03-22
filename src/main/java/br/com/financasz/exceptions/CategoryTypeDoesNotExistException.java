package br.com.financasz.exceptions;

public class CategoryTypeDoesNotExistException extends RuntimeException {

    public CategoryTypeDoesNotExistException(String message) {
        super(message);
    }

    public CategoryTypeDoesNotExistException() {
        super("Tipo de categoria informada inválida. Os tipos válidos são: INCOME ou EXPENSE.");
    }
}
