package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Categorie {
    private int idCategorie;
    private String labelCategorie;
}
