package leduyhung.me.animemoment.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import leduyhung.me.animemoment.R;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View view;
    private Button bLogin, bDemo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);
        bindView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mContext = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_demo:
                EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_DEMO_BUTTON_CLICK, null));
                break;
            case R.id.btn_login_fb:
                EventBus.getDefault().post(new MessageForLoginActivity(MessageForLoginActivity.CODE_FACEBOOK_BUTTON_CLICK, null));
                break;
        }
    }

    private void bindView(View v) {

        bLogin = v.findViewById(R.id.btn_login_fb);
        bDemo = v.findViewById(R.id.btn_demo);
        bLogin.setOnClickListener(this);
        bDemo.setOnClickListener(this);
    }
}