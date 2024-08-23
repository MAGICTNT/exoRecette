package fr.maxime.dao;

import fr.maxime.entity.Categorie;
import fr.maxime.entity.Commentaire;
import fr.maxime.utils.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommentaireDAO extends BaseDAO<Commentaire> {
    private RecetteDAO recetteDAO;
//
public CommentaireDAO(){
        this.recetteDAO = new RecetteDAO();
    }
    @Override
    public Commentaire save(Commentaire element) throws SQLException {
        if (recetteDAO.get(element.getRecette().getIdRecette()) == null) {
            return null;
        }
        try {
            connection = DatabaseManager.getConnection();
            query = "INSERT INTO commentaire (description_commentaire, id_recette) values(?,?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, element.getLabelCommentaire());
            statement.setInt(2, element.getRecette().getIdRecette());
            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                element.setIdCommentaire(resultSet.getInt(1));
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
    public boolean delete(Commentaire element) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "DELETE FROM commentaire WHERE id_commentaire = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, element.getIdCommentaire());
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
    public Commentaire update(Commentaire element) throws SQLException {
        String query = "UPDATE commentaire SET description_commentaire = ? WHERE id_commentaire = ?";
        try {
            connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, element.getLabelCommentaire());
            statement.setLong(2, element.getIdCommentaire());

            int nbrRows = statement.executeUpdate();
            connection.commit();
            element.setLabelCommentaire(element.getLabelCommentaire());
            return element;
        }catch (SQLException e){
            System.out.println("erreur lors de la maj du commentaire: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Commentaire get(int id) throws SQLException {
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM commentaire WHERE id_commentaire = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Commentaire.builder()
                        .idCommentaire(resultSet.getInt("id_commentaire"))
                        .labelCommentaire(resultSet.getString("description_commentaire"))
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
    public List<Commentaire> get() throws SQLException {
        List<Commentaire> elementsList = new ArrayList<Commentaire>();
        try {
            connection = DatabaseManager.getConnection();
            query = "SELECT * FROM commentaire";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                elementsList.add(
                        Commentaire.builder()
                                .idCommentaire(resultSet.getInt("id_commentaire"))
                                .labelCommentaire(resultSet.getString("description_commentaire"))
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

    public boolean deleteByIdRecette(int idRecette) throws SQLException {
        try{
            connection = DatabaseManager.getConnection();
            query = "DELETE FROM commentaire WHERE id_recette = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, idRecette);
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
}
