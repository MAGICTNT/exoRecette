package fr.maxime.dao;

import fr.maxime.entity.Categorie;
import fr.maxime.entity.Ingredient;
import fr.maxime.entity.Recette;
import fr.maxime.utils.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecetteDAO extends BaseDAO<Recette> {
    private CategorieDAO categorieDAO;

    protected RecetteDAO() {
        this.categorieDAO = new CategorieDAO();
    }

    @Override
    public Recette save(Recette element) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "INSERT INTO recette (label_recette,temps_preparation, temps_cuisson, difficulte, id_categorie) VALUES(?,?,?,?,?);";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, element.getLabelRecette());
            statement.setInt(2, element.getTempsPreparation());
            statement.setInt(3, element.getTempsCuisson());
            statement.setString(4, element.getDifficulte());
            statement.setInt(5, element.getCategorie().getIdCategorie());
            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                element.setIdRecette(resultSet.getInt(1));
            }

            connection.commit();
            return element;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection.rollback();
            return null;
        } finally {
            close();
        }
    }

    @Override
    public boolean delete(Recette element) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "DELETE FROM recette WHERE id_recette = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, element.getIdRecette());
            int rowsDeleted = statement.executeUpdate();
            connection.commit();
            return rowsDeleted == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public Recette update(Recette element) throws SQLException {
        if (get(element.getIdRecette()) == null) {
            return null;
        }
        String query = "UPDATE recette SET label_recette = ?,temps_preparation = ?, temps_cuisson = ?,difficulte = ? , id_recette = ?  WHERE id_recette = ?";
        try {

            connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, element.getLabelRecette());
            statement.setInt(2, element.getTempsPreparation());
            statement.setInt(3, element.getTempsCuisson());
            statement.setString(4, element.getDifficulte());
            statement.setInt(5, element.getCategorie().getIdCategorie());
            statement.setInt(6, element.getIdRecette());

            int nbrRows = statement.executeUpdate();
            connection.commit();
            return element;
        } catch (SQLException e) {
            System.out.println("erreur lors de la maj de la categorie: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Recette get(int id) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM recette WHERE id_recette = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Recette.builder()
                        .idRecette(resultSet.getInt("id_recette"))
                        .labelRecette(resultSet.getString("label_recette"))
                        .tempsPreparation(resultSet.getInt("temps_preparation"))
                        .tempsCuisson(resultSet.getInt("temps_cuisson"))
                        .difficulte(resultSet.getString("difficulte"))
                        .categorie(categorieDAO.get(resultSet.getInt("id_categorie")))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Recette> get() throws SQLException {
        List<Recette> elementsList = new ArrayList<Recette>();
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM recette";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                elementsList.add(
                        Recette.builder()
                                .idRecette(resultSet.getInt("id_recette"))
                                .labelRecette(resultSet.getString("label_recette"))
                                .tempsPreparation(resultSet.getInt("temps_preparation"))
                                .tempsCuisson(resultSet.getInt("temps_cuisson"))
                                .difficulte(resultSet.getString("difficulte"))
                                .categorie(categorieDAO.get(resultSet.getInt("id_categorie")))
                                .build()
                );
            }
            return elementsList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
