package leduyhung.me.animemoment.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.leduyhung.loglibrary.Logg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import leduyhung.me.animemoment.AnimeApplication;
import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.dialog.DialogOk;
import leduyhung.me.animemoment.dialog.OnDialogClickListener;
import leduyhung.me.animemoment.module.category.Category;
import leduyhung.me.animemoment.module.user.User;
import leduyhung.me.animemoment.ui.login.category.CategoryFragment;
import leduyhung.me.animemoment.util.CacheUtil;
import leduyhung.view.myprogress.loading.dot.LoadingDotView;

public class LoginActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private LoginFragment loginFragment;
    private CategoryFragment categoryFragment;
    private RelativeLayout relativeLayout;
    private LoadingDotView loadingDotView;
    private DialogOk dialogOk;

    private int clickBack = 0;
    private boolean isFacebookHasClick;
    private boolean requestPermissionComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        relativeLayout = findViewById(R.id.frame);
        loadingDotView = findViewById(R.id.loading_dot);
        Category.newInstance().addContext(this);

        requestPermisson();

        EventBus.getDefault().register(this);
        isFacebookHasClick = false;
        dialogOk = new DialogOk(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (requestPermissionComplete && loginFragment == null && categoryFragment == null) {

            if (!User.newInstance().addContext(this).isUserLogin()) {

                showLoginScreen();
            } else {
                showCategoryScreen();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        dialogOk.dismiss();
    }

    @Override
    public void onBackPressed() {

        if (categoryFragment == null || !categoryFragment.isAdded()) {
            clickBack++;
            if (clickBack == 2) {

                super.onBackPressed();
            } else {

                Toast.makeText(this, getResources().getString(R.string.tap_again_to_exit), Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        clickBack = 0;
                    }
                }, 1500);
            }
        } else {

            if (!User.newInstance().addContext(this).isUserLogin()) {
                showLoginScreen();
            } else {

                clickBack++;
                if (clickBack == 2) {

                    super.onBackPressed();
                } else {

                    Toast.makeText(this, getResources().getString(R.string.tap_again_to_exit), Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            clickBack = 0;
                        }
                    }, 1500);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10000:
                if (!checkPermission())
                    requestPermisson();
                else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (!User.newInstance().addContext(this).isUserLogin()) {

                        showLoginScreen();
                    } else {
                        showCategoryScreen();
                    }
                } else {

                    requestPermissionComplete = true;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(MessageForLoginActivity message) {

        switch (message.getCode()) {

            case MessageForLoginActivity.CODE_FACEBOOK_BUTTON_CLICK:
                if (!isFacebookHasClick) {
                    loadingDotView.showLoading(true);
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                    isFacebookHasClick = true;
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                }
                break;
            case MessageForLoginActivity.CODE_DEMO_BUTTON_CLICK:
                if (!loadingDotView.isShow()) {
                    dialogOk.showDialog(getResources().getString(R.string.you_will_see_more_advertise), false, true, new OnDialogClickListener() {
                        @Override
                        public void onPositiveButtonClick() {

                            showCategoryScreen();
                            dialogOk.dismiss();

                        }

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                }
                break;
            case MessageForLoginActivity.CODE_LOGIN_FACEBOOK_SUCCESS:
                isFacebookHasClick = false;
                loadingDotView.showLoading(false);
                relativeLayout.setBackground(null);
                showCategoryScreen();
                break;
            case MessageForLoginActivity.CODE_LOGIN_FACEBOOK_CANCEL:
                isFacebookHasClick = false;
                loadingDotView.showLoading(false);
                relativeLayout.setBackground(null);
                break;
            case MessageForLoginActivity.CODE_LOGIN_FACEBOOK_FAIL:
                Snackbar.make(relativeLayout, message.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                User.newInstance().pushDataToServer();
                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light)).show();
                isFacebookHasClick = false;
                loadingDotView.showLoading(false);
                relativeLayout.setBackground(null);
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        User.newInstance().setActivityResult(requestCode, resultCode, data);
    }

    private synchronized void showLoginScreen() {

        if (loginFragment == null)
            loginFragment = new LoginFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, loginFragment);
        try {
            transaction.commit();
        } catch (Exception e) {

            Logg.error(getClass(), e.toString());
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e1) {

                Logg.error(getClass(), e1.toString());
            }
        }
    }

    private synchronized void showCategoryScreen() {

        if (categoryFragment == null) {
            categoryFragment = new CategoryFragment();
        }
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, categoryFragment);
        transaction.commit();
    }

    private void requestPermisson() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10000);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}