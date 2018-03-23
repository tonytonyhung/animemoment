package leduyhung.me.animemoment.module.category;

import android.content.Context;
import android.util.Log;

import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.db.Appdatabase;
import leduyhung.me.animemoment.db.CacheDatabaseManage;
import leduyhung.me.animemoment.module.category.data.CategoryInfo;
import leduyhung.me.animemoment.rest.BaseRestApi;
import leduyhung.me.animemoment.ui.login.category.CategoryFragment;
import leduyhung.me.animemoment.ui.login.category.MessageForCategoryFragment;
import leduyhung.me.animemoment.util.ClientUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category {

    private Context mContext;
    private static Category category;
    private int countTask;

    private List<CategoryInfo> categoryInfo;

    public static Category newInstance() {

        if (category == null) {

            category = new Category();
        }

        return category;
    }

    public Category() {
        countTask = 0;
    }

    public void addContext(Context ctx) {

        this.mContext = ctx;
    }

    public List<CategoryInfo> getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(List<CategoryInfo> categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public void getData(final boolean needEvent) {

        countTask++;
        categoryInfo = Appdatabase.newInstance(mContext).categoryDao().getAllDataCategory();
        if (categoryInfo != null && categoryInfo.size() > 0 && !CacheDatabaseManage.newInstance().addContext(mContext).check(CacheDatabaseManage.TAG_CATEGORY)) {

            EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_SUCCESS, null, categoryInfo));
            countTask--;
            categoryInfo = null;
        } else {

            BaseRestApi.request(mContext).getAllCategory(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext))
                    .enqueue(new Callback<List<CategoryInfo>>() {
                        @Override
                        public void onResponse(Call<List<CategoryInfo>> call, Response<List<CategoryInfo>> response) {

                            if (response.isSuccessful() && response.body().size() > 0) {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_SUCCESS, null, response.body()));
                                for (CategoryInfo cate : response.body()) {
                                    CacheDatabaseManage.newInstance().addContext(mContext).save(CacheDatabaseManage.TAG_CATEGORY, cate);
                                }
                            } else {

                                if (needEvent) {

                                    if (response.code() == 403) {
                                        EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.NEED_TO_UPDATE, mContext.getResources().getString(R.string.app_need_update), null));

                                    } else
                                        EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_FAIL, mContext.getResources().getString(R.string.server_maintain), null));

                                }
                            }
                            countTask--;
                        }

                        @Override
                        public void onFailure(Call<List<CategoryInfo>> call, Throwable t) {

                            if (!ClientUtil.isConnectInternet(mContext)) {

                                if (needEvent)
                                    EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_FAIL,
                                            mContext.getResources().getString(R.string.no_connect_internet), null));
                            } else {

                                if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain), null));
                                } else {

                                    if (needEvent)
                                        EventBus.getDefault().post(new MessageForCategoryFragment(MessageForCategoryFragment.CODE_GET_LIST_CATEGORY_FAIL,
                                                mContext.getResources().getString(R.string.server_maintain), null));
                                }
                            }

                            countTask--;
                        }
                    });
        }
    }

    public int getCountTask() {

        return countTask;
    }
}