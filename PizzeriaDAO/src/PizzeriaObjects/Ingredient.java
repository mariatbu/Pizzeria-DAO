package src.PizzeriaObjects;

import java.util.*;

public class Ingredient extends Entity {
    private String name;
    private Double price;

    public Ingredient(){}
    public Ingredient(UUID id, String name, Double price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public Double getPrice(){
        return this.price;
    }

    @Override
    public void validate(){
        super.validate();
        try {
            if (this.price == 0) {
                throw new Exception("Invalid ingredient price.");
            }
            if (this.name == null || this.name.equals("")) {
                throw new Exception("Invalid ingredient name.");
            }
 
        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }
}
