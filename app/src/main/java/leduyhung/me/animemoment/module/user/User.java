package leduyhung.me.animemoment.module.user;

import android.content.Context;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.module.user.data.UserInfo;
import leduyhung.me.animemoment.rest.BaseRestApi;
import leduyhung.me.animemoment.ui.login.MessageForLoginActivity;
import leduyhung.me.animemoment.util.CacheUtil;
import leduyhung.me.animemoment.util.ClientUtil;
import leduyhung.me.animemoment.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * HOW TO USE:
 * First: call User.newInstance().addContext(context).isUserLogin() (return boolean) to check user has login or not.
 * Second: call {@link #setActivityResult(int, int, Intent)} in onActivityResult to receive data from facebook
 * To logout, call {@link #logout()}. This function will delete cache user and logout facebook
 * To push only data user to server, call {@link #pushDataToServer()}
 * REMEMBER:
 * if context is NULL, call {@link #addContext(Context)} before use.
 */
public class User implements FacebookCallback<LoginResult> {

    public transient static final String KEY_CACHE_USER = "KEY_CACHE_USER";

    private transient Context mContext;
    private transient static User user;
    private transient CallbackManager callbackManager;
    private UserInfo userInfo;
    private String access_token;

    public static User newInstance() {

        if (user == null)
            user = new User();

        return user;
    }

    User() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    public User addContext(Context ctx) {

        this.mContext = ctx;
        return user;
    }

    public Context getContext() {

        return mContext;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public UserInfo getUserInfo() {
        if (userInfo == null && isUserLogin()) {
            userInfo = GsonUtil.newInstance().fromJson(CacheUtil.newInstance().addContext(mContext).getString(KEY_CACHE_USER, ""), UserInfo.class);
        }
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isUserLogin() {

        return CacheUtil.newInstance().addContext(mContext).getString(KEY_CACHE_USER, "") != "";
    }

    public void logout() {

        CacheUtil.newInstance().addContext(mContext).clearByName(KEY_CACHE_USER);
        LoginManager.getInstance().logOut();
    }

    private void save(UserInfo userInfo) {

        user.setUserInfo(userInfo);
        CacheUtil.newInstance().addContext(mContext).putString(KEY_CACHE_USER, GsonUtil.newInstance().toJson(user));
    }

    public void pushDataToServer() {

        if (user.getAccess_token() != null && user.getAccess_token() != "")
            BaseRestApi.request(mContext).login(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), user).enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                    if (response.isSuccessful()) {

                        user.save(response.body());
                        EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_LOGIN_FACEBOOK_SUCCESS, null));
                    } else {

                        EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_LOGIN_FACEBOOK_FAIL, mContext.getResources()
                                .getString(R.string.server_maintain)));
                        logout();
                    }

                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {

                    if (!ClientUtil.isConnectInternet(mContext)) {

                        EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_LOGIN_FACEBOOK_FAIL, mContext.getResources()
                                .getString(R.string.no_connect_internet)));
                    } else {

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                            EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_LOGIN_FACEBOOK_FAIL, mContext.getResources()
                                    .getString(R.string.server_maintain)));
                        } else {

                            EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_LOGIN_FACEBOOK_FAIL, mContext.getResources()
                                    .getString(R.string.server_maintain)));
                        }
                    }
                    logout();
                }
            });
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

        user.setAccess_token(loginResult.getAccessToken().getToken());
        pushDataToServer();
    }

    @Override
    public void onCancel() {

        EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_LOGIN_FACEBOOK_CANCEL, null));
        logout();
    }

    @Override
    public void onError(FacebookException error) {

        EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_LOGIN_FACEBOOK_FAIL, mContext.getResources()
                .getString(R.string.facebook_login_error)));
        logout();
    }
}