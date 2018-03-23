package leduyhung.me.animemoment.ui.character;

import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.character.data.CharacterInfo;
import leduyhung.me.animemoment.ui.character.art.ListArtFragment;
import leduyhung.me.animemoment.ui.character.detail.DetailCharacterFragment;
import leduyhung.me.animemoment.ui.character.detail.gallery.GalleryCharacterFragment;
import leduyhung.me.animemoment.ui.character.detail.video.VideoCharacterFragment;
import leduyhung.me.animemoment.ui.character.list.ListCharacterFragment;
import leduyhung.me.animemoment.ui.login.category.CategoryFragment;

public class CharacterActivity extends AppCompatActivity {

    public static final String INTENT_CHARACTER_DATA = "INTENT_CHARACTER_DATA";
    public static final String INTENT_LIST_MEDIA_INFOS = "INTENT_LIST_MEDIA_INFOS";
    public static final String INTENT_POSITION_MEDIA_INFOS = "INTENT_POSITION_MEDIA_INFOS";
    public static final String INTENT_URL_MEDIA = "INTENT_URL_MEDIA";
    public static final String INTENT_ADS_MEDIA = "INTENT_ADS_MEDIA";

    private FragmentTransaction transaction;
    private ListCharacterFragment listCharacterFragment;
    private DetailCharacterFragment detailCharacterFragment;
    private ListArtFragment listArtFragment;
    private GalleryCharacterFragment galleryCharacterFragment;
    private VideoCharacterFragment videoCharacterFragment;

    private Bundle bundle;
    private int categoryId;
    private String categoryName;
    private int categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        categoryId = getIntent().getIntExtra(CategoryFragment.INTENT_ID_CATEGORY, 0);
        categoryName = getIntent().getStringExtra(CategoryFragment.INTENT_NAME_CATEGORY);
        categoryType = getIntent().getIntExtra(CategoryFragment.INTENT_TYPE_CATEGORY, 0);
        if (categoryType == 0)
            showListCharacterFragment();
        else
            showListArtFragment();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onBackPressed() {

        if (listCharacterFragment != null && listCharacterFragment.isSearchBarOpen()) {

            listCharacterFragment.closeSearchBar();
        } else if (detailCharacterFragment != null && detailCharacterFragment.isAdded()) {

            if ((galleryCharacterFragment != null && galleryCharacterFragment.isAdded()) ||
                    (videoCharacterFragment != null && videoCharacterFragment.isAdded()))
                super.onBackPressed();
            else
                showListCharacterFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForCharacterActivity message) {

        switch (message.getCode()) {

            case MessageForCharacterActivity.CODE_OPEN_CHARACTER_DETAIL:
                showDetailFragment(message.getDataDetail());
                break;
            case MessageForCharacterActivity.CODE_CLICK_BACK_CHARACTER_DETAIL:
                onBackPressed();
                break;
            case MessageForCharacterActivity.CODE_OPEN_GALLERY:
                showGalleryFragment(message.getCharacterId(), message.getPosition());
                break;
            case MessageForCharacterActivity.CODE_OPEN_CLIP:
                showClipFragment(message.getMediaInfo().getUrl(), message.getMediaInfo().getAds());
                break;
        }
    }

    private void showListCharacterFragment() {

        if (listCharacterFragment == null) {
            listCharacterFragment = new ListCharacterFragment();
        }

        bundle = new Bundle();
        bundle.putInt(CategoryFragment.INTENT_ID_CATEGORY, categoryId);
        bundle.putString(CategoryFragment.INTENT_NAME_CATEGORY, categoryName);

        if (listCharacterFragment.getArguments() != null)
            listCharacterFragment.getArguments().clear();
        listCharacterFragment.setArguments(bundle);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, listCharacterFragment);
        transaction.commit();
    }

    private void showDetailFragment(CharacterInfo characterInfo) {

        if (detailCharacterFragment == null) {
            detailCharacterFragment = new DetailCharacterFragment();
        }
        bundle = new Bundle();
        bundle.putSerializable(INTENT_CHARACTER_DATA, characterInfo);
        if (detailCharacterFragment.getArguments() != null)
            detailCharacterFragment.getArguments().clear();
        detailCharacterFragment.setArguments(bundle);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, detailCharacterFragment);
        transaction.commit();
    }

    private void showListArtFragment() {

        if (listArtFragment == null) {
            listArtFragment = new ListArtFragment();
        }
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, listArtFragment);
        transaction.commit();
    }

    private void showGalleryFragment(int characterId, int position) {

        galleryCharacterFragment = new GalleryCharacterFragment();
        bundle = new Bundle();
        bundle.putInt(INTENT_LIST_MEDIA_INFOS, characterId);
        bundle.putInt(INTENT_POSITION_MEDIA_INFOS, position);
        if (galleryCharacterFragment.getArguments() != null)
            galleryCharacterFragment.getArguments().clear();
        galleryCharacterFragment.setArguments(bundle);
        transaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition f = new Slide();
            f.setDuration(300);
            galleryCharacterFragment.setEnterTransition(f);
        }
        transaction.add(R.id.frame, galleryCharacterFragment).addToBackStack(null);
        transaction.commit();
    }

    private void showClipFragment(String url, int ads) {

        videoCharacterFragment = new VideoCharacterFragment();
        bundle = new Bundle();
        bundle.putString(INTENT_URL_MEDIA, url);
        bundle.putInt(INTENT_ADS_MEDIA, ads);
        if (videoCharacterFragment.getArguments() != null)
            videoCharacterFragment.getArguments().clear();
        videoCharacterFragment.setArguments(bundle);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame, videoCharacterFragment).addToBackStack(null);
        transaction.commit();
    }
}