package leduyhung.me.animemoment.rest;

import java.util.List;

import leduyhung.me.animemoment.module.category.data.CategoryInfo;
import leduyhung.me.animemoment.module.character.data.CharacterInfo;
import leduyhung.me.animemoment.module.character.data.CharacterResponse;
import leduyhung.me.animemoment.module.media.data.MediaResponse;
import leduyhung.me.animemoment.module.user.User;
import leduyhung.me.animemoment.module.user.data.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskApi {

    @POST("/v1/auth")
    Call<UserInfo> login(@Header("version") int version, @Header("pakage") String pakage, @Body User user);

    @GET("/v1/categories")
    Call<List<CategoryInfo>> getAllCategory(@Header("version") int version, @Header("pakage") String pakage);

    @GET("/v1/category/{category_id}/characters")
    Call<CharacterResponse> getCharacterByPage(@Header("version") int version, @Header("pakage") String pakage, @Path("category_id") int category_id, @Query("page") int page, @Query("name") String name);

    @GET("/v1/user/{user_id}/favorite/characters")
    Call<CharacterResponse> getCharacterFavorite(@Header("version") int version, @Header("pakage") String pakage, @Query("page") int page, @Path("user_id") int user_id);

    @POST("/v1/user/{user_id}/favorite/characters/{character_id}")
    Call<Void> loveCharacter(@Header("version") int version, @Header("pakage") String pakage, @Path("user_id") int user_id, @Path("character_id") int character_id);

    @GET("/v1/character/{character_id}/media")
    Call<MediaResponse> getMediaByPage(@Header("version") int version, @Header("pakage") String pakage, @Path("character_id") int character_id,
                                       @Query("page") int page, @Query("type") int type);

    @GET("/v1/character/art/media")
    Call<MediaResponse> getMediaArtByPage(@Header("version") int version, @Header("pakage") String pakage, @Query("page") int page, @Query("type") int type);


    @POST("/v1/user/{user_id}/favorite/media/{media_id}")
    Call<Void> loveMedia(@Header("version") int version, @Header("pakage") String pakage, @Path("user_id") int user_id, @Path("media_id") int media_id);

    @GET("/v1/user/4/favorite/media")
    Call<MediaResponse> getMediaFavorite(@Header("version") int version, @Header("pakage") String pakage, @Query("page") int page, @Query("type") int type, @Path("user_id") int user_id);
}