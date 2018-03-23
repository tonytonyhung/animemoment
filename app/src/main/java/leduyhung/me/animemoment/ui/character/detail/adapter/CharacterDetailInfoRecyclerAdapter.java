package leduyhung.me.animemoment.ui.character.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import leduyhung.me.animemoment.Constants;
import leduyhung.me.animemoment.R;

public class CharacterDetailInfoRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String content;

    private int lastPosition = -1;
    private int animationStyle;

    public CharacterDetailInfoRecyclerAdapter(Context mContext, String content, int animationStyle) {

        this.mContext = mContext;
        this.content = content;
        this.animationStyle = animationStyle;
    }

    @Override
    public int getItemViewType(int position) {
        if (content == null) {

            return Constants.Recycler.RECYCLER_ITEM_NO_DATA;
        } else {

            return Constants.Recycler.RECYCLER_ITEM_HAS_DATA;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == Constants.Recycler.RECYCLER_ITEM_NO_DATA)
            return new NoItem(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_no_data, parent, false));
        else
            return new ItemVIew(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_character_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        setAnimation(holder.itemView, position);
        if (holder instanceof ItemVIew) {

            ((ItemVIew) holder).tContent.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, animationStyle);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private class ItemVIew extends RecyclerView.ViewHolder {

        private TextView tContent;

        public ItemVIew(View itemView) {
            super(itemView);

            tContent = itemView.findViewById(R.id.txt_content);
        }
    }

    private class NoItem extends RecyclerView.ViewHolder {
        public NoItem(View itemView) {
            super(itemView);
        }
    }
}