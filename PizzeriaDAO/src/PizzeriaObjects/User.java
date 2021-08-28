package src.PizzeriaObjects;

import java.util.*;

public class User extends Entity {
    private String email; //no tiene sentido ponerlos públicos seter y geter
    private String name;
    private String surname;
    private String password;

    public User(){}
    public User (UUID id, String name, String surname, String email, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setSurname(String surname){
        this.surname = surname;
    }
    public String getSurname(){
        return this.surname;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

    @Override
    public void validate(){
        super.validate();
        try {
            if (this.name == null || this.name.equals("")) {
                throw new Exception("Invalid user name.");
            }
            if (this.password == null || this.password.equals("")) {
                throw new Exception("Password required.");
            }
 
        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }
}
