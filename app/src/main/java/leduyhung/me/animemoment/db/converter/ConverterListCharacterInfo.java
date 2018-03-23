package leduyhung.me.animemoment.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import leduyhung.me.animemoment.module.character.data.CharacterInfo;
import leduyhung.me.animemoment.util.GsonUtil;

public class ConverterListCharacterInfo {

    @TypeConverter
    public static List<CharacterInfo> fromString(String value) {
        Type listType = new TypeToken<List<CharacterInfo>>() {}.getType();
        return GsonUtil.newInstance().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<CharacterInfo> list) {
        String json = GsonUtil.newInstance().toJson(list);
        return json;
    }
}