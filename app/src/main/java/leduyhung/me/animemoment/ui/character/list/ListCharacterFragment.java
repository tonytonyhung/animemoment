package leduyhung.me.animemoment.ui.character.list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import leduyhung.me.animemoment.Constants;
import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.core.view.SearchView;
import leduyhung.me.animemoment.module.character.Character;
import leduyhung.me.animemoment.module.character.data.CharacterInfo;
import leduyhung.me.animemoment.ui.character.list.adapter.ListCharacterRecyclerAdapter;
import leduyhung.me.animemoment.ui.login.category.CategoryFragment;
import leduyhung.me.animemoment.util.ClientUtil;
import leduyhung.me.animemoment.util.ToastUtil;
import leduyhung.view.myprogress.loading.dot.LoadingDotView;

public class ListCharacterFragment extends Fragment implements TextWatcher, View.OnClickListener {

    private Context mContext;
    private View view, vShadow;
    private LoadingDotView loadingDotView;
    private RecyclerView recycler;
    private ListCharacterRecyclerAdapter adap;
    private SearchView searchView;
    private ImageView iBack;
    private CoordinatorLayout coordinatorLayout;
    private GridLayoutManager manager;
    private Handler handler;

    private int categoryId;
    private String categoryName;
    private List<CharacterInfo> lsData;
    private String search;
    private int page, totalPage;
    private boolean isDataSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryId = getArguments().getInt(CategoryFragment.INTENT_ID_CATEGORY);
        categoryName = getArguments().getString(CategoryFragment.INTENT_NAME_CATEGORY);
        lsData = new ArrayList();
        page = totalPage = 1;
        isDataSearch = false;
        search = "";
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
        view = inflater.inflate(R.layout.fragment_list_character, container, false);
        searchView = view.findViewById(R.id.search_view);
        loadingDotView = view.findViewById(R.id.loading_dot);
        vShadow = view.findViewById(R.id.view_shadow);
        recycler = view.findViewById(R.id.recycler);
        iBack = view.findViewById(R.id.img_back);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView.setTitleSearchView(categoryName);
        loadingDotView.showLoading(true);
        vShadow.setVisibility(View.VISIBLE);
        configRecycler();
        searchView.setTextWatcherListerner(this);
        Character.newInstance().getCharacter(page, categoryId, "", true);
        iBack.setOnClickListener(this);

        switch (categoryId) {

            case 2:
                coordinatorLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_cata_romance_character));
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                coordinatorLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_cata_cosplay_character));
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

        if (handler == null) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (!loadingDotView.isShow()) {
                        loadingDotView.showLoading(true);
                        vShadow.setVisibility(View.VISIBLE);
                    }
                    if (charSequence.length() > 0) {
                        search = charSequence.toString();
                        Character.newInstance().getCharacter(page, categoryId, search, true);
                    } else {
                        search = "";
                        Character.newInstance().getCharacter(1, categoryId, search, true);
                    }
                    handler = null;
                }
            }, 400);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back:
                ((Activity) mContext).finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForListCharacterFragment message) {

        switch (message.getCode()) {

            case MessageForListCharacterFragment.CODE_LOAD_LIST_SUCCESS:
                if (isDataSearch) {
                    isDataSearch = false;
                    page = 1;
                }

                if (message.getCategoryId() == categoryId) {
                    if (page == 1) {
                        lsData.clear();
                        lsData.addAll(message.getData().getData());
                    } else {

                        lsData.remove(lsData.size() - 1);
                        adap.notifyItemRemoved(lsData.size());
                        lsData.addAll(message.getData().getData());
                        adap.setRecyclerLoadMore(false);
                    }
                    adap.notifyDataSetChanged();
                }
                break;
            case MessageForListCharacterFragment.CODE_LOAD_LIST_FAIL:
                if (isDataSearch) {
                    isDataSearch = false;
                    page = 1;
                }
                if (message.getCategoryId() == categoryId) {
                    if (page == 1) {
                        Toast.makeText(mContext, message.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case MessageForListCharacterFragment.CODE_SEARCH_LIST_FAIL:
                isDataSearch = true;
                if (message.getCategoryId() == categoryId) {
                    if (page == 1) {
                        lsData.clear();
                        adap.notifyDataSetChanged();
                        Toast.makeText(mContext, message.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

        loadingDotView.showLoading(false);
        vShadow.setVisibility(View.GONE);
    }

    private void configRecycler() {

        adap = new ListCharacterRecyclerAdapter(mContext, recycler, lsData, R.anim.anim_fade_in);
        recycler.setHasFixedSize(true);
        manager = new GridLayoutManager(mContext, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (adap.getItemViewType(position) == Constants.Recycler.RECYCLER_ITEM_HAS_DATA) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adap);
        adap.setOnLoadMoreListener(new ListCharacterRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadmoreListener() {

                if (totalPage > page) {
                    lsData.add(null);
                    recycler.post(new Runnable() {
                        @Override
                        public void run() {

                            page++;
                            adap.notifyItemInserted(lsData.size() - 1);
                            Character.newInstance().getCharacter(page, categoryId, search, true);
                        }
                    });
                }
            }
        });
        if (lsData.size() > 0) {
            adap.configLoadmore();
        }
    }

    public boolean isSearchBarOpen() {

        return searchView.isSearchOpen();
    }

    public void closeSearchBar() {

        searchView.closeSearch();
    }
}