package leduyhung.me.animemoment.module.media.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import leduyhung.me.animemoment.db.DbConstants;
import leduyhung.me.animemoment.db.converter.ConverterDate;
import leduyhung.me.animemoment.db.converter.ConverterListMediaInfo;


@Entity(tableName = DbConstants.TABLE_MEDIA)
public class MediaResponse {

    @Ignore
    public transient static final int TYPE_GALLERY = 0;
    @Ignore
    public transient static final int TYPE_CLIP = 1;

    @ColumnInfo(index = true)
    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters(ConverterListMediaInfo.class)
    private List<MediaInfo> data;
    private int total_item;
    private int total_page;
    private int current_page;
    private int user;
    private int character;
    private int type;
    @TypeConverters(ConverterDate.class)
    private Date save_date;

    public MediaResponse() {
    }

    public List<MediaInfo> getData() {
        return data;
    }

    public void setData(List<MediaInfo> data) {
        this.data = data;
    }

    public int getTotal_item() {
        return total_item;
    }

    public void setTotal_item(int total_item) {
        this.total_item = total_item;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getSave_date() {
        return save_date;
    }

    public void setSave_date(Date save_date) {
        this.save_date = save_date;
    }
}