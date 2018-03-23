package leduyhung.me.animemoment.ui.character.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GalleryTransition extends Transition {

    public GalleryTransition() {
    }

    public GalleryTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {

    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {

    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        final ImageView imageView = (ImageView)startValues.view;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                imageView.getLayoutParams().width = (int)valueAnimator.getCurrentPlayTime();
            }
        });
        return animator;
    }
}
