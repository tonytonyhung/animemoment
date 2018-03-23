package leduyhung.me.animemoment.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import leduyhung.me.animemoment.db.DbConstants;
import leduyhung.me.animemoment.module.character.data.CharacterResponse;

@Dao
public interface CharacterDao {

    @Query("SELECT * FROM " + DbConstants.TABLE_CHARACTER + " WHERE current_page IN (:page) AND categoryId IN (:categoryId) AND user IN (:user)")
    CharacterResponse getCharacterByPage(int page, int categoryId, int user);

    @Query("SELECT * FROM " + DbConstants.TABLE_CHARACTER + " WHERE current_page IN (:page) AND user IN (:user)")
    CharacterResponse getCharacterFavorite(int page, int user);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertCharacter(CharacterResponse... characterResponses);

    @Query("SELECT id FROM " + DbConstants.TABLE_CHARACTER + " WHERE save_date <= (:date)")
    List<Integer> getCharacterByTime(long date);

    @Query("DELETE FROM " + DbConstants.TABLE_CHARACTER)
    void deleteAllCharacter();

    @Query("DELETE FROM " + DbConstants.TABLE_CHARACTER + " WHERE user IN (:user)")
    void deleteCharacterFavorite(int user);
}