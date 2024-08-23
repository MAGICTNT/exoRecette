package fr.maxime.dao;

import fr.maxime.entity.Categorie;
import fr.maxime.entity.Etape;
import fr.maxime.utils.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EtapeDAO extends BaseDAO<Etape> {
    private RecetteDAO recetteDAO;

    private EtapeDAO(){
        this.recetteDAO = new RecetteDAO();
    }

    @Override
    public Etape save(Etape element) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "INSERT INTO etape (description_etape, id_recette) VALUES (?,?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1 , element.getDescriptionEtape());
            statement.setInt(2, element.getRecette().getIdRecette());
            int nbrRows = statement.executeUpdate();
            if(nbrRows != 1){
                connection.rollback();
                return null;
            }

            resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                element.setIdEtape(resultSet.getInt(1));
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
    public boolean delete(Etape element) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "DELETE FROM etape WHERE id_etape = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, element.getIdEtape());
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
    public Etape update(Etape element) throws SQLException {
        String query = "UPDATE etape SET description_etape = ?, id_recette = ? WHERE id_etape = ?";
        try {
            connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, element.getDescriptionEtape());
            statement.setInt(2, element.getRecette().getIdRecette());
            statement.setInt(2, element.getIdEtape());

            int nbrRows = statement.executeUpdate();
            connection.commit();

            return element;
        }catch (SQLException e){
            System.out.println("erreur lors de la maj de la categorie: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Etape get(int id) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM etape WHERE id_etape = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Etape.builder()
                        .idEtape(resultSet.getInt("id_etape"))
                        .descriptionEtape(resultSet.getString("description_etape"))
                        .recette(recetteDAO.get(resultSet.getInt("id_recette")))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Etape> get() throws SQLException {
        List<Etape> elementsList = new ArrayList<Etape>();
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM etape";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                elementsList.add(
                        Etape.builder()
                                .idEtape(resultSet.getInt("id_etape"))
                                .descriptionEtape(resultSet.getString("description_etape"))
                                .recette(recetteDAO.get(resultSet.getInt("id_recette")))
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
