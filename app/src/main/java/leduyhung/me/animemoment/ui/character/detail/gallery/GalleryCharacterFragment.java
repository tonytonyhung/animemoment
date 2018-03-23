package leduyhung.me.animemoment.ui.character.detail.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leduyhung.loglibrary.Logg;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.db.Appdatabase;
import leduyhung.me.animemoment.module.media.data.MediaInfo;
import leduyhung.me.animemoment.module.media.data.MediaResponse;
import leduyhung.me.animemoment.ui.character.MessageForCharacterActivity;
import leduyhung.me.animemoment.ui.character.detail.gallery.adapter.GalleryCharacterViewPagerAdapter;
import leduyhung.me.animemoment.ui.character.detail.gallery.transfomer.GalleryTransfomerViewPager;
import leduyhung.me.animemoment.util.ClientUtil;
import leduyhung.me.animemoment.util.ToastUtil;

import static leduyhung.me.animemoment.ui.character.CharacterActivity.INTENT_LIST_MEDIA_INFOS;
import static leduyhung.me.animemoment.ui.character.CharacterActivity.INTENT_POSITION_MEDIA_INFOS;

public class GalleryCharacterFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Context mContext;
    private View v;
    private ViewPager vPager;
    private RelativeLayout rToolbar;
    private TextView tPage;
    private ImageView iBack, iDownload;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    private GalleryCharacterViewPagerAdapter adap;
    private List<MediaInfo> mediaInfos;
    private List<MediaResponse> lsResponse;
    private int position;
    private int characterId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        characterId = getArguments().getInt(INTENT_LIST_MEDIA_INFOS);
        position = getArguments().getInt(INTENT_POSITION_MEDIA_INFOS);
        lsResponse = Appdatabase.newInstance(mContext).mediaDao().getMediaByCharacter(characterId, 0, 0);
        mediaInfos = new ArrayList();
        if (lsResponse != null && lsResponse.size() > 0) {
            for (MediaResponse response : lsResponse) {

                if (response != null && response.getData() != null && response.getData().size() > 0) {
                    mediaInfos.addAll(response.getData());
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_gallery_character, container, false);
        iBack = v.findViewById(R.id.img_back);
        tPage = v.findViewById(R.id.txt_page);
        vPager = v.findViewById(R.id.v_pager);
        iDownload = v.findViewById(R.id.img_download);
        rToolbar = v.findViewById(R.id.relative_toolbar);
        slidingUpPanelLayout = v.findViewById(R.id.sliding_layout);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iBack.setOnClickListener(this);
        iDownload.setOnClickListener(this);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    setEnterTransition(null);
                    EventBus.getDefault().post(new MessageForCharacterActivity(MessageForCharacterActivity.CODE_CLICK_BACK_CHARACTER_DETAIL));
                }
            }
        });
        configViewPager();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back:

                EventBus.getDefault().post(new MessageForCharacterActivity(MessageForCharacterActivity.CODE_CLICK_BACK_CHARACTER_DETAIL));
                break;
            case R.id.img_download:
                EventBus.getDefault().post(new MessageForImageCharacterFragment(MessageForImageCharacterFragment.CODE_CLICK_DOWNLOAD_IMG, mediaInfos.get(position).getUrl()));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForGalleyCharacterFragment message) {

        switch (message.getCode()) {

            case MessageForGalleyCharacterFragment.CODE_SAVE_IMG_SUCCESS:
                ClientUtil.vibarateDevice(mContext);
                break;
            case MessageForGalleyCharacterFragment.CODE_SAVE_IMG_FAIL:
                ToastUtil.newInstance().showToast(mContext,
                        mContext.getResources().getString(R.string.cannot_save_the_image_to_device),
                        Toast.LENGTH_SHORT);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        tPage.setText(position + 1 + "/" + mediaInfos.size());
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void configViewPager() {

        adap = new GalleryCharacterViewPagerAdapter(getChildFragmentManager(), mediaInfos);
        vPager.setAdapter(adap);
        vPager.setOffscreenPageLimit(10);
        vPager.setCurrentItem(position);
        vPager.addOnPageChangeListener(this);
        vPager.setPageTransformer(true, new GalleryTransfomerViewPager());
        tPage.setText(position + 1 + "/" + mediaInfos.size());
    }
}