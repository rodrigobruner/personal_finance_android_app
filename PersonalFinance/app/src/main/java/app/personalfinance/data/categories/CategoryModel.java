package app.personalfinance.data.categories;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories") //Table name
public class CategoryModel {
    //PK id
    @PrimaryKey(autoGenerate = true)
    private int id;
    //Category name
    private String name;
    //Category type
    private String type;

    //Constructor
    public CategoryModel(String name, String type) {
        this.name = name;
        this.type = type;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
