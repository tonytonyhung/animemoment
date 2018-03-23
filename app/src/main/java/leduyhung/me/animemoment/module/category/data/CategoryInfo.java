package leduyhung.me.animemoment.module.category.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import leduyhung.me.animemoment.db.DbConstants;
import leduyhung.me.animemoment.db.converter.ConverterDate;

@Entity(tableName = DbConstants.TABLE_CATEGORY, primaryKeys = {"id"})
public class CategoryInfo{

    private int id;
    @TypeConverters(ConverterDate.class)
    private Date create_date;
    private String name;
    private String description;
    @TypeConverters(ConverterDate.class)
    private Date save_date;
    private int type;

    public CategoryInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSave_date() {
        return save_date;
    }

    public void setSave_date(Date save_date) {
        this.save_date = save_date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}