package leduyhung.me.animemoment.db;

import android.content.Context;
import android.os.Bundle;

import com.leduyhung.loglibrary.Logg;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import leduyhung.me.animemoment.module.category.data.CategoryInfo;
import leduyhung.me.animemoment.module.character.data.CharacterResponse;
import leduyhung.me.animemoment.module.media.data.MediaResponse;

public class CacheDatabaseManage {

    public static final String TAG_CATEGORY = "TAG_CATEGORY";
    public static final String TAG_CHARACTER = "TAG_CHARACTER";
    public static final String TAG_MEDIA = "TAG_MEDIA";

    private static CacheDatabaseManage cache;
    private Context mContext;

    public static CacheDatabaseManage newInstance() {

        if (cache == null) {

            cache = new CacheDatabaseManage();
        }
        return cache;
    }

    public CacheDatabaseManage addContext(Context mContext) {

        this.mContext = mContext;
        return cache;
    }

    public void save(String tagTable, Object data, int... i) {

        switch (tagTable) {

            case TAG_CATEGORY:
                saveCategory((CategoryInfo) data);
                break;
            case TAG_CHARACTER:
                saveCharacter((CharacterResponse) data, i[0]);
                break;
            case TAG_MEDIA:
                saveMedia((MediaResponse) data, i[0], i[1]);
                break;
        }
    }

    public boolean check(String tagTable) {

        boolean result = false;
        switch (tagTable) {

            case TAG_CATEGORY:
                result = checkCacheCategory();
                break;
            case TAG_CHARACTER:
                result = checkCharacter();
                break;
            case TAG_MEDIA:
                break;
        }
        return result;
    }

    private void saveCategory(CategoryInfo categoryInfo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 4);
        categoryInfo.setSave_date(c.getTime());
        Appdatabase.newInstance(mContext).categoryDao().insertCategory(categoryInfo);
    }

    private void saveCharacter(CharacterResponse character, int categoryId) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 4);
        character.setSave_date(c.getTime());
        character.setCategoryId(categoryId);
        Appdatabase.newInstance(mContext).characterDao().insertCharacter(character);
    }

    private void saveMedia(MediaResponse media, int characterId, int type) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 4);
        media.setSave_date(c.getTime());
        media.setCharacter(characterId);
        media.setType(type);
        Appdatabase.newInstance(mContext).mediaDao().insertMedia(media);
    }

    private boolean checkCacheCategory() {

        Calendar c = Calendar.getInstance();
        List<Integer> l = Appdatabase.newInstance(mContext).categoryDao().getCategoryByTime(c.getTime().getTime());
        if (l != null && l.size() > 0) {

            Appdatabase.newInstance(mContext).categoryDao().deleteAllDataCategory();
            Logg.error(getClass(), TAG_CATEGORY + ": category is expired. it will be delete");
            return true;
        }
        return false;
    }

    private boolean checkCharacter() {

        Calendar c = Calendar.getInstance();
        List<Integer> l = Appdatabase.newInstance(mContext).characterDao().getCharacterByTime(c.getTime().getTime());
        if (l != null && l.size() > 0) {

            Appdatabase.newInstance(mContext).characterDao().deleteAllCharacter();
            Logg.error(getClass(), TAG_CHARACTER + ": character is expired. it will be delete");
            return true;
        }
        return false;
    }
}
