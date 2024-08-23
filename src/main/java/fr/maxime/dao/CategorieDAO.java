package fr.maxime.dao;

import fr.maxime.entity.Categorie;
import fr.maxime.utils.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO extends BaseDAO<Categorie> {

    @Override
    public Categorie save(Categorie element) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "INSERT INTO categorie (label_categorie) VALUES (?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1 , element.getLabelCategorie());
            int nbrRows = statement.executeUpdate();
            if(nbrRows != 1){
                connection.rollback();
                return null;
            }

            resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                element.setIdCategorie(resultSet.getInt(1));
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
    public boolean delete(Categorie element) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "DELETE FROM categorie WHERE id_categorie = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, element.getIdCategorie());
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
    public Categorie update(Categorie element) throws SQLException {
        String query = "UPDATE categorie SET label_categorie = ? WHERE id_categorie = ?";
        try {
            connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, element.getLabelCategorie());
            statement.setLong(2, element.getIdCategorie());

            int nbrRows = statement.executeUpdate();
            connection.commit();
            element.setLabelCategorie(element.getLabelCategorie());
            return element;
        }catch (SQLException e){
            System.out.println("erreur lors de la maj de la categorie: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Categorie get(int id) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM categorie WHERE id_categorie = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Categorie.builder()
                        .idCategorie(resultSet.getInt("id_categorie"))
                        .labelCategorie(resultSet.getString("label_categorie"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Categorie> get() throws SQLException {
        List<Categorie> elementsList = new ArrayList<Categorie>();
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM categorie";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                elementsList.add(
                        Categorie.builder()
                                .idCategorie(resultSet.getInt("id_categorie"))
                                .labelCategorie(resultSet.getString("label_categorie"))
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
