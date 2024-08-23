package fr.maxime.dao;

import fr.maxime.entity.Categorie;
import fr.maxime.entity.Ingredient;
import fr.maxime.utils.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO extends BaseDAO<Ingredient> {

    @Override
    public Ingredient save(Ingredient element) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "INSERT INTO ingredient (label_ingredient) VALUES (?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, element.getLabelIngredient());
            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                element.setIdIngredient(resultSet.getInt(1));
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
    public boolean delete(Ingredient element) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "DELETE FROM ingredient WHERE id_ingredient = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, element.getIdIngredient());
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
    public Ingredient update(Ingredient element) throws SQLException {
        String query = "UPDATE ingredient SET label_ingredient = ? WHERE id_ingredient = ?";
        try {
            connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, element.getLabelIngredient());
            statement.setLong(2, element.getIdIngredient());

            int nbrRows = statement.executeUpdate();
            connection.commit();
            element.setLabelIngredient(element.getLabelIngredient());
            return element;
        } catch (SQLException e) {
            System.out.println("erreur lors de la maj de l'ingredient: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Ingredient get(int id) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM ingredient WHERE id_ingredient = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Ingredient.builder()
                        .idIngredient(resultSet.getInt("id_ingredient"))
                        .labelIngredient(resultSet.getString("label_ingredient"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ingredient> get() throws SQLException {
        List<Ingredient> elementsList = new ArrayList<Ingredient>();
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM categorie";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                elementsList.add(
                        Ingredient.builder()
                                .idIngredient(resultSet.getInt("id_ingredient"))
                                .labelIngredient(resultSet.getString("label_ingredient"))
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
