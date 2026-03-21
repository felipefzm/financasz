package br.com.financasz.dtos;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StandardErrorDTO {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
