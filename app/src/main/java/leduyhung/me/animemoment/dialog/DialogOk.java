package leduyhung.me.animemoment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import leduyhung.me.animemoment.R;

public class DialogOk extends Dialog{

    private Context mContext;
    private TextView tContent, tOk;

    public DialogOk(@NonNull Context context) {
        super(context);
        this.mContext = context;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_ok);
        tContent = findViewById(R.id.txt_content);
        tOk = findViewById(R.id.txt_ok);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (((Activity) mContext).isFinishing()) {

            dismiss();
        }
    }

    public void showDialog(String content, boolean fragDetach, boolean cancelAble, final OnDialogClickListener onDialogClickListener) {

        if (!this.isShowing() && !fragDetach && !((Activity)mContext).isFinishing()) {
            this.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = this.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            setCancelable(cancelAble);
            tContent.setText(content);
            tOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onDialogClickListener.onPositiveButtonClick();
                }
            });
        }
    }
}