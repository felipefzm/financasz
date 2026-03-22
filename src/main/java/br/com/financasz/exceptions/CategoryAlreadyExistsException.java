package br.com.financasz.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {
    
      public CategoryAlreadyExistsException(String message) {
        super(message);
    }

    public CategoryAlreadyExistsException() {
        super("Categoria já existe!");
    }
}
