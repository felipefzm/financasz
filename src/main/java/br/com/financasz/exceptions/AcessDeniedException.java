package br.com.financasz.exceptions;

public class AcessDeniedException extends RuntimeException {

    public AcessDeniedException() {
        super("Acesso negado: você não tem permissão para acessar este recurso.");
    }

    public AcessDeniedException(String message) {
        super(message);
    }
    
}
