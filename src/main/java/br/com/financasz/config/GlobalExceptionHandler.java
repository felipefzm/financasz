package br.com.financasz.config;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.financasz.dtos.StandardErrorDTO;
import br.com.financasz.exceptions.AcessDeniedException;
import br.com.financasz.exceptions.CategoryAlreadyExistsException;
import br.com.financasz.exceptions.CategoryNotFoundException;
import br.com.financasz.exceptions.CategoryTypeDoesNotExistException;
import br.com.financasz.exceptions.InvalidLoginException;
import br.com.financasz.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<StandardErrorDTO> invalidLogin(InvalidLoginException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED; // 401
        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Erro de Autenticação",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> usernameNotFound(UserNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404
        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Usuário não Encontrado",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }


    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> categoryNotFound(CategoryNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404
        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Categoria não Encontrada",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(CategoryTypeDoesNotExistException.class)
    public ResponseEntity<StandardErrorDTO> categoryTypeDoesNotExist(CategoryTypeDoesNotExistException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400
        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Tipo de Categoria Inválido",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<StandardErrorDTO> categoryAlreadyExists(CategoryAlreadyExistsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT; // 409
        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Conflito de Categoria - Já existe uma categoria com esse nome para este usuário",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AcessDeniedException.class)
    public ResponseEntity<StandardErrorDTO> acessDenied(AcessDeniedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN; // 403
        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Acesso Negado",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }



}
