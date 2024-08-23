package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Etape {
    private int idEtape;
    private String descriptionEtape;
    private Recette recette;

}
