package leduyhung.me.animemoment.db.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import leduyhung.me.animemoment.module.media.data.MediaInfo;
import leduyhung.me.animemoment.util.GsonUtil;

public class ConverterListMediaInfo {

    @TypeConverter
    public static List<MediaInfo> fromString(String value) {
        Type listType = new TypeToken<List<MediaInfo>>() {}.getType();
        return GsonUtil.newInstance().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<MediaInfo> list) {
        String json = GsonUtil.newInstance().toJson(list);
        return json;
    }
}
