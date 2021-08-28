package src.PizzeriaDAO;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DAO<T> {
        
    void insert(T t);
    
    void update(T t, String[] params);
    
    void delete(UUID id);

    Optional<T> select(UUID id); //List<T>

    List<T> selectAll();
}
