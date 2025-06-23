package br.org.catolicasc.category.DTO;

import br.org.catolicasc.category.model.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(@NotBlank String name){
}
