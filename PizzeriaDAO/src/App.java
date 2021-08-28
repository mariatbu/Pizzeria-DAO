package src;
import java.util.UUID;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.math.*;
import java.lang.*;
import src.PizzeriaDAO.*;
import src.PizzeriaObjects.*;

public class App {
    public static void main(String [] args){
        String URL = System.getenv("url");
        String USER = System.getenv("USER");
        String PASS = System.getenv("PASS");
        Connection conn;
        Statement stmt;

        try{
            conn = DriverManager.getConnection(URL, USER, PASS);
            stmt = conn.createStatement();
            System.out.println("conexión exitosa");


            String sql = "USE PIZZERIA;";
            stmt.executeUpdate(sql);

            PizzaDAO pizzasDAO = new PizzaDAO();
            UUID id_carbonara = UUID.randomUUID();
            UUID id_barbacoa = UUID.randomUUID();
            UUID id_peperoni = UUID.randomUUID();
            UUID id_margarita = UUID.randomUUID();
            Pizza carbonara = new Pizza(id_carbonara, "Carbonara", "carbonara.jpg");
            Pizza barbacoa = new Pizza(id_barbacoa, "Barbacoa", "barbacoa.jpg");
            Pizza peperoni = new Pizza(id_peperoni, "Peperoni", "peperoni.jpg");
            Pizza margarita = new Pizza(id_margarita, "Margarita", "margarita.jpg");

            //pizzasDAO.insert(barbacoa);
            //pizzasDAO.insert(peperoni);
            pizzasDAO.delete(peperoni.getId());
            List<Pizza> pizzasGuardadas = pizzasDAO.selectAll();
            for(Pizza pizza: pizzasGuardadas){
                System.out.println(pizza.getName());
            }
            }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            //TODO: Debería cerrar aquí la conexión
        }


    }
}
