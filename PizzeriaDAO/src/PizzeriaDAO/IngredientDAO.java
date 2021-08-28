package src.PizzeriaDAO;
import src.PizzeriaObjects.Ingredient;
import java.sql.*;
import java.util.UUID;
import src.ConnectionFactory;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO implements DAO<Ingredient> {

    public Connection connection;
    public PreparedStatement stmt;

    public IngredientDAO() throws SQLException{
        this.connection = getConnection();
    }
    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        return conn;
    }

    @Override
    public void insert(Ingredient ingredient) {
        this.connection = ConnectionFactory.getInstance().getConnection();
        
        String sql = """
                        INSERT INTO Ingredient (id, name, price)
                        VALUES (?,?,?); """;
        try{
            this.connection.setAutoCommit(false);

            this.stmt = this.connection.prepareStatement(sql);
            UUID uuid = ingredient.getId();
            byte[] ingredientId = UuidAdapter.getBytesFromUUID(uuid);
            stmt.setBytes(1, ingredientId);
            stmt.setString(2, ingredient.getName());
            stmt.setDouble(3, ingredient.getPrice());
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }        
    }

    @Override
    public void update(Ingredient ingredient, String[] params) {
        String sql = """
                        UPDATE ingredient
                        SET ingredientname = ?, price = ?
                        WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            UUID uuid = ingredient.getId();
            byte[] ingredientId = UuidAdapter.getBytesFromUUID(uuid);
            stmt.setString(1, params[0]);
            stmt.setString(2, params[1]);
            stmt.setBytes(3, ingredientId);
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
        String sql = """
                    DELETE FROM Ingredient
                    WHERE id = ?;""";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            byte[] ingredientId = UuidAdapter.getBytesFromUUID(id);
            stmt.setBytes(1, ingredientId);
            stmt.executeUpdate();
            this.connection.commit();
            this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }        
    }

    @Override
    public Optional<Ingredient> select(UUID id) {
        ResultSet result = null;
        Ingredient ingredientResult = null;

        String sql = """
                    SELECT name, price FROM ingredient
                    WHERE id = ?;""";
        try{
            this.stmt = this.connection.prepareStatement(sql);
            UUID uuid = id;
            byte[] ingredientId = UuidAdapter.getBytesFromUUID(uuid);
            stmt.setBytes(1, ingredientId);
            result = stmt.executeQuery();
           // result.next();
            while (result.next()){
                String name = result.getString("name");
                Double price = result.getDouble("price");
                ingredientResult = new Ingredient(uuid, name, price);
                System.out.format("%s, %s\n", name, price);
            }
            stmt.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        Optional<Ingredient> oIngredient = Optional.of(ingredientResult);
        return oIngredient;
    }
    @Override
    public List<Ingredient> selectAll(){
        ResultSet result = null;
        Ingredient ingredientResult = null;
        List<Ingredient> ingredientsResult = new ArrayList<Ingredient>();
        String sql = "SELECT id, name, price FROM pizzas;";
        try{
            this.connection.setAutoCommit(false);
            this.stmt = this.connection.prepareStatement(sql);
            result = stmt.executeQuery();
            while (result.next()){
                byte[]  id = result.getBytes("id");
                String name = result.getString("name");
                Double price = result.getDouble("price");
                System.out.format("%s, %s\n", name, price);
                UUID ingredientId = UuidAdapter.getUUIDFromBytes(id);
                ingredientResult = new Ingredient(ingredientId, name, price);
                ingredientsResult.add(ingredientResult);
        }
        this.connection.commit();
        this.connection.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return ingredientsResult;
    }
}
