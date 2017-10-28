package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 带有2个按钮的Tab Group Buttton ，切换时带有动画效果
 *
 * @author zhiwen.nan
 * @version 5.3
 * @see RadioGroup
 * @since 2013.12.16
 */
public class StockTabGroupButton extends LinearLayout {

    protected RadioGroup mRadioGroup;
    protected List<RadioButton> radioButtonList = new ArrayList<>();
    protected LinearLayout mTabAnimView;
    protected View mAnimView0;
    protected int mWidth;
    protected int mPadding;
    private boolean mIsFillScreen = false;
    private int mTabSize;
    protected Animation mAnimation;
    protected int mIndex;
    protected OnTabItemSelectedListener mOnTabItemSelectedListener;
    private View mBottomLine;

    public StockTabGroupButton(Context context) {
        this(context, null);
    }

    public StockTabGroupButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpViews(context, attrs);
    }

    protected void setUpViews(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.hotel_common_tab_group_buttton, this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_switch);
        mTabAnimView = (LinearLayout) findViewById(R.id.tab_anim_view);
        mBottomLine = findViewById(R.id.bottom_line);
    }

    public void initView() {
        mPadding = getPaddingLeft();//这里pidding为空，所以有问题
        mWidth = DeviceUtil.getScreenWidth(getContext()) - mPadding - getPaddingRight();
        mTabAnimView.removeAllViews();
        //添加animView
        if (mTabAnimView.getVisibility() == View.VISIBLE) {
            int childCount = mRadioGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View inflate = View.inflate(getContext(), R.layout.hotel_common_tab_group_button_anim_item, null);
                mTabAnimView.addView(inflate, getLinearLayoutLayoutParams());
            }
        }
        if (mTabAnimView.getChildCount() > 0) {
            mAnimView0 = mTabAnimView.getChildAt(0);
            mAnimView0.setVisibility(View.VISIBLE);
        } else {
            mAnimView0 = new View(getContext());
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (radioButton.getId() == checkedId) {
                        if (mOnTabItemSelectedListener != null) {
                            mOnTabItemSelectedListener.onTabItemClicked(i);
                        }
                        int fromDelata = mWidth / childCount * mIndex;
                        mIndex = i;
                        int toXDelta = mWidth / childCount * i;
                        mAnimation = new TranslateAnimation(fromDelata, toXDelta, 0, 0);
                        startAnimation();
                    }
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPadding = getPaddingLeft();//这里pidding为空，所以有问题
        mWidth = getMeasuredWidth();
    }

    /**
     * 设置每个Item的Text Value
     *
     * @param itemArray tab item array
     */
    public void setTabItemArrayText(List<String> itemArray) {
        mTabSize = itemArray.size();
        mRadioGroup.removeAllViews();
        for (int i = 0; i < itemArray.size(); i++) {
            String str = itemArray.get(i);
            RadioButton radioButton = (RadioButton) View.inflate(getContext(), R.layout.hotel_common_tab_group_button_item, null);
            radioButton.setText(str);
            radioButton.setId(i);
            radioButton.setGravity(Gravity.CENTER);
            radioButtonList.add(radioButton);
            mRadioGroup.addView(radioButton, getLinearLayoutLayoutParams());
        }
    }

    /**
     * 设置RadioGroup的Background
     *
     * @param res
     */
    public void setBackgroundWithTabGroup(int res) {
        mRadioGroup.setBackgroundResource(res);
    }

    /**
     * 设置RadioGroup的padding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setPaddingWithTabGroup(int left, int top, int right, int bottom) {
        mRadioGroup.setPadding(left, top, right, bottom);
    }

    /**
     * 设置view的宽度, 用于位移
     *
     * @param width
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * 设置默认选项卡
     *
     * @param position 0：第一项 1：第二项 2：第三项
     */
    public void setDefaultTab(int position) {
        int childCount = mRadioGroup.getChildCount();
        if (position >= childCount) {
            return;
        }
        RadioButton button = (RadioButton) mRadioGroup.getChildAt(position);
        button.setSelected(true);
    }

    /**
     * 功能描述:隐藏底部线条
     * <pre>
     *     youj:   2013-12-30      新建
     * </pre>
     */
    public void hineBottomLine() {
        if (null != mBottomLine)
            mBottomLine.setVisibility(View.GONE);
    }

    /**
     * 功能描述: 隐藏底部动画
     * <pre>
     *     zhuc:   2015-10-27      新建
     * </pre>
     */
    public void hideAniwView() {
        if (null != mTabAnimView)
            mTabAnimView.setVisibility(View.INVISIBLE);
    }

    /**
     * 功能描述: 展示底部动画
     * <pre>
     *     zhuc:   2015-10-27      新建
     * </pre>
     */
    public void showAniwView() {
        if (null != mTabAnimView)
            mTabAnimView.setVisibility(View.VISIBLE);
    }

    protected void startAnimation() {
        // True:图片停在动画结束位置
        mAnimation.setFillAfter(true);
        mAnimation.setDuration(300);
        mAnimView0.startAnimation(mAnimation);
    }

    public LinearLayout.LayoutParams getLinearLayoutLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, DeviceUtil.getPixelFromDip(getContext(), -1));
        lp.weight = 1;
        return lp;
    }


    public int getIndex() {
        return mIndex;
    }


    /**
     * 设置 TabGroup对外回调接口，供外部回调使用
     *
     * @param onTabItemSelectedListener TabGroup 对外回调接口
     */
    public void setOnTabItemSelectedListener(OnTabItemSelectedListener onTabItemSelectedListener) {
        mOnTabItemSelectedListener = onTabItemSelectedListener;
    }

    /**
     * Tab Group 回调接口
     */
    public interface OnTabItemSelectedListener {

        /**
         * 当点击button时的外部事件处理
         *
         * @param whichButton 对应点击的那个Item button
         */
        void onTabItemClicked(int whichButton);
    }
}
