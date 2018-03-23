package leduyhung.me.animemoment.ui.character.detail.behaviors;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leduyhung.loglibrary.Logg;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameBehavior extends CoordinatorLayout.Behavior<TextView> implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private ImageView iNavigation;
    private CircleImageView circleImageView;
    private TextView tName;
    private CoordinatorLayout.LayoutParams lp;
    private int paddingToolbar;
    private int withINavigation;
    private int withImage;
    private int maxOffset, maxWithImage;
    private int heightToolbar;
    private float textSize, maxTextSize, minTextSize;
    private int maxX;
    private boolean first = true;

    public NameBehavior() {
    }

    public NameBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(
            final CoordinatorLayout parent,
            final TextView child,
            final View dependency) {

        if (dependency instanceof CircleImageView && maxWithImage == 0) {

            tName = child;
            textSize = tName.getTextSize();
            maxTextSize = 6 * textSize / 5;
            minTextSize = 5 * textSize / 6;
            circleImageView = (CircleImageView) dependency;
            withImage = circleImageView.getWidth();
            maxWithImage = withImage * 2;
        }

        if (dependency instanceof Toolbar && heightToolbar == 0) {
            toolbar = (Toolbar) dependency;
            heightToolbar = toolbar.getHeight();

            constraintLayout = (ConstraintLayout) toolbar.getChildAt(0);
            iNavigation = (ImageView) constraintLayout.getChildAt(0);
            withINavigation = iNavigation.getWidth();
            paddingToolbar = iNavigation.getWidth() / 2;
            lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        }

        if (dependency instanceof AppBarLayout) {

            appBarLayout = (AppBarLayout) dependency;
            appBarLayout.addOnOffsetChangedListener(this);
            maxOffset = appBarLayout.getTotalScrollRange();
        }

        if (maxWithImage != 0 && lp != null && first) {

            first = false;
            tName.setTextSize(maxTextSize);
            maxX =  3 * paddingToolbar / 2 + maxWithImage;
        }

        return dependency instanceof AppBarLayout; // change if you want another sibling to depend on
    }

    @Override
    public boolean onDependentViewChanged(
            final CoordinatorLayout parent,
            final TextView child,
            final View dependency) {

        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int realY = maxOffset + verticalOffset;
        int y = realY - (3 * withImage / 2);
        if (y < heightToolbar / 2 - withImage / 2) {
            y = heightToolbar / 2 - withImage / 2;
        }

        tName.setY(y);
        tName.setX(maxX);
    }
}