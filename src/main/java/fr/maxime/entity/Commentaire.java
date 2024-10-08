package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Commentaire {
    private int idCommentaire;
    private String labelCommentaire;
    private Recette recette;
}
