package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.xt.lxl.stock.R;


/**
 * An extension to TextView that supports the {@link android.widget.Checkable} interface.
 * This is useful when used in a {@link android.widget.ListView ListView} where the it's
 * {@link android.widget.ListView#setChoiceMode(int) setChoiceMode} has been set to
 * something other than {@link android.widget.ListView#CHOICE_MODE_NONE CHOICE_MODE_NONE}.
 */
public class StockCheckedTextView extends android.widget.TextView implements android.widget.Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private boolean bIsChecked;
    private int nCheckMarkResource;
    private Drawable mCheckMarkDrawable;
    private int mWidth;
    private int mHeight;

    public StockCheckedTextView(Context context) {
        this(context, null);
    }

    public StockCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StockCheckedTextView, 0, 0);

        if (a != null) {
            Drawable d = a.getDrawable(R.styleable.StockCheckedTextView_checkMark);
//            mWidth = a.getDimensionPixelSize(R.styleable.CtripCheckedTextView_check_drawable_width, 0);
//            mHeight = a.getDimensionPixelSize(R.styleable.CtripCheckedTextView_check_drawable_height, 0);
            if (d != null) {
                setCheckMarkDrawable(d);
            }
            boolean checked = a.getBoolean(R.styleable.StockCheckedTextView_checked, false);
            setChecked(checked);

            a.recycle();
        }
    }

    /**
     * <p>
     * Change the checked state of the view to the inverse of its current state
     * </p>
     */
    @Override
    public void toggle() {
        setChecked(!bIsChecked);
    }

    /**
     * @return The current checked state of the view
     */
    @Override
    public boolean isChecked() {
        return bIsChecked;
    }

    /**
     * <p>
     * Changes the checked state of this text view.
     * </p>
     *
     * @param checked true to check the text, false to uncheck it
     */
    @Override
    public void setChecked(boolean checked) {
        if (bIsChecked != checked) {
            bIsChecked = checked;
            refreshDrawableState();
        }
    }

    /**
     * Set the checkmark to a given Drawable, identified by its resourece id.
     * This will be drawn when {@link #isChecked()} is true.
     *
     * @param resid The Drawable to use for the checkmark.
     */
    public void setCheckMarkDrawable(int resid) {
        if (resid != 0 && resid == nCheckMarkResource) {
            return;
        }

        nCheckMarkResource = resid;

        Drawable d = null;
        if (nCheckMarkResource != 0) {
            d = getResources().getDrawable(nCheckMarkResource);
        }
        setCheckMarkDrawable(d);
    }

    /**
     * Set the checkmark to a given Drawable. This will be drawn when
     * {@link #isChecked()} is true.
     *
     * @param d The Drawable to use for the checkmark.
     */
    public void setCheckMarkDrawable(Drawable d) {
        if (mCheckMarkDrawable != null) {
            mCheckMarkDrawable.setCallback(null);
            unscheduleDrawable(mCheckMarkDrawable);
        }
        if (d != null) {
            d.setCallback(this);
            d.setVisible(getVisibility() == VISIBLE, false);
            d.setState(CHECKED_STATE_SET);
            setMinHeight(d.getIntrinsicHeight());
            d.setState(getDrawableState());
            if (mWidth != 0 && mHeight != 0) {
                d.setBounds(0, 0, mWidth, mHeight);
            }
        }
        mCheckMarkDrawable = d;

        setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
    }

    /**
     * Generate the new {@link Drawable} state for
     * this view. This is called by the view
     * system when the cached Drawable state is determined to be invalid.  To
     * retrieve the current state, you should use {@link #getDrawableState}.
     *
     * @param extraSpace if non-zero, this is the number of extra entries you
     *                   would like in the returned array in which you can place your own
     *                   states.
     * @return Returns an array holding the current {@link Drawable} state of
     * the view.
     * @see #mergeDrawableStates(int[], int[])
     */
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /**
     * This function is called whenever the state of the view changes in such
     * a way that it impacts the state of drawables being shown.
     * <p>
     * <p>Be sure to call through to the superclass when overriding this
     * function.
     *
     * @see Drawable#setState(int[])
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mCheckMarkDrawable != null) {
            int[] myDrawableState = getDrawableState();

            // Set the state of the Drawable
            mCheckMarkDrawable.setState(myDrawableState);

            invalidate();
        }
    }
}
