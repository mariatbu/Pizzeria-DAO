package src.PizzeriaObjects;

import java.util.*;

public abstract class Entity {
    protected UUID id;

    public UUID getId(){
        return this.id;
    }

    public void setId(UUID id){
        this.id = id;
    }
    public void validate(){
        //TO DO: validar que el id es único
        try{
            if(this.id == null){
                throw new Exception ("Required id");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Entity)){
            return false;
        }
        Entity tmp = (Entity) o;
        return tmp.getId().equals(this.getId());
    }
}
