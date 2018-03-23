package leduyhung.me.animemoment.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import leduyhung.me.animemoment.db.dao.CategoryDao;
import leduyhung.me.animemoment.db.dao.CharacterDao;
import leduyhung.me.animemoment.db.dao.MediaDao;
import leduyhung.me.animemoment.module.category.data.CategoryInfo;
import leduyhung.me.animemoment.module.character.data.CharacterResponse;
import leduyhung.me.animemoment.module.media.data.MediaResponse;

@Database(entities = {CategoryInfo.class, CharacterResponse.class, MediaResponse.class}, version = DbConstants.DATABASE_VERSION, exportSchema = false)
public abstract class Appdatabase extends RoomDatabase {

    private static Appdatabase appDatabase;

    public abstract CategoryDao categoryDao();

    public abstract CharacterDao characterDao();

    public abstract MediaDao mediaDao();

    public static Appdatabase newInstance(Context ctx) {

        if (appDatabase == null) {

            appDatabase = Room.databaseBuilder(ctx, Appdatabase.class, DbConstants.DATABASE_NAME).allowMainThreadQueries().addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            }).build();
        }

        return appDatabase;
    }

    public static void destroyAppDatabase() {

        appDatabase = null;
    }
}