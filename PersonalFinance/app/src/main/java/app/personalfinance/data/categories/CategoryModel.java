package app.personalfinance.data.categories;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String type;

    public CategoryModel(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
