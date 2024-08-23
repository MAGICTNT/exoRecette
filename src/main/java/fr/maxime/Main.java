package fr.maxime;

import fr.maxime.dao.*;
import fr.maxime.entity.Categorie;
import fr.maxime.entity.Recette;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static  final CategorieDAO categorieDAO = new CategorieDAO();
    private static final CommentaireDAO commentaireDAO = new CommentaireDAO();
    private static final ComposerDAO composerDAO = new ComposerDAO();
    private static final EtapeDAO etapeDAO = new EtapeDAO();
    private static final IngredientDAO ingredientDAO = new IngredientDAO();
    private static final RecetteDAO recetteDAO = new RecetteDAO();
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

       boolean flag = true;
       while (flag) {
           System.out.println("----- menu -----");
           System.out.println("1 - Recette");
           System.out.println("2 - Ingredient");
           System.out.println("3 - stoper");

           int option = scanner.nextInt();
           switch (option) {
               case 1:
                   ihmRecette(scanner);
                   break;
               case 2:
                   ihmIngredient(scanner);
                   break;
               case 3:
                   flag = false;
                   break;
               default:
                   System.out.println("choix non pris en charge");
                   break;
           }
       }
    }

    private static void ihmRecette(Scanner scanner) throws SQLException {
        boolean flag = true;
        while (flag) {
            System.out.println("----- menu recette -----");
            System.out.println("1 - Voir tout les recette ");
            System.out.println("2 - créé une recette ");
            System.out.println("3 - mettre a jour une recette ");
            System.out.println("4 - supprimer une recette ");
            System.out.println("5 - retour ");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    allRecette();
                    break;
                case 2:
                    createRecette(scanner);
                    break;
                case 3:
                    updateRecette(scanner);
                    break;
                case 4:
                    deleteRecette(scanner);
                    break;
                case 5:
                    flag = !flag;
                    break;
                default:
                    System.out.println("choix non pris en charge");
                    break;
            }


        }
    }

    private static void allRecette() throws SQLException {
        List<Recette> recetteList = recetteDAO.get();
        if(recetteList.isEmpty()){
            System.out.println("Vous n'avez pas les recettes");
        }else {
            System.out.println("--- voici la liste des recette ---");
            for (Recette recette : recetteList) {
                System.out.println(recette);
            }
        }
    }

    private static void createRecette(Scanner scanner) throws SQLException {
        List<Categorie> listCategorie = categorieDAO.get();
        if (listCategorie.isEmpty()){
            System.out.println("aucun categorie existe, voulez vous en créé une ");
            System.out.println("1 - oui");
            System.out.println("2 - non");
            int option = scanner.nextInt();
            if (option == 1) {
                createCategorie(scanner);
            }
        }else{
            boolean flag = true;
                System.out.println("quelle et le nom de la recette ?");
                String nom = scanner.next();
                System.out.println("quelle et son niveau de difficulter");
                String difficulter = scanner.next();
                System.out.println("quelle est le temps de preparation");
                int temps = scanner.nextInt();
                scanner.nextLine();
                System.out.println("quelle et la duré de cuisson");
                int cuisson = scanner.nextInt();
                scanner.nextLine();
                System.out.println("quelle catégorie de recette est elle (selection de l'id");
                System.out.println("id - label");
                for (Categorie categorie : listCategorie) {
                    System.out.println(categorie.getIdCategorie() + " - " + categorie.getLabelCategorie());
                }
                int categorieId = scanner.nextInt();
                scanner.nextLine();
                Recette newRecette = recetteDAO.update(
                        Recette.builder()
                                .labelRecette(nom)
                                .difficulte(difficulter)
                                .tempsPreparation(temps)
                                .tempsCuisson(cuisson)
                                .categorie(Categorie.builder()
                                        .idCategorie(categorieId)
                                        .build())
                                .build());
                System.out.println("vous avez bien créé votre recette");
                System.out.println(newRecette);
        }

    }



    private static void updateRecette(Scanner scanner) throws SQLException {
        List<Recette> recetteList = recetteDAO.get();
        boolean choix = false;
        if(recetteList.isEmpty()) {
            System.out.println("aucun recette trouver");
        }else {
            System.out.println("quelle recette voulez vous update (selectionner l'id)");
            for (Recette recette : recetteList) {
                System.out.println(recette);
            }

            int idRecette = scanner.nextInt();
            scanner.nextLine();

            Recette oldRecette = recetteDAO.get(idRecette);

            System.out.println("voulez vous changer son nom 1 - oui 2 - non");
            choix = scanner.nextInt() == 1;
            scanner.nextLine();
            if (choix) {
                System.out.println("quelle et le nouveau nom : (nom actuelle : " + oldRecette.getLabelRecette() + " )");
                oldRecette.setLabelRecette(scanner.nextLine());
                choix = false;

            }
            System.out.println("voulez voux changer le temps de preparaton 1 - oui 2 - non");
            choix = scanner.nextInt() == 1;
            scanner.nextLine();
            if (choix) {
                System.out.println("quelle et le temps: (temps actuelle " + oldRecette.getTempsPreparation() +") ");

                oldRecette.setTempsPreparation(scanner.nextInt());
                scanner.nextLine();

                choix = false;

            }
            System.out.println("voulez voux changer le temps de cuisson 1 - oui 2 - non");
            choix = scanner.nextInt() == 1;
            scanner.nextLine();
            if (choix) {
                System.out.println("quelle et le temps: (temps actuelle " + oldRecette.getTempsCuisson() +") ");
                oldRecette.setTempsCuisson(scanner.nextInt());
                scanner.nextLine();

                choix = false;

            }
            System.out.println("voulez vous changer la difficulter 1 - oui 2 - non");
            choix = scanner.nextInt() == 1;
            scanner.nextLine();
            if (choix) {
                System.out.println("quelle et la nouvelle difficulter: (difficulter actuelle " + oldRecette.getDifficulte() +") ");
                oldRecette.setDifficulte(scanner.nextLine());
                choix = false;

            }
            System.out.println("voulez vous changer la categorie 1 - oui 2 - non");
            choix = scanner.nextInt() == 1;
            scanner.nextLine();
            if (choix) {
                System.out.println("quelle et la nouvelle categorie (categorie actuelle " + oldRecette.getCategorie().getLabelCategorie() +" ) ");
                List<Categorie> categorieList = categorieDAO.get();
                for (Categorie categorie : categorieList){
                    System.out.println(categorie);
                }
                oldRecette.setCategorie(categorieDAO.get(scanner.nextInt()));
                scanner.nextLine();
                choix = false;
            }
            System.out.println("confirmer vous le changement 1 - oui 2 - non");
            choix = scanner.nextInt() == 1;
            scanner.nextLine();
            if (choix) {
                Recette unpdateRecette = recetteDAO.update(oldRecette);
                if (unpdateRecette != null) {
                    System.out.println("recette update");
                }else{
                    System.out.println("probleme durant la mise a jour");
                }
            }else{
                System.out.println("recette abandonner");
            }
        }
    }

    private static void deleteRecette(Scanner scanner) throws SQLException {
        List<Recette> recetteList = recetteDAO.get();
        for (Recette recette : recetteList) {
            System.out.println(recette);
        }
        System.out.println("quelle recette a suprimer ? selection de son id");
        int id = scanner.nextInt();
        scanner.nextLine();
        Recette recette = recetteDAO.get(id);
        boolean deleteAllCommentaire = commentaireDAO.deleteByIdRecette(recette.getIdRecette());
        if(deleteAllCommentaire){
            boolean choix = recetteDAO.delete(recette);
            if (choix) {
                System.out.println("recette deleted");
            }else {
                System.out.println("probleme durant la supression");
            }
        }else {
            System.out.println("probleme durant la suppresion des commentaire de recette");
        }

    }

    private static void ihmIngredient(Scanner scanner) throws SQLException {
        boolean flag = true;
        while (flag) {}

    }

    private static void createCategorie(Scanner scanner) throws SQLException {
        System.out.println("quelle et le nom de la categorie : ");
        String nomDeLaCategorie = scanner.nextLine();
        Categorie newCategorie = categorieDAO.save(
                Categorie.builder()
                .labelCategorie(nomDeLaCategorie)
                .build()
        );
        if (newCategorie != null) {
            System.out.println("categorie create");
        }else {
            System.out.println("probleme durant la creation de la categorie ");
        }
    }

    private static boolean scannerChoix(int choix ) {
        return choix == 1;
    }
}