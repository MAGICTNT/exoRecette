package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Composer {
    private int idComposer;
    private Ingredient ingredient;
    private Recette recette;
}
