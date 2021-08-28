package src.PizzeriaDAO;
import java.sql.ResultSet;
import java.sql.*;
import src.ConnectionFactory;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import src.PizzeriaObjects.User;

public class UserDAO implements DAO<User>{

    public Connection connection;
    public PreparedStatement stmt;

    public UserDAO() throws SQLException{
        this.connection = getConnection();
    }
    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        return conn;
    }

    @Override
    public void insert(User user) {

        this.connection = ConnectionFactory.getInstance().getConnection();

        String insertUser= """
                            INSERT INTO user(id, name, lastName, email, password)
                            VALUES
                            (?,?,?,?,?);
                            """;
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(insertUser);
            UUID userId = user.getId();
            byte[] id = UuidAdapter.getBytesFromUUID(userId);

            stmt.setBytes(1, id);
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }        
    }

    @Override
    public void update(User user, String[] params) {
        String sql = """
                    UPDATE user
                    SET name = ?, lastName = ?, email = ?, password = ?
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            //PROBLEMAS
            //1. los parámetros deben ser del mismo tipo y no tiene por qué serlo en la tabla
            //2. ¿Hay que actualizar el objeto aquí?
            stmt.setString(1, params[0]);
            stmt.setString(2, params[1]);
            stmt.setString(3, params[2]);
            stmt.setString(4, params[3]);
            UUID id = user.getId();
            byte [] userId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(3, userId);
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
        //DELETE FROM table_name WHERE condition;
        String sql = """
                    DELETE FROM user
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            byte [] userId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(1, userId);
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }        
    }

    @Override
    public Optional<User> select(UUID id) {
        ResultSet result = null;
        User userResult = null;

        String sql = """
                    SELECT name, lastName, email, password FROM users
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            byte [] userId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(1, userId);
            result = stmt.executeQuery();
            while (result.next()){
                String name = result.getString("name");
                String surname = result.getString("lastName");
                String email = result.getString("email");
                String password = result.getString("password");
                userResult = new User(id, name, surname, email, password);
                System.out.format("%s, %s\n", name, surname, email, password);

            }
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        Optional<User> oUser = Optional.of(userResult);
        return oUser;
    }

    @Override
    public List<User> selectAll() {
        ResultSet result = null;
        User userResult = null;
        List<User> usersResult= new ArrayList<User>();
        String sql = "SELECT id, name, lastName, email, password FROM users;";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            result = stmt.executeQuery();
            while (result.next()){
                byte[]  id = result.getBytes("id");
                String name = result.getString("name");
                String surname = result.getString("lastName");
                String email = result.getString("email");
                String password = result.getString("password");
                UUID userId = UuidAdapter.getUUIDFromBytes(id);
                userResult = new User(userId, name, surname, email, password);
                usersResult.add(userResult);

                System.out.format("%s, %s\n", name, surname, email, password);
        }
        this.connection.commit();
        this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return usersResult;
    }
    
}
