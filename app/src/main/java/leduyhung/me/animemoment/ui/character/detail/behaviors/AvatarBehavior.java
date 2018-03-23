package leduyhung.me.animemoment.ui.character.detail.behaviors;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.leduyhung.loglibrary.Logg;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarBehavior extends CoordinatorLayout.Behavior<CircleImageView> implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private ImageView iNavigation;
    private CircleImageView circleImageView;
    private CoordinatorLayout.LayoutParams lp;
    private int paddingToolbar;
    private int withImage, withINavigation;
    private int maxOffset, maxWithImage;
    private int heightToolbar;
    private boolean first = true;

    public AvatarBehavior() {
    }

    public AvatarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(
            final CoordinatorLayout parent,
            final CircleImageView child,
            final View dependency) {

        if (withImage == 0) {

            withImage = child.getWidth();
            maxWithImage = withImage * 2;
        }

        if (dependency instanceof Toolbar && heightToolbar == 0) {
            toolbar = (Toolbar) dependency;
            heightToolbar = toolbar.getHeight();

            circleImageView = child;
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

        if (withImage != 0 && lp != null && first) {

            first = false;
            lp.width = maxWithImage;
            lp.height = maxWithImage;
            circleImageView.setLayoutParams(lp);
        }

        return dependency instanceof AppBarLayout; // change if you want another sibling to depend on
    }

    @Override
    public boolean onDependentViewChanged(
            final CoordinatorLayout parent,
            final CircleImageView child,
            final View dependency) {

        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int size;
        int realY = maxOffset + verticalOffset;
        int y = realY - maxWithImage - paddingToolbar / 3;
        if (y < heightToolbar / 2 - withImage / 2) {
            y = heightToolbar / 2 - withImage / 2;
        }

        int x = paddingToolbar - verticalOffset / 3;
        if (x > paddingToolbar / 2 + withINavigation) {
            x = paddingToolbar / 2 + withINavigation;
        }

        size = ((maxOffset + verticalOffset) * maxWithImage) / maxOffset;
        if (size >= maxWithImage)
            size = maxWithImage;
        else if (size <= withImage)
            size = withImage;
        lp.width = size;
        lp.height = size;
        circleImageView.setLayoutParams(lp);

        circleImageView.setY(y);
        circleImageView.setX(x);
    }
}