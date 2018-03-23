package leduyhung.me.animemoment.module.media;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.db.Appdatabase;
import leduyhung.me.animemoment.db.CacheDatabaseManage;
import leduyhung.me.animemoment.module.media.data.MediaResponse;
import leduyhung.me.animemoment.rest.BaseRestApi;
import leduyhung.me.animemoment.ui.character.detail.DetailCharacterFragment;
import leduyhung.me.animemoment.ui.character.detail.MessageForDetailCharacterFragment;
import leduyhung.me.animemoment.util.ClientUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Media {

    private static Media media;
    private Context mContext;
    private MediaResponse mediaResponse;
    private int countTask;

    public static Media newInstance() {

        if (media == null) {

            media = new Media();
        }

        return media;
    }

    public Media addContext(Context ctx) {

        this.mContext = ctx;
        return media;
    }

    public void getMedia(int page, final int characterId, final int type, final boolean needEvent) {

        countTask++;
        mediaResponse = Appdatabase.newInstance(mContext).mediaDao().getMediaByPage(page, characterId, type, 0);
        if (mediaResponse != null) {

            EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                    MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_SUCCESS :
                    MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_SUCCESS,
                    mediaResponse));
            mediaResponse = null;
            countTask--;
        } else {

            BaseRestApi.request(mContext).getMediaByPage(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), characterId, page, type)
                    .enqueue(new Callback<MediaResponse>() {
                        @Override
                        public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {

                            if (response.isSuccessful() && response.body().getData() != null && response.body().getData().size() > 0) {

                                CacheDatabaseManage.newInstance().addContext(mContext).save(CacheDatabaseManage.TAG_MEDIA, response.body(), characterId, type);
                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_SUCCESS :
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_SUCCESS,
                                            response.body()));
                            } else {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                            mContext.getResources().getString(R.string.no_data_found)));
                            }

                            countTask--;
                        }

                        @Override
                        public void onFailure(Call<MediaResponse> call, Throwable t) {

                            if (!ClientUtil.isConnectInternet(mContext)) {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                            mContext.getResources().getString(R.string.no_connect_internet)));
                            } else {

                                if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain)));
                                } else {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain)));
                                }
                            }

                            countTask--;
                        }
                    });
        }
    }

    public void getArtMedia(int page, final int characterId, final int type, final boolean needEvent) {

        countTask++;
        mediaResponse = Appdatabase.newInstance(mContext).mediaDao().getMediaByPage(page, 0, type, 0);
        if (mediaResponse != null) {

            EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                    MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_SUCCESS :
                    MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_SUCCESS,
                    mediaResponse));
            mediaResponse = null;
            countTask--;
        } else {

            BaseRestApi.request(mContext).getMediaArtByPage(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), page, type)
                    .enqueue(new Callback<MediaResponse>() {
                        @Override
                        public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {

                            if (response.isSuccessful() && response.body().getData() != null && response.body().getData().size() > 0) {

                                CacheDatabaseManage.newInstance().addContext(mContext).save(CacheDatabaseManage.TAG_MEDIA, response.body(), characterId, type);
                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_SUCCESS :
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_SUCCESS,
                                            response.body()));
                            } else {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                            mContext.getResources().getString(R.string.no_data_found)));
                            }

                            countTask--;
                        }

                        @Override
                        public void onFailure(Call<MediaResponse> call, Throwable t) {

                            if (!ClientUtil.isConnectInternet(mContext)) {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                            MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                            mContext.getResources().getString(R.string.no_connect_internet)));
                            } else {

                                if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain)));
                                } else {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForDetailCharacterFragment(type == MediaResponse.TYPE_GALLERY ?
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL :
                                                MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain)));
                                }
                            }

                            countTask--;
                        }
                    });
        }
    }

    public void setLoveMedia(int type, final int userId, int mediaId) {

        BaseRestApi.request(mContext).loveMedia(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), userId, mediaId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {

                    Appdatabase.newInstance(mContext).mediaDao().deleteMediaFavorite(userId);
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

    public void getMediaFavorite(int page, int characterId, final int type, final int userId) {

        mediaResponse = Appdatabase.newInstance(mContext).mediaDao().getMediaByPage(page, characterId, type, userId);
        if (mediaResponse != null) {

        } else {

            BaseRestApi.request(mContext).getMediaFavorite(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), page, type, userId)
                    .enqueue(new Callback<MediaResponse>() {
                        @Override
                        public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {

                            if (response.isSuccessful() && response.body().getData() != null && response.body().getData().size() > 0) {

                                response.body().setUser(userId);
                                response.body().setType(type);
                                Appdatabase.newInstance(mContext).mediaDao().insertMedia(response.body());
                                // TODO: EventBus
                            } else {

                                // TODO: EventBus fail mContext.getResources().getString(R.string.server_error) or mContext.getResources().getString(R.string.no_data_found)
                            }
                        }

                        @Override
                        public void onFailure(Call<MediaResponse> call, Throwable t) {

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