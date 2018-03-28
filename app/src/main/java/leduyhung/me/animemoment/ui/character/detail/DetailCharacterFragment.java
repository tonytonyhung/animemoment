package leduyhung.me.animemoment.ui.character.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import leduyhung.me.animemoment.Constants;
import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.character.data.CharacterInfo;
import leduyhung.me.animemoment.module.media.Media;
import leduyhung.me.animemoment.module.media.data.MediaInfo;
import leduyhung.me.animemoment.module.media.data.MediaResponse;
import leduyhung.me.animemoment.ui.character.CharacterActivity;
import leduyhung.me.animemoment.ui.character.MessageForCharacterActivity;
import leduyhung.me.animemoment.ui.character.detail.adapter.CharacterDetailClipRecyclerAdapter;
import leduyhung.me.animemoment.ui.character.detail.adapter.CharacterDetailGalleryRecyclerAdapter;
import leduyhung.me.animemoment.ui.character.detail.adapter.CharacterDetailInfoRecyclerAdapter;
import leduyhung.me.animemoment.util.ImageUtil;
import leduyhung.view.myprogress.loading.dot.LoadingDotView;

public class DetailCharacterFragment extends Fragment implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private Context mContext;
    private View v;
    private ImageView iWall, iBack;
    private CircleImageView iAvatar;
    private TextView tName;
    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private TabLayout tabLayout;
    private LoadingDotView loadingDotView;

    private CharacterDetailInfoRecyclerAdapter adapInfo;
    private CharacterDetailGalleryRecyclerAdapter adapGallery;
    private CharacterDetailClipRecyclerAdapter adapClip;

    private CharacterInfo characterInfo;
    private List<MediaInfo> lsDataGallery, lsDataClip;

    private int pageGallery, pageClip, totalPageGallery, totalPageClip;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        characterInfo = (CharacterInfo) getArguments().getSerializable(CharacterActivity.INTENT_CHARACTER_DATA);
        pageGallery = pageClip = totalPageClip = totalPageGallery = 1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
        ImageUtil.newInstance().addContext(mContext);
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
        v = inflater.inflate(R.layout.fragment_detail_character, container, false);
        iWall = v.findViewById(R.id.img_wall);
        iBack = v.findViewById(R.id.img_back);
        iAvatar = v.findViewById(R.id.img_avatar);
        tName = v.findViewById(R.id.txt_avatar);
        tabLayout = v.findViewById(R.id.tab_layout);
        recyclerView = v.findViewById(R.id.recycler);
        loadingDotView = v.findViewById(R.id.loading_dot);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageUtil.newInstance().showImageFromInternet(characterInfo.getUrl(), iWall,
                mContext.getResources().getInteger(R.integer.img_normal), mContext.getResources().getInteger(R.integer.img_normal_short), null);
        ImageUtil.newInstance().showImageFromInternet(characterInfo.getThumbnail(), iAvatar,
                mContext.getResources().getInteger(R.integer.img_tiny), mContext.getResources().getInteger(R.integer.img_tiny), null);
        tName.setText(characterInfo.getName());
        iBack.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
        lsDataGallery = new ArrayList();
        lsDataClip = new ArrayList();
        configRecyclerGallery();
        Media.newInstance().addContext(mContext).getMedia(pageGallery, characterInfo.getId(), MediaResponse.TYPE_GALLERY, true);
        Media.newInstance().addContext(mContext).getMedia(pageClip, characterInfo.getId(), MediaResponse.TYPE_CLIP, false);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back:
                EventBus.getDefault().post(new MessageForCharacterActivity(MessageForCharacterActivity.CODE_CLICK_BACK_CHARACTER_DETAIL));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForDetailCharacterFragment message) {

        switch (message.getCode()) {

            case MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_SUCCESS:
                loadingDotView.showLoading(false);
                totalPageGallery = message.getData().getTotal_page();
                if (pageGallery == 1) {
                    lsDataGallery.clear();
                    lsDataGallery.addAll(message.getData().getData());
                    adapGallery.configLoadmore();
                    adapGallery.notifyDataSetChanged();

                } else {

                    lsDataGallery.remove(lsDataGallery.size() - 1);
                    adapGallery.notifyItemRemoved(lsDataGallery.size());
                    for (MediaInfo mediaInfo : message.getData().getData()) {

                        lsDataGallery.add(mediaInfo);
                        adapGallery.notifyItemInserted(lsDataGallery.size() - 1);
                    }
                    adapGallery.setRecyclerLoadMore(false);
                }
                break;
            case MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_SUCCESS:
                loadingDotView.showLoading(false);
                totalPageClip = message.getData().getTotal_page();
                if (adapClip != null) {
                    if (pageClip == 1) {
                        lsDataClip.clear();
                        lsDataClip.addAll(message.getData().getData());
                        adapClip.configLoadmore();
                        adapClip.notifyDataSetChanged();
                    } else {

                        lsDataClip.remove(lsDataClip.size() - 1);
                        adapClip.notifyItemRemoved(lsDataClip.size());
                        for (MediaInfo mediaInfo : message.getData().getData()) {

                            lsDataClip.add(mediaInfo);
                            adapClip.notifyItemInserted(lsDataGallery.size() - 1);
                        }
                        adapClip.setRecyclerLoadMore(false);
                    }
                }
                break;
            case MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL:
                loadingDotView.showLoading(false);
                if (pageGallery > 1 && adapGallery.getRecyclerLoadMore()) {

                    lsDataGallery.remove(lsDataGallery.size() - 1);
                    adapGallery.notifyItemRemoved(lsDataGallery.size());
                    adapGallery.setRecyclerLoadMore(false);
                }
                break;
            case MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_CLIP_FAIL:
                loadingDotView.showLoading(false);
                if (adapClip != null) {
                    if (pageClip > 1 && adapClip.getRecyclerLoadMore()) {

                        lsDataClip.remove(lsDataClip.size() - 1);
                        adapClip.notifyItemRemoved(lsDataClip.size());
                        adapClip.setRecyclerLoadMore(false);
                    }
                }
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {

            case 0:
                pageClip = 1;
                configRecyclerGallery();
                if (lsDataGallery.size() <= 0 && Media.newInstance().getCountTask() == 0) {
                    if (!loadingDotView.isShow())
                        loadingDotView.showLoading(true);
                    Media.newInstance().addContext(mContext).getMedia(1, characterInfo.getId(), MediaResponse.TYPE_GALLERY, true);
                }
                break;
            case 1:
                pageGallery = 1;
                configRecyclerClip();
                if (lsDataClip.size() <= 0 && Media.newInstance().getCountTask() == 0) {
                    if (!loadingDotView.isShow())
                        loadingDotView.showLoading(true);
                    Media.newInstance().addContext(mContext).getMedia(pageGallery, characterInfo.getId(), MediaResponse.TYPE_CLIP, true);
                }
                break;
            case 2:

                configRecyclerInfo();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void configRecyclerInfo() {

        adapInfo = new CharacterDetailInfoRecyclerAdapter(mContext, characterInfo.getDescription(), R.anim.anim_fade_in);
        manager = new GridLayoutManager(mContext, 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapInfo);
    }

    private void configRecyclerGallery() {

        manager = new GridLayoutManager(mContext, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                switch (adapGallery.getItemViewType(position)) {

                    case Constants.Recycler.RECYCLER_ITEM_NO_DATA:
                        return 2;
                    case Constants.Recycler.RECYCLER_ITEM_HAS_DATA:
                        return 1;
                    default:
                        return 2;
                }
            }
        });

        recyclerView.setLayoutManager(manager);
        adapGallery = new CharacterDetailGalleryRecyclerAdapter(mContext, recyclerView, lsDataGallery, R.anim.anim_fade_in);
        adapGallery.setOnLoadMoreListener(new CharacterDetailGalleryRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadmoreListener() {

                if (totalPageGallery > pageGallery) {
                    lsDataGallery.add(null);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {

                            pageGallery++;
                            adapGallery.notifyItemInserted(lsDataGallery.size() - 1);
                            Media.newInstance().addContext(mContext).getMedia(pageGallery, characterInfo.getId(),
                                    MediaResponse.TYPE_GALLERY, true);
                        }
                    });
                }
            }
        });
        if (lsDataGallery.size() > 0) {

            adapGallery.configLoadmore();
        }

        recyclerView.setAdapter(adapGallery);
    }

    private void configRecyclerClip() {

        manager = new GridLayoutManager(mContext, 1);

        recyclerView.setLayoutManager(manager);
        adapClip = new CharacterDetailClipRecyclerAdapter(mContext, recyclerView, lsDataClip, R.anim.anim_fade_in);
        adapClip.setOnLoadMoreListener(new CharacterDetailClipRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadmoreListener() {

                if (totalPageClip > pageClip) {
                    lsDataClip.add(null);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {

                            pageClip++;
                            adapClip.notifyItemInserted(lsDataClip.size() - 1);
                            Media.newInstance().addContext(mContext).getMedia(pageClip, characterInfo.getId(),
                                    MediaResponse.TYPE_CLIP, true);
                        }
                    });
                }
            }
        });
        if (lsDataClip.size() > 0) {

            adapClip.configLoadmore();
        }
        recyclerView.setAdapter(adapClip);
    }
}