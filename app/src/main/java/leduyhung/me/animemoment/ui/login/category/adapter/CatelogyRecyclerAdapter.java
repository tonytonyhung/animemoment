package leduyhung.me.animemoment.ui.login.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import leduyhung.me.animemoment.Constants;
import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.category.data.CategoryInfo;
import leduyhung.me.animemoment.ui.login.category.MessageForCategoryFragment;

public class CatelogyRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CategoryInfo> lsData;

    private int lastPosition = -1;
    private int animationStyle;

    public CatelogyRecyclerAdapter(Context context, List<CategoryInfo> lsData, int animationStyle) {

        this.mContext = context;
        this.lsData = lsData;
        this.animationStyle = animationStyle;
    }

    @Override
    public int getItemViewType(int position) {

        if (lsData.size() > 0) {

            return Constants.Recycler.RECYCLER_ITEM_HAS_DATA;
        } else {

            return Constants.Recycler.RECYCLER_ITEM_NO_DATA;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.Recycler.RECYCLER_ITEM_HAS_DATA)
            return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_category, parent, false));
        else
            return new NoItem(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_no_data, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        setAnimation(holder.itemView, position);
        if (holder instanceof ItemView) {
            ((ItemView) holder).tName.setText(lsData.get(position).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.CODE_CLICK_ITEM_CATEGORY,
                            lsData.get(position).getId(), lsData.get(position).getName(), lsData.get(position).getType()));
                    Logg.error(CatelogyRecyclerAdapter.this.getClass(), "click item: " + lsData.get(position).getId() + " at index: " + position + " type: " + lsData.get(position).getType());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (lsData.size() > 0) {

            return lsData.size();
        } else {
            return 1;
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, animationStyle); //android.R.anim.slide_in_left
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private class ItemView extends RecyclerView.ViewHolder {

        private TextView tName;
        private RelativeLayout rItem;

        public ItemView(View itemView) {
            super(itemView);

            tName = itemView.findViewById(R.id.txt_name);
            rItem = itemView.findViewById(R.id.relative_item);
        }
    }

    private class NoItem extends RecyclerView.ViewHolder {
        public NoItem(View itemView) {
            super(itemView);
        }
    }
}