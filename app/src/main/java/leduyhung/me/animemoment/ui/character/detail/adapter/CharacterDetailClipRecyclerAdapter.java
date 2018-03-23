package leduyhung.me.animemoment.ui.character.detail.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import leduyhung.me.animemoment.Constants;
import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.media.data.MediaInfo;
import leduyhung.me.animemoment.module.user.User;
import leduyhung.me.animemoment.ui.character.MessageForCharacterActivity;

import leduyhung.me.animemoment.util.ImageUtil;
import leduyhung.me.animemoment.util.TimeUtil;

public class CharacterDetailClipRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<MediaInfo> lsData;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;

    private int lastPosition = -1;
    private int animationStyle;
    private int totalItemCount, lastVisibleItem;
    private boolean loading;

    private OnLoadMoreListener onLoadMore;

    public CharacterDetailClipRecyclerAdapter(Context mContext, RecyclerView recyclerView, List<MediaInfo> lsData, int animationStyle) {
        this.mContext = mContext;
        this.lsData = lsData;
        this.animationStyle = animationStyle;
        this.recyclerView = recyclerView;
        gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (lsData.size() <= 0) {

            return Constants.Recycler.RECYCLER_ITEM_NO_DATA;
        } else {
            if (lsData.get(position) != null)
                return Constants.Recycler.RECYCLER_ITEM_HAS_DATA;
            else
                return Constants.Recycler.RECYCLER_ITEM_LOAD_MORE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.Recycler.RECYCLER_ITEM_HAS_DATA)
            return new ItemVIew(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_character_clip, parent, false));
        else if (viewType == Constants.Recycler.RECYCLER_ITEM_NO_DATA)
            return new NoItem(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_no_data, parent, false));
        else
            return new LoadMoreItem(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_load_more, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        setAnimation(holder.itemView, position);
        if (holder instanceof ItemVIew) {
            ((ItemVIew) holder).tName.setText(lsData.get(position).getName());
            ((ItemVIew) holder).tTime.setText(TimeUtil.newInstance().convertSecondToTimeString(lsData.get(position).getTime()));
            if (lsData.get(position).getAds() == 1 || !User.newInstance().addContext(mContext).isUserLogin()) {

                ((ItemVIew) holder).tNote.setText(mContext.getResources().getString(R.string.this_video_have_advertisement));
            } else {

                ((ItemVIew) holder).tNote.setText("");
            }
            ImageUtil.newInstance().showImageFromInternet(lsData.get(position).getThumbnail(), ((ItemVIew) holder).iThumbnail,
                    mContext.getResources().getInteger(R.integer.img_small), mContext.getResources().getInteger(R.integer.img_small), null);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logg.error(CharacterDetailClipRecyclerAdapter.this.getClass(), "click item: " + lsData.get(position).getId() + " at index: " + position);
                    EventBus.getDefault().post(new MessageForCharacterActivity(MessageForCharacterActivity.CODE_OPEN_CLIP, lsData.get(position)));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (lsData.size() <= 0)
            return 1;
        else
            return lsData.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, animationStyle);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private class ItemVIew extends RecyclerView.ViewHolder {

        private ImageView iThumbnail;
        private TextView tName, tTime, tNote;

        public ItemVIew(View itemView) {
            super(itemView);

            iThumbnail = itemView.findViewById(R.id.img_thumbnail);
            tName = itemView.findViewById(R.id.txt_name);
            tTime = itemView.findViewById(R.id.txt_time);
            tNote = itemView.findViewById(R.id.txt_note);
        }
    }

    private class NoItem extends RecyclerView.ViewHolder {
        public NoItem(View itemView) {
            super(itemView);
        }
    }

    private class LoadMoreItem extends RecyclerView.ViewHolder {

        public LoadMoreItem(View itemView) {
            super(itemView);
        }
    }

    public void configLoadmore() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition() + 1;

                if (!loading && totalItemCount % 30 == 0 && totalItemCount - lastVisibleItem <= 6) {

                    onLoadMore.onLoadmoreListener();
                    loading = true;
                }
            }
        });
    }

    public void setRecyclerLoadMore(boolean load) {

        this.loading = load;
    }

    public boolean getRecyclerLoadMore() {

        return loading;
    }

    public interface OnLoadMoreListener {

        void onLoadmoreListener();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {

        this.onLoadMore = listener;
    }
}
