package src.PizzeriaObjects;

import java.util.*;

public class Comment extends Entity {
    private String text;
    private double mark;
    private final Date dateTime = new Date();
    private User user;
    private UUID userId;
    private Pizza pizza;
    private UUID pizzaId;

    public Comment(UUID id, String text, double mark, UUID userId, UUID pizzaId){
        this.id = id;
        this.text = text;
        this.mark = mark;
        this.userId = userId;
        this.pizzaId = pizzaId;
    }

    public String getText(){
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }
    public double getMark(){
        return this.mark;
    }
    public void setMark(Double mark){
        this.mark = mark;
    }
    public Date getDateTime(){
        return this.dateTime;
    }
    public UUID getUserId(){
        return this.userId;
    }
    public UUID getPizzaId(){
        return this.pizzaId;
    }

    @Override
    public void validate(){
        super.validate();
        try {
            if (this.text == null || this.text.equals("")) {
                throw new Exception("You must write to send a comment.");
            }
            if (this.user == null) {
                throw new Exception("You must login to write a comment.");
            }
 
        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }

}
