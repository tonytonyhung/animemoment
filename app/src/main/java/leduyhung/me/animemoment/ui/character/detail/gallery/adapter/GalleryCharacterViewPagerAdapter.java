package leduyhung.me.animemoment.ui.character.detail.gallery.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import leduyhung.me.animemoment.module.media.data.MediaInfo;
import leduyhung.me.animemoment.module.media.data.MediaResponse;
import leduyhung.me.animemoment.ui.character.detail.gallery.ImageCharacterFragment;

public class GalleryCharacterViewPagerAdapter extends FragmentStatePagerAdapter {

    public static final String INTENT_MEDIA_URL = "INTENT_MEDIA_URL";
    public static final String INTENT_MEDIA_NAME = "INTENT_MEDIA_NAME";
    public static final String INTENT_MEDIA_ADS = "INTENT_MEDIA_ADS";

    private ImageCharacterFragment imageCharacterFragment;

    private List<MediaInfo> mediaInfos;

    public GalleryCharacterViewPagerAdapter(FragmentManager fm, List<MediaInfo> mediaInfos) {
        super(fm);
        this.mediaInfos = mediaInfos;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();

        imageCharacterFragment = new ImageCharacterFragment();
        bundle.putString(INTENT_MEDIA_URL, mediaInfos.get(position).getUrl());
        bundle.putString(INTENT_MEDIA_NAME, mediaInfos.get(position).getName());
        bundle.putInt(INTENT_MEDIA_ADS, mediaInfos.get(position).getAds());
        if (imageCharacterFragment.getArguments() != null)
            imageCharacterFragment.getArguments().clear();
        imageCharacterFragment.setArguments(bundle);
        return imageCharacterFragment;
    }

    @Override
    public int getCount() {
        return mediaInfos.size();
    }
}
