package br.com.financasz.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFilter {
    private Long id;
    private String name;
    private String type;
    private Boolean active;
    private String userId;
}
