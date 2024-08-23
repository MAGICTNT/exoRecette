package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recette {
    private int idRecette;
    private String labelRecette;
    private int tempsPreparation;
    private int tempsCuisson;
    private String difficulte;
    private Categorie categorie;
}
