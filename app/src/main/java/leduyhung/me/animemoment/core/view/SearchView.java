package leduyhung.me.animemoment.core.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import leduyhung.me.animemoment.R;
import leduyhung.me.animemoment.util.ClientUtil;

public class SearchView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private ImageView iSearch;
    private EditText eSearch;
    private TextView tTitle;
    private Animation aTitle, aEtxt;


    private int widthESearch, heightESearch, widthISearch, heightISearch;

    public SearchView(Context context) {
        super(context);
        bindView(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bindView(context, attrs);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindView(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        eSearch.getLayoutParams().width = widthESearch;
        eSearch.getLayoutParams().height = heightESearch;
        iSearch.getLayoutParams().width = widthISearch;
        iSearch.getLayoutParams().height = heightISearch;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void bindView(Context context, AttributeSet attrs) {

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_search_view, this, true);
        tTitle = findViewById(R.id.txt_title);
        eSearch = findViewById(R.id.etxt_search);
        iSearch = findViewById(R.id.img_search);
        iSearch.setOnClickListener(this);
        eSearch.setImeActionLabel("Search", EditorInfo.IME_ACTION_UNSPECIFIED);

        if (attrs != null) {

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
            widthESearch = typedArray.getDimensionPixelSize(R.styleable.SearchView_width_etxt_search,
                    context.getResources().getDimensionPixelSize(R.dimen.width_search_view));
            heightESearch = typedArray.getDimensionPixelSize(R.styleable.SearchView_height_etxt_search, 0);
            widthISearch = typedArray.getDimensionPixelSize(R.styleable.SearchView_width_ic_search, 0);
            heightISearch = typedArray.getDimensionPixelSize(R.styleable.SearchView_height_ic_search, 0);
            typedArray.recycle();
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.img_search) {

            setAnimationView(eSearch.getVisibility() == GONE);
            if (eSearch.getVisibility() == VISIBLE) {

                eSearch.setText("");
                ClientUtil.hideSoftKeyboard((Activity) mContext);
            } else {

                eSearch.requestFocus();
                ClientUtil.showSoftKeyboard((Activity) mContext);
            }
        }
    }

    private void setAnimationView(final boolean open) {


        if (open) {

            aTitle = AnimationUtils.loadAnimation(mContext, R.anim.anim_title_search_view_hide);
            aEtxt = AnimationUtils.loadAnimation(mContext, R.anim.anim_etxt_search_view_show);
        } else {

            aTitle = AnimationUtils.loadAnimation(mContext, R.anim.anim_title_search_view_show);
            aEtxt = AnimationUtils.loadAnimation(mContext, R.anim.anim_etxt_search_view_hide);
        }
        aTitle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (open)
                    openSearch();
                else
                    closeSearch();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tTitle.startAnimation(aTitle);
        eSearch.startAnimation(aEtxt);
    }

    public void setTextWatcherListerner(TextWatcher textWatcherListerner) {

        eSearch.addTextChangedListener(textWatcherListerner);
    }

    public boolean isSearchOpen() {

        return eSearch.getVisibility() == VISIBLE;
    }

    public void openSearch() {
        eSearch.setVisibility(VISIBLE);
        iSearch.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_cancel));
        eSearch.requestFocus();
        tTitle.setVisibility(GONE);
    }

    public void closeSearch() {

        tTitle.setVisibility(VISIBLE);
        eSearch.setVisibility(GONE);
        iSearch.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_search));
        eSearch.setText("");
    }

    public void setTitleSearchView(String title) {

        tTitle.setText(title);
    }
}