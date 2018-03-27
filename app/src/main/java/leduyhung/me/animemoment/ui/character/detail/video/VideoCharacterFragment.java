package leduyhung.me.animemoment.ui.character.detail.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.user.User;
import leduyhung.me.animemoment.module.user.data.UserInfo;
import leduyhung.me.animemoment.ui.character.CharacterActivity;
import leduyhung.me.animemoment.util.AdsUtil;
import leduyhung.me.animemoment.util.ClientUtil;
import leduyhung.me.animemoment.util.ToastUtil;
import leduyhung.view.myprogress.loading.dot.LoadingDotView;

public class VideoCharacterFragment extends Fragment {

    private Context mContext;
    private View v;
    private String url;
    private int ads;
    private long currentPosition;
    private boolean isPlay;

    AdsUtil adsUtil;
    private LoadingDotView loadingDotView;
    private PlayerView playerView;

    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private DefaultDataSourceFactory dataSourceFactory;
    private DefaultBandwidthMeter defaultBandwidthMeter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString(CharacterActivity.INTENT_URL_MEDIA);
        ads = getArguments().getInt(CharacterActivity.INTENT_ADS_MEDIA);
        adsUtil = new AdsUtil(mContext);
    }

    @Override
    public void onResume() {
        adsUtil.resumeAdsReward();
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null) && isPlay) {
            initializePlayer();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDetach() {
        adsUtil.destroyAdsReward();
        super.onDetach();
        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {

        adsUtil.pauseAdsReward();
        super.onPause();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_video_character, container, false);
        playerView = v.findViewById(R.id.player_view);
        loadingDotView = v.findViewById(R.id.loading_dot);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adsUtil = new AdsUtil(mContext);
        if (User.newInstance().addContext(mContext).isUserLogin()) {
            if (User.newInstance().getUserInfo().getAds() < ads) {

                ToastUtil.newInstance().showToast(mContext, mContext.getResources().getString(R.string.wait_ads_complete_to_view_clip), Toast.LENGTH_LONG);
                adsUtil.initAdsReward(new RewardedVideoAdListener() {
                    @Override
                    public void onRewardedVideoAdLoaded() {

                        adsUtil.showAdsReward();
                    }

                    @Override
                    public void onRewardedVideoAdOpened() {

                    }

                    @Override
                    public void onRewardedVideoStarted() {

                    }

                    @Override
                    public void onRewardedVideoAdClosed() {

                    }

                    @Override
                    public void onRewarded(RewardItem rewardItem) {

                        if (rewardItem.getAmount() > 0) {
                            User.newInstance().getUserInfo().setAds(User.newInstance().getUserInfo().getAds() + rewardItem.getAmount());
                            Logg.error(getClass(), rewardItem.getAmount() + " -- " + rewardItem.getType());
                            User.newInstance().getUserInfo().setAds(User.newInstance().getUserInfo().getAds() + rewardItem.getAmount() - ads);
                            loadingDotView.showLoading(true);
                            initializePlayer();
                        }
                    }

                    @Override
                    public void onRewardedVideoAdLeftApplication() {

                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(int i) {

                        initializePlayer();
                    }
                });
            } else {

                User.newInstance().getUserInfo().setAds(User.newInstance().getUserInfo().getAds() - ads);
                loadingDotView.showLoading(true);
                initializePlayer();
            }
        } else {

            ToastUtil.newInstance().showToast(mContext, mContext.getResources().getString(R.string.wait_ads_complete_to_view_clip), Toast.LENGTH_LONG);
            adsUtil.initAdsReward(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {

                    adsUtil.showAdsReward();
                }

                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoStarted() {

                }

                @Override
                public void onRewardedVideoAdClosed() {

                }

                @Override
                public void onRewarded(RewardItem rewardItem) {

                    if (rewardItem.getAmount() > 0) {

                        Logg.error(getClass(), rewardItem.getAmount() + " -- " + rewardItem.getType());
                        loadingDotView.showLoading(true);
                        initializePlayer();
                    }
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {

                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {

                    initializePlayer();
                }
            });
        }
    }

    private void initializePlayer() {

        defaultBandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, ClientUtil.getPakageName(mContext)), defaultBandwidthMeter);
        playerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(defaultBandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        playerView.setPlayer(player);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

                Logg.error(getClass(), "onLoadingChanged -> " + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                Logg.error(getClass(), "playWhenReady -> " + playWhenReady + " --- " + playbackState);
                if (playWhenReady && playbackState == 3) {
                    isPlay = true;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                Logg.error(getClass(), "shuffleModeEnabled -> " + shuffleModeEnabled);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                Logg.error(getClass(), "onPlayerError -> " + error.toString());
                if (player != null)
                    currentPosition = player.getCurrentPosition();
                releasePlayer();
                initializePlayer();
                player.seekTo(currentPosition);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

                Logg.error(getClass(), "onPositionDiscontinuity: " + reason);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

                Logg.error(getClass(), "onSeekProcessed: " + player.getCurrentPosition());
            }
        });

        ExtractorMediaSource.Factory f = new ExtractorMediaSource.Factory(dataSourceFactory);
        Logg.error(getClass(), "load video: " + url);
        MediaSource mediaSource = f.createMediaSource(Uri.parse(url), new Handler(), new MediaSourceEventListener() {
            @Override
            public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {

                Logg.error(getClass(), "onLoadStarted: mediaStartTimeMs -> " + mediaStartTimeMs + " mediaEndTimeMs -> " + mediaEndTimeMs);
            }

            @Override
            public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

                Logg.error(getClass(), "onLoadCompleted");
            }

            @Override
            public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

                Logg.error(getClass(), "onLoadCanceled");
                if (player != null)
                    currentPosition = player.getCurrentPosition();
            }

            @Override
            public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {

                Logg.error(getClass(), "onLoadError");
                if (!ClientUtil.isConnectInternet(mContext)) {

                    ToastUtil.newInstance().showToast(mContext, mContext.getResources().getString(R.string.no_connect_internet), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {

                Logg.error(getClass(), "onUpstreamDiscarded");
            }

            @Override
            public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {

                Logg.error(getClass(), "onDownstreamFormatChanged");
                loadingDotView.showLoading(false);
            }
        });

        player.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            trackSelector = null;
        }
    }
}