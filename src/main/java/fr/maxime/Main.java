package fr.maxime;

import fr.maxime.dao.CategorieDAO;
import fr.maxime.entity.Categorie;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException {
        CategorieDAO dao = new CategorieDAO();
        Scanner scanner = new Scanner(System.in);
        System.out.println("quelle et le label de la categorie ");
        String label = scanner.nextLine();
        Categorie categorie = dao.save(Categorie.builder().labelCategorie(label).build());
        System.out.println(categorie);
        System.out.println("-------");
        List<Categorie> categorieList = dao.get();
        for (Categorie c : categorieList) {
            System.out.println(c);
        }
        System.out.println("suprpesion id:");
        int id = scanner.nextInt();
        scanner.nextLine();
        boolean success = dao.delete(Categorie.builder().idCategorie(id).build());
        if (success) {
            System.out.println("suppression réussi");
        }else {
            System.out.println("suppresion echec");
        }
        System.out.println("update , quelle et l'la categorie a update:");
         categorieList = dao.get();
        for (Categorie c : categorieList) {
            System.out.println(c);
        }
        System.out.println("id: ");
        id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("quelle et le nouveau nom: ");
        label = scanner.nextLine();
        Categorie newCategorie = dao.update(Categorie.builder().idCategorie(id).labelCategorie(label).build());
        System.out.println("update de : " + newCategorie);

    }
}