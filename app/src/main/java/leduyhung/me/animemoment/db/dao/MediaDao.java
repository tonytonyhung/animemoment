package leduyhung.me.animemoment.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import leduyhung.me.animemoment.db.DbConstants;
import leduyhung.me.animemoment.module.media.data.MediaResponse;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM " + DbConstants.TABLE_MEDIA + " WHERE character IN (:character) AND type IN (:type) AND user IN (:user)")
    List<MediaResponse> getMediaByCharacter(int character, int type, int user);

    @Query("SELECT * FROM " + DbConstants.TABLE_MEDIA + " WHERE current_page IN (:page) AND character IN (:character) AND type IN (:type) AND user IN (:user)")
    MediaResponse getMediaByPage(int page, int character, int type, int user);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertMedia(MediaResponse... mediaResponses);

    @Query("DELETE FROM " + DbConstants.TABLE_MEDIA)
    void deleteAllMedia();

    @Query("DELETE FROM " + DbConstants.TABLE_MEDIA + " WHERE user IN (:user)")
    void deleteMediaFavorite(int user);
}
