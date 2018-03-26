package leduyhung.me.animemoment.ui.login.category;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.dialog.DialogOk;
import leduyhung.me.animemoment.dialog.OnDialogClickListener;
import leduyhung.me.animemoment.module.category.Category;
import leduyhung.me.animemoment.module.category.data.CategoryInfo;
import leduyhung.me.animemoment.module.character.Character;
import leduyhung.me.animemoment.ui.character.CharacterActivity;
import leduyhung.me.animemoment.ui.login.category.adapter.CatelogyRecyclerAdapter;
import leduyhung.me.animemoment.util.ClientUtil;
import leduyhung.me.animemoment.util.ToastUtil;

public class CategoryFragment extends Fragment {

    public static final String INTENT_ID_CATEGORY = "INTENT_ID_CATEGORY";
    public static final String INTENT_NAME_CATEGORY = "INTENT_NAME_CATEGORY";
    public static final String INTENT_TYPE_CATEGORY = "INTENT_TYPE_CATEGORY";

    private Context mContext;
    private List<CategoryInfo> lsData;

    private View view;
    private DialogOk dialogOk;
    private RecyclerView recycler;
    private LayoutManager manager;
    private CatelogyRecyclerAdapter adap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogOk = new DialogOk(mContext);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        dialogOk.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecycler(view);
        Character.newInstance().addContext(mContext);
        Category.newInstance().getData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForCategoryFragment messageForCategoryFragment) {

        switch (messageForCategoryFragment.getCode()) {

            case MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_SUCCESS:
                lsData.addAll(messageForCategoryFragment.getData());
                adap.notifyDataSetChanged();
                for (CategoryInfo response : messageForCategoryFragment.getData()) {
                    loadCharacter(response.getId());
                }
                break;
            case MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_FAIL:
                ToastUtil.newInstance().showToast(mContext, messageForCategoryFragment.getMessage(), Toast.LENGTH_SHORT);
                break;
            case MessageForCategoryFragment.NEED_TO_UPDATE:
                dialogOk.showDialog(messageForCategoryFragment.getMessage(), isDetached(), false, new OnDialogClickListener() {
                    @Override
                    public void onPositiveButtonClick() {

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ClientUtil.getPakageName(mContext))));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + ClientUtil.getPakageName(mContext))));
                        } finally {

                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }

                    @Override
                    public void onNegativeButtonClick() {

                    }
                });
                break;
            case MessageForCategoryFragment.CODE_CLICK_ITEM_CATEGORY:
                Intent intent = new Intent(mContext, CharacterActivity.class);
                intent.putExtra(INTENT_ID_CATEGORY, messageForCategoryFragment.getId());
                intent.putExtra(INTENT_NAME_CATEGORY, messageForCategoryFragment.getName());
                intent.putExtra(INTENT_TYPE_CATEGORY, messageForCategoryFragment.getType());
                startActivity(intent);
                break;
        }
    }

    private void initRecycler(View v) {

        lsData = new ArrayList();
        recycler = v.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        adap = new CatelogyRecyclerAdapter(mContext, lsData, R.anim.anim_fade_in);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adap);
    }

    private void loadCharacter(int categoryId) {

        Character.newInstance().getCharacter(1, categoryId, "", false);
    }
}