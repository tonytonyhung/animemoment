package leduyhung.me.animemoment.ui.character.detail.behaviors;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

public class ToolbarBehavior extends CoordinatorLayout.Behavior<Toolbar> implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String colorToolbar;
    private String colorTransparent;
    private int paddingToolbar;

    public ToolbarBehavior() {
    }

    public ToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(
            final CoordinatorLayout parent,
            final Toolbar child,
            final View dependency) {

        if (dependency instanceof AppBarLayout && appBarLayout == null) {
            appBarLayout = (AppBarLayout) dependency;
            appBarLayout.addOnOffsetChangedListener(this);
            toolbar = child;
            colorToolbar = String.format("#%06X", 0xFFFFFF & ((ColorDrawable)toolbar.getBackground()).getColor());
            colorTransparent = colorToolbar.replace("#", "#00");
            paddingToolbar = toolbar.getPaddingBottom();
        }
        return dependency instanceof AppBarLayout; // change if you want another sibling to depend on
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        if (maxScroll + verticalOffset < toolbar.getHeight()) {

            toolbar.setBackgroundColor(Color.parseColor(colorToolbar));
        } else {

            toolbar.setBackgroundColor(Color.parseColor(colorTransparent));
        }
    }
}