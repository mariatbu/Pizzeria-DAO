package src.PizzeriaObjects;

import java.util.*;
import java.io.*;

public class Pizza extends Entity{
    private String name;
    private String url;
    private final Set<Ingredient> ingredients = new HashSet<Ingredient>(); //Debería ser un HashSet de UUID
    private double price;
    private final Set<Comment> comments = new HashSet<Comment>();

    public Pizza(){}
    public Pizza(UUID id, String name, String url){
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }
    public void removeIngredient(Ingredient ingredient){
        ingredients.remove(ingredient);
    }
    public Set<Ingredient> getIngredients(){
        return this.ingredients;
    }
    public Double getPrice(){
        for(Ingredient ing: ingredients){
            this.price += ing.getPrice();
        }
        this.price = 1.2*this.price;
        return this.price;
    }
    public void setComment(Comment comment){
        comments.add(comment);
    }
    public void removeComment(Comment comment){
        comments.remove(comment);
    }
    public Set<Comment> getComment(){
        return this.comments;
    }

    @Override
    public void validate(){
        super.validate();
        try {
            if (this.name == null || this.name.equals("")) {
                throw new Exception("Name required.");
            }
            if (this.url == null || this.url.equals("")) {
                throw new Exception("URL required.");
            }
            if (this.price == 0){
                throw new Exception ("Price required.");
            }
 
        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }
}
