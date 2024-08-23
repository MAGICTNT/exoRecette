package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ingredient {
    private int idIngredient;
    private String labelIngredient;
}
