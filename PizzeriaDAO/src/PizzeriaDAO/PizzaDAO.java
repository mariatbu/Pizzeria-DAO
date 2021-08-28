package src.PizzeriaDAO;
import src.PizzeriaObjects.Pizza;
import src.PizzeriaObjects.Ingredient;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import src.ConnectionFactory;

public class PizzaDAO implements DAO<Pizza> {

    public Connection connection;
    public PreparedStatement stmt;

    public PizzaDAO() throws SQLException{
        this.connection = getConnection();
    }
    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        return conn;
    }

    public void insert(Pizza pizza) throws SQLException {

        this.connection = ConnectionFactory.getInstance().getConnection();

        String insertPizza= """
                            INSERT INTO pizza(id, name, url)
                            VALUES
                            (?,?,?);
                                    """;
        try{
            this.connection.setAutoCommit(false);

            UUID uuid = pizza.getId();
            byte[] pizzaId = UuidAdapter.getBytesFromUUID(uuid);
            
            this.stmt = this.connection.prepareStatement(insertPizza);
            stmt.setBytes(1, pizzaId);
            stmt.setString(2, pizza.getName());
            stmt.setString(3, pizza.getUrl());
            stmt.executeUpdate();
        
            for (Ingredient i: pizza.getIngredients()){
                UUID id = i.getId();
                byte[] ingredientId = UuidAdapter.getBytesFromUUID(id);
                String query1 = """
                                    INSERT INTO ingredient (id, name, price)
                                    VALUES
                                    (?,?,?);""";
                this.stmt = this.connection.prepareStatement(query1);
                stmt.setBytes(1, ingredientId);
                stmt.setString(2, i.getName());
                stmt.setDouble(3, i.getPrice());
                stmt.executeUpdate();

                String query2 = """
                                    INSERT INTO pizza_ingredient (id, id_pizza, id_ingredient)
                                    VALUES
                                    (UUID_TO_BIN(UUID()), ?,?);""";
                this.stmt = this.connection.prepareStatement(query2);
                stmt.setBytes(1, pizzaId);
                stmt.setBytes(2, ingredientId);
                stmt.executeUpdate();
            }

            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            this.connection.rollback();
        }

    }

    @Override
    public void update(Pizza pizza, String[] params) {

        this.connection = ConnectionFactory.getInstance().getConnection();

        String sql = """
                    UPDATE pizza
                    SET name = ?, url = ?
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);

            this.stmt = this.connection.prepareStatement(sql);
            //PROBLEMAS
            //1. los parámetros deben ser del mismo tipo y no tiene por qué serlo en la tabla
            //2. ¿Hay que actualizar el objeto aquí?
            UUID uuid = pizza.getId();
            byte[] pizzaId = UuidAdapter.getBytesFromUUID(uuid);
            stmt.setString(1, params[0]);
            stmt.setString(2, params[1]);
            stmt.setBytes(3, pizzaId);
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
                    DELETE FROM pizza
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);

            this.stmt = this.connection.prepareStatement(sql);
            byte[] pizzaId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(1, pizzaId);
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Pizza> select(UUID id) {

        this.connection = ConnectionFactory.getInstance().getConnection();

        ResultSet result = null;
        Pizza pizzaResult = null;

        String sql = """
                    SELECT id, name, url FROM pizza
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            byte[] pizzaId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(1, pizzaId);
            result = stmt.executeQuery();
            while (result.next()){
                pizzaResult = new Pizza(id, name, url);
                String name = result.getString("name");
                String url = result.getString("url");
                
                System.out.format("%s, %s\n", name, url);
            }
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        Optional<Pizza> oPizza = Optional.of(pizzaResult);
        return oPizza;
    }

    @Override
    public List<Pizza> selectAll(){

        this.connection = ConnectionFactory.getInstance().getConnection();

        ResultSet result = null;
        Pizza pizzaResult = null;
        List<Pizza> pizzasResult= new ArrayList<Pizza>();
        String sql = "SELECT id, name, url FROM pizza;";
        try{
            this.connection.setAutoCommit(false);

            this.stmt = this.connection.prepareStatement(sql);
            result = stmt.executeQuery();
            this.connection.commit();
            while (result.next()){
                byte[]  id = result.getBytes("id");
                String name = result.getString("name");
                String url = result.getString("url");
                System.out.format("%s, %s\n", id, name, url);
                UUID pizzaId = UuidAdapter.getUUIDFromBytes(id);
                pizzaResult = new Pizza(pizzaId, name, url);
                pizzasResult.add(pizzaResult);

        }
        this.connection.commit();
        this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return pizzasResult;
    }
    
}
