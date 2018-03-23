package leduyhung.me.animemoment.ui.character.detail.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.user.User;
import leduyhung.me.animemoment.util.AdsUtil;
import leduyhung.me.animemoment.util.ClientUtil;
import leduyhung.me.animemoment.util.ImageUtil;
import leduyhung.me.animemoment.util.ToastUtil;

import static leduyhung.me.animemoment.ui.character.detail.gallery.adapter.GalleryCharacterViewPagerAdapter.INTENT_MEDIA_ADS;
import static leduyhung.me.animemoment.ui.character.detail.gallery.adapter.GalleryCharacterViewPagerAdapter.INTENT_MEDIA_NAME;
import static leduyhung.me.animemoment.ui.character.detail.gallery.adapter.GalleryCharacterViewPagerAdapter.INTENT_MEDIA_URL;

public class ImageCharacterFragment extends Fragment {

    private Context mContext;
    private View v;
    private ImageView image;
    private String url;
    private String name;
    private int ads;

    private AdsUtil adsUtil;

    private EventBus eventBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString(INTENT_MEDIA_URL);
        name = getArguments().getString(INTENT_MEDIA_NAME);
        ads = getArguments().getInt(INTENT_MEDIA_ADS);
        adsUtil = new AdsUtil(mContext);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (eventBus != null)
            eventBus.unregister(this);
        eventBus = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        } else {

            if (eventBus != null)
                eventBus.unregister(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_image_character, container, false);
        image = v.findViewById(R.id.image);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageUtil.newInstance().addContext(mContext).showImageFromInternet(url, image,
                mContext.getResources().getInteger(R.integer.img_large), mContext.getResources().getInteger(R.integer.img_large_short), null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForImageCharacterFragment message) {

        switch (message.getCode()) {

            case MessageForImageCharacterFragment.CODE_CLICK_DOWNLOAD_IMG:
                if (message.getUrl() == url && ClientUtil.isConnectInternet(mContext)) {
                    ToastUtil.newInstance().showToast(mContext,
                            mContext.getResources().getString(R.string.saving),
                            Toast.LENGTH_SHORT);
                    BitmapDrawable draw = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = draw.getBitmap();
                    ImageUtil.newInstance().addContext(mContext).saveImage(bitmap, mContext);
                    if (!User.newInstance().addContext(mContext).isUserLogin() || User.newInstance().getUserInfo().getAds() < ads) {

                        adsUtil.initAdsNoReward(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                            }

                            @Override
                            public void onAdLeftApplication() {
                                super.onAdLeftApplication();
                            }

                            @Override
                            public void onAdOpened() {
                                super.onAdOpened();

                                ToastUtil.newInstance().showToast(mContext,
                                        mContext.getResources().getString(R.string.click_to_ads_for_no_show_next_time),
                                        Toast.LENGTH_LONG);
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                adsUtil.showAdsNoReward();
                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();

                                if (User.newInstance().isUserLogin()) {

                                    User.newInstance().getUserInfo().setAds(User.newInstance().getUserInfo().getAds() + 10);
                                }
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }
                        });
                    } else {

                        User.newInstance().getUserInfo().setAds(User.newInstance().getUserInfo().getAds() - 1);
                    }
                } else {

                    ToastUtil.newInstance().showToast(mContext, mContext.getResources().getString(R.string.no_connect_internet), Toast.LENGTH_SHORT);
                }
                break;
        }
    }
}
