package leduyhung.me.animemoment.ui.character.art;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import leduyhung.me.animemoment.Constants;
import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.media.Media;
import leduyhung.me.animemoment.module.media.data.MediaInfo;
import leduyhung.me.animemoment.module.media.data.MediaResponse;
import leduyhung.me.animemoment.ui.character.MessageForCharacterActivity;
import leduyhung.me.animemoment.ui.character.art.adapter.ArtRecyclerAdapter;
import leduyhung.me.animemoment.ui.character.detail.MessageForDetailCharacterFragment;

public class ListArtFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View v;
    private ImageView iBack;
    private TextView tTitle;
    private RecyclerView recycler;
    private ArtRecyclerAdapter adap;
    private GridLayoutManager manager;

    private List<MediaInfo> lsData;
    private int page;
    private int totalPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
        totalPage = 1;
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
        v = inflater.inflate(R.layout.fragment_list_art, container, false);
        iBack = v.findViewById(R.id.img_back);
        tTitle = v.findViewById(R.id.txt_title);
        recycler = v.findViewById(R.id.recycler);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iBack.setOnClickListener(this);
        tTitle.setText(mContext.getResources().getString(R.string.fan_art));
        lsData = new ArrayList();
        configRecyclerGallery();
        Media.newInstance().addContext(mContext).getMedia(page, -1,
                MediaResponse.TYPE_GALLERY, true);
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
                totalPage = message.getData().getTotal_page();
                if (page == 1) {
                    lsData.clear();
                    lsData.addAll(message.getData().getData());
                    adap.configLoadmore();

                } else {

                    lsData.remove(lsData.size() - 1);
                    adap.notifyItemRemoved(lsData.size());
                    lsData.addAll(message.getData().getData());
                    adap.setRecyclerLoadMore(false);
                }
                adap.notifyDataSetChanged();
                break;
            case MessageForDetailCharacterFragment.CODE_LOAD_MEDIA_IMG_FAIL:
                if (page > 1 && adap.getRecyclerLoadMore()) {

                    lsData.remove(lsData.size() - 1);
                    adap.notifyItemRemoved(lsData.size());
                }
                break;
        }
    }

    private void configRecyclerGallery() {

        manager = new GridLayoutManager(mContext, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                switch (adap.getItemViewType(position)) {

                    case Constants.Recycler.RECYCLER_ITEM_NO_DATA:
                        return 3;
                    case Constants.Recycler.RECYCLER_ITEM_HAS_DATA:
                        return 1;
                    default:
                        return 3;
                }
            }
        });

        recycler.setLayoutManager(manager);
        adap = new ArtRecyclerAdapter(mContext, recycler, lsData, R.anim.anim_fade_in);
        adap.setOnLoadMoreListener(new ArtRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadmoreListener() {

                if (totalPage > page) {
                    lsData.add(null);
                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            adap.notifyItemInserted(lsData.size() - 1);
                            Media.newInstance().addContext(mContext).getMedia(page, -1,
                                    MediaResponse.TYPE_GALLERY, true);
                        }
                    });
                }
            }
        });
        if (lsData.size() > 0) {

            adap.configLoadmore();
        }

        recycler.setAdapter(adap);
    }
}