package src;
import java.sql.*;

public class ConnectionFactory {
    
    public String URL = System.getenv("url");
    public String USER = System.getenv("USER");
    public String PASS = System.getenv("PASS");
    public Connection conn;
    public Statement stmt;
    public static ConnectionFactory connectionFactory;

    public ConnectionFactory(){}

    public static ConnectionFactory getInstance(){
        if (connectionFactory == null){
            connectionFactory = new ConnectionFactory();
        }
        return connectionFactory;
    }

    public Connection getConnection(){
        try{
            this.conn = DriverManager.getConnection(this.URL, this.USER, this.PASS);
            return this.conn;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return this.conn;
    }
}
