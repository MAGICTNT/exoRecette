package fr.maxime.dao;

import fr.maxime.entity.Categorie;
import fr.maxime.entity.Composer;
import fr.maxime.utils.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ComposerDAO extends BaseDAO<Composer> {
    private RecetteDAO recetteDAO;
    private IngredientDAO ingredientDAO;

    private ComposerDAO() {
        this.recetteDAO = new RecetteDAO();
        this.ingredientDAO = new IngredientDAO();
    }

    @Override
    public Composer save(Composer element) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "INSERT INTO composer (id_ingredient, id_recette) VALUES (?,?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1 , element.getRecette().getIdRecette());
            statement.setInt(1 , element.getIngredient().getIdIngredient());
            int nbrRows = statement.executeUpdate();
            if(nbrRows != 1){
                connection.rollback();
                return null;
            }

            resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                element.setIdComposer(resultSet.getInt(1));
            }

            connection.commit();
            return element;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection.rollback();
            return null;
        }finally {
            close();
        }
    }

    @Override
    public boolean delete(Composer element) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "DELETE FROM composer WHERE id_ingredient = ? AND id_recette = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, element.getIngredient().getIdIngredient());
            statement.setInt(2, element.getRecette().getIdRecette());
            int rowsDeleted = statement.executeUpdate();
            connection.commit();
            return rowsDeleted == 1;
        }catch (SQLException e){
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
    public Composer update(Composer element) throws SQLException {
        String query = "UPDATE composer SET id_ingredient = ?, id_recette = ? WHERE id_ingredient = ? AND id_recette = ?";
        try {
            connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, element.getIngredient().getIdIngredient());
            statement.setInt(2, element.getRecette().getIdRecette());
            statement.setInt(3, element.getIngredient().getIdIngredient());
            statement.setInt(4, element.getRecette().getIdRecette());

            int nbrRows = statement.executeUpdate();
            connection.commit();
            return element;
        }catch (SQLException e){
            System.out.println("erreur lors de la maj de la suppresion: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Composer get(int id) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM composer WHERE id_ingredient = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Composer.builder()
                        .idComposer(resultSet.getInt(1))
                        .recette(recetteDAO.get(resultSet.getInt("id_recette")))
                        .ingredient(ingredientDAO.get(resultSet.getInt("id_ingredient")))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Composer> get() throws SQLException {
        List<Composer> elementsList = new ArrayList<Composer>();
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM categorie";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                elementsList.add(
                        Composer.builder()
                                .idComposer(resultSet.getInt(1))
                                .recette(recetteDAO.get(resultSet.getInt("id_recette")))
                                .ingredient(ingredientDAO.get(resultSet.getInt("id_ingredient")))
                                .build()
                );
            }
            return elementsList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
