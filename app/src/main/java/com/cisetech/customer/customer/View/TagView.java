package com.cisetech.customer.customer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * Author:Yqy
 * Date:2016-07-28
 * Desc:
 * Company:cisetech
 */
public class TagView extends FrameLayout implements Checkable {
    private boolean mChecked;
    private View targetView;

    public View getTargetView() {
        return getChildAt(0);
    }

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };
    public TagView(Context context) {
        this(context,null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
    /**
     * Sets the checked state of this view.
     *
     * @param checked {@code true} set the state to checked, {@code false} to
     *                uncheck
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void toggle() {
        setChecked(!mChecked);
    }

}
