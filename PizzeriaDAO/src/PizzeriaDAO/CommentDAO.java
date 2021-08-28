package src.PizzeriaDAO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import src.ConnectionFactory;
import src.PizzeriaObjects.Comment;


public class CommentDAO implements DAO<Comment>{

    public Connection connection;
    public PreparedStatement stmt;

    public CommentDAO() throws SQLException{
        this.connection = getConnection();
    }
    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        return conn;
    }

    @Override
    public void insert(Comment comment) {
        this.connection = ConnectionFactory.getInstance().getConnection();

        String insertComment= """
                            INSERT INTO comment(id, text, rate, date, id_user, id_pizza)
                            VALUES
                            (?,?,?,?,?,?);
                                         """; 
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(insertComment);
            UUID uuid = comment.getId();
            byte[] commentId = UuidAdapter.getBytesFromUUID(uuid);
            stmt.setBytes(1, commentId);
            stmt.setString(2, comment.getText());
            stmt.setDouble(3, comment.getMark());
            stmt.setDate(4, (Date)comment.getDateTime());
            stmt.setBytes(5, UuidAdapter.getBytesFromUUID(comment.getUserId()));
            stmt.setBytes(6, UuidAdapter.getBytesFromUUID(comment.getPizzaId()));
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
    }

    @Override
    public void update(Comment comment, String []text) {
        this.connection = ConnectionFactory.getInstance().getConnection();
        
        String sql = """
                        UPDATE comment
                        SET text = ?
                        WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);

            this.stmt = this.connection.prepareStatement(sql);
            //PROBLEMAS
            //1. los parámetros deben ser del mismo tipo y no tiene por qué serlo en la tabla
            //2. ¿Hay que actualizar el objeto aquí?
            UUID uuid = comment.getId();
            byte[] commentId = UuidAdapter.getBytesFromUUID(uuid);
            stmt.setString(1, text[0]);
            stmt.setBytes(2, commentId);
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = """
                    DELETE FROM comment
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);

            this.stmt = this.connection.prepareStatement(sql);
            byte[] commentId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(1, commentId);
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Comment> select(UUID id) {
        ResultSet result = null;
        Comment commentResult = null;
        String sql = """
                        SELECT id, text, rate, date, id_user, id_pizza FROM comment
                        WHERE id = ?;""";
        try{
            this.stmt = this.connection.prepareStatement(sql);
            byte[] commentId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(1, commentId);
            stmt.executeQuery();
            while (result.next()){
                byte[] idC = result.getBytes("id");
                String text = result.getString("text");
                Double score = result.getDouble("rate");
                Date dateTime = result.getDate("date");
                byte [] userId = result.getBytes("id_user");
                byte [] pizzaId = result.getBytes("id_pizza");
                UUID idComment = UuidAdapter.getUUIDFromBytes(idC);
                UUID idUser = UuidAdapter.getUUIDFromBytes(userId);
                UUID idPizza = UuidAdapter.getUUIDFromBytes(pizzaId);
                commentResult = new Comment(idComment, text, score, idUser, idPizza);
                System.out.format("%s, %s\n", id, text, score, dateTime, userId, pizzaId);
                
            }
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        Optional<Comment> oComment = Optional.of(commentResult);
        return oComment;
    }
    

    @Override
    public List<Comment> selectAll() {
        ResultSet result = null;
        Comment commentResult = null;
        List<Comment> commentsResult= new ArrayList<Comment>();
        String sql = "SELECT text, score, date, id_user, id_pizza FROM comment;";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            result = stmt.executeQuery();
            while (result.next()){
                byte[] id = result.getBytes("id");
                String text = result.getString("text");
                Double score = result.getDouble("rate");
                Date dateTime = result.getDate("date");
                byte[] userId = result.getBytes("id_user");
                byte[] pizzaId = result.getBytes("id_pizza");
                UUID idComment = UuidAdapter.getUUIDFromBytes(id);
                UUID idUser = UuidAdapter.getUUIDFromBytes(userId);
                UUID idPizza = UuidAdapter.getUUIDFromBytes(pizzaId);
                commentResult = new Comment(idComment, text, score, idUser, idPizza);
                commentsResult.add(commentResult);
                System.out.format("%s, %s\n", id, text, score, dateTime, userId, pizzaId);
        }
         this.connection.commit();
         this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return commentsResult;
    }
    
}
