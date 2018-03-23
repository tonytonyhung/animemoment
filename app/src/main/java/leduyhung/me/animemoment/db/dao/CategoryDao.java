package leduyhung.me.animemoment.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import leduyhung.me.animemoment.db.DbConstants;
import leduyhung.me.animemoment.module.category.data.CategoryInfo;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryInfo... categoryInfos);

    @Query("SELECT * FROM " + DbConstants.TABLE_CATEGORY)
    List<CategoryInfo> getAllDataCategory();

    @Query("SELECT id FROM " + DbConstants.TABLE_CATEGORY + " WHERE save_date <= (:date)")
    List<Integer> getCategoryByTime(long date);

    @Query("DELETE FROM " + DbConstants.TABLE_CATEGORY)
    void deleteAllDataCategory();
}
