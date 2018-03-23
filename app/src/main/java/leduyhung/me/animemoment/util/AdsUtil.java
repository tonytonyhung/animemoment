package leduyhung.me.animemoment.util;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import leduyhung.me.animemoment.R;

public class AdsUtil {

    private Context mContext;

    private RewardedVideoAd mRewardedVideoAd;

    private InterstitialAd mInterstitialAd;

    public AdsUtil(Context mContext) {
        this.mContext = mContext;

    }

    public void initAdsReward(RewardedVideoAdListener rewardedVideoAdListener) {

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        mRewardedVideoAd.loadAd(mContext.getResources().getString(R.string.ads_reward),
                new AdRequest.Builder().build());
    }

    public void showAdsReward() {

        mRewardedVideoAd.show();
    }

    public void resumeAdsReward() {

        mRewardedVideoAd.resume(mContext);
    }

    public void pauseAdsReward() {

        mRewardedVideoAd.pause(mContext);

    }

    public void destroyAdsReward() {

        mRewardedVideoAd.destroy(mContext);

    }

    public void initAdsNoReward(AdListener adListener) {

//        AdRequest request = new AdRequest.Builder()
//                .addTestDevice("00EA6141A9EB9AFCD18676E0DA43755B")
//                .build();
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(mContext.getResources().getString(R.string.ads_no_reward));
        mInterstitialAd.setAdListener(adListener);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    public void showAdsNoReward() {

        mInterstitialAd.show();
    }
}