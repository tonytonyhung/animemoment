package leduyhung.me.animemoment.module.character;

import android.content.Context;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.db.Appdatabase;
import leduyhung.me.animemoment.db.CacheDatabaseManage;
import leduyhung.me.animemoment.module.character.data.CharacterResponse;
import leduyhung.me.animemoment.rest.BaseRestApi;
import leduyhung.me.animemoment.ui.character.list.ListCharacterFragment;
import leduyhung.me.animemoment.ui.character.list.MessageForListCharacterFragment;
import leduyhung.me.animemoment.util.ClientUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Character {

    private static Character character;
    private CharacterResponse characterResponse;
    private Context mContext;
    private int countTask;

    public static Character newInstance() {

        if (character == null) {

            character = new Character();
        }

        return character;
    }

    public Character() {

        countTask = 0;
    }

    public void addContext(Context ctx) {

        this.mContext = ctx;
    }

    public void getCharacter(int page, final int categoryId, final String name, final boolean needEvent) {

        countTask++;
        characterResponse = Appdatabase.newInstance(mContext).characterDao().getCharacterByPage(page, categoryId, 0);
        if (characterResponse != null && name == "" && !CacheDatabaseManage.newInstance().addContext(mContext).check(CacheDatabaseManage.TAG_CHARACTER)) {

            EventBus.getDefault().post(new MessageForListCharacterFragment(MessageForListCharacterFragment.CODE_LOAD_LIST_SUCCESS,
                    characterResponse, categoryId));
            countTask--;
            characterResponse = null;
        } else {

            BaseRestApi.request(mContext).getCharacterByPage(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), categoryId, page, name)
                    .enqueue(new Callback<CharacterResponse>() {
                        @Override
                        public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {

                            if (response.isSuccessful() && response.body().getData() != null && response.body().getData().size() > 0) {

                                if (name == "") {
                                    CacheDatabaseManage.newInstance().addContext(mContext).save(CacheDatabaseManage.TAG_CHARACTER, response.body(), categoryId);
                                }
                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForListCharacterFragment(MessageForListCharacterFragment.CODE_LOAD_LIST_SUCCESS,
                                            response.body(), categoryId));
                            } else {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForListCharacterFragment(name == "" ? MessageForListCharacterFragment.CODE_LOAD_LIST_FAIL :
                                            MessageForListCharacterFragment.CODE_SEARCH_LIST_FAIL,
                                            mContext.getResources().getString(R.string.no_data_found), categoryId));
                            }

                            countTask--;
                        }

                        @Override
                        public void onFailure(Call<CharacterResponse> call, Throwable t) {

                            if (!ClientUtil.isConnectInternet(mContext)) {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForListCharacterFragment(name == "" ? MessageForListCharacterFragment.CODE_LOAD_LIST_FAIL :
                                            MessageForListCharacterFragment.CODE_SEARCH_LIST_FAIL,
                                            mContext.getResources().getString(R.string.no_connect_internet), categoryId));
                            } else {

                                if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForListCharacterFragment(name == "" ? MessageForListCharacterFragment.CODE_LOAD_LIST_FAIL :
                                                MessageForListCharacterFragment.CODE_SEARCH_LIST_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain), categoryId));
                                } else {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForListCharacterFragment(name == "" ? MessageForListCharacterFragment.CODE_LOAD_LIST_FAIL :
                                                MessageForListCharacterFragment.CODE_SEARCH_LIST_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain), categoryId));
                                }
                            }

                            countTask--;
                        }
                    });
        }
    }

    public void setLoveCharacter(final int userId, int characterId) {

        BaseRestApi.request(mContext).loveCharacter(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), userId, characterId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {

                    Appdatabase.newInstance(mContext).characterDao().deleteCharacterFavorite(userId);
                } else {

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                if (!ClientUtil.isConnectInternet(mContext)) {

                    // TODO: EventBus fail mContext.getResources().getString(R.string.no_connect_internet)
                } else {

                    if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                        // TODO: EventBus mContext.getResources().getString(R.string.no_connect_to_server)
                    } else {

                        // TODO: EventBus fail mContext.getResources().getString(R.string.server_error)
                    }
                }
            }
        });
    }

    public void getCharacterFavorite(int page, final int userId) {

        characterResponse = Appdatabase.newInstance(mContext).characterDao().getCharacterFavorite(page, userId);
        if (characterResponse != null) {

        } else {

            BaseRestApi.request(mContext).getCharacterFavorite(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), page, userId)
                    .enqueue(new Callback<CharacterResponse>() {
                        @Override
                        public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {

                            if (response.isSuccessful() && response.body().getData() != null && response.body().getData().size() > 0) {

                                response.body().setUser(userId);
                                Appdatabase.newInstance(mContext).characterDao().insertCharacter(response.body());
                                // TODO: EventBus
                            } else {

                                // TODO: EventBus fail mContext.getResources().getString(R.string.server_error) or mContext.getResources().getString(R.string.no_data_found)
                            }
                        }

                        @Override
                        public void onFailure(Call<CharacterResponse> call, Throwable t) {

                            if (!ClientUtil.isConnectInternet(mContext)) {

                                // TODO: EventBus fail mContext.getResources().getString(R.string.no_connect_internet)
                            } else {

                                if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                                    // TODO: EventBus mContext.getResources().getString(R.string.no_connect_to_server)
                                } else {

                                    // TODO: EventBus fail mContext.getResources().getString(R.string.server_error)
                                }
                            }
                        }
                    });
        }
    }

    public int getCountTask() {

        return countTask;
    }
}