package com.xt.lxl.stock.widget.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
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
 */
public class StockTabGroupButton2 extends LinearLayout {

    protected RadioGroup mRadioGroup;
    protected List<RadioButton> radioButtonList = new ArrayList<>();
    protected LinearLayout mTabAnimView;
    protected View mAnimView0;
    protected int mWidth;
    protected int mPadding;
    private boolean mIsFillScreen = false;
    private int mTabSize;
    protected int mIndex;
    protected OnTabItemSelectedListener mOnTabItemSelectedListener;
    protected List<ShowLocationModel> tabLocationList = new ArrayList<>();

    public StockTabGroupButton2(Context context) {
        this(context, null);
    }

    public StockTabGroupButton2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpViews(context, attrs);
    }

    protected void setUpViews(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.hotel_common_tab2_group_buttton, this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_switch);
        mTabAnimView = (LinearLayout) findViewById(R.id.tab_anim_view);
//        mBottomLine = findViewById(R.id.bottom_line);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mWidth = getMeasuredWidth();
    }

    /**
     * 设置每个Item的Text Value
     *
     * @param itemArray tab item array
     */
    public void setTabItemArrayText(List<String> itemArray) {
        mTabSize = itemArray.size();

        mRadioGroup.removeAllViews();
        mTabAnimView.removeAllViews();

        initShowLocationList(itemArray);
        //获取字体的总宽度
        for (int i = 0; i < tabLocationList.size(); i++) {
            ShowLocationModel locationModel = tabLocationList.get(i);
            RadioButton radioButton = (RadioButton) View.inflate(getContext(), R.layout.hotel_common_tab_group_button_item, null);
            radioButton.setText(locationModel.tab);
            radioButton.setId(i);
            radioButton.setGravity(Gravity.CENTER);
            radioButtonList.add(radioButton);
            mRadioGroup.addView(radioButton, getLinearLayoutLayoutParams(locationModel.leftWidth + locationModel.rightWidth + locationModel.useWidth));
            if (mTabAnimView.getVisibility() == View.VISIBLE) {
                View inflate = View.inflate(getContext(), R.layout.hotel_common_tab2_group_button_anim_item, null);
                LayoutParams linearLayoutLayoutParams = getLinearLayoutLayoutParams(locationModel.useWidth);
                linearLayoutLayoutParams.leftMargin = locationModel.leftWidth;
                mTabAnimView.addView(inflate, linearLayoutLayoutParams);
            }
            radioButton.setTag(locationModel);
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
                    if (radioButton.getId() != checkedId) {
                        continue;
                    }
                    if (mOnTabItemSelectedListener != null) {
                        mOnTabItemSelectedListener.onTabItemClicked(i);
                    }
                    RadioButton baseRadioButton = (RadioButton) group.getChildAt(0);
                    RadioButton oldRadioButton = (RadioButton) group.getChildAt(mIndex);
                    ShowLocationModel baselocationModel = (ShowLocationModel) baseRadioButton.getTag();
                    ShowLocationModel locationModel = (ShowLocationModel) radioButton.getTag();
                    ShowLocationModel oldLocationModel = (ShowLocationModel) oldRadioButton.getTag();
                    mIndex = i;
                    startViewAnimation(baselocationModel, oldLocationModel, locationModel);
                }
            }
        });
    }

    public void initShowLocationList(List<String> showLocationList) {
        tabLocationList.clear();

        Rect bounds = new Rect();
        TextPaint paint = new TextPaint();
        paint.setTextSize(DeviceUtil.sp2px(getContext(), 15));
        int widthSum = 0;
        for (int i = 0; i < showLocationList.size(); i++) {
            String s = showLocationList.get(i);
            ShowLocationModel locationModel = new ShowLocationModel();
            paint.getTextBounds(s, 0, s.length(), bounds);
            int width = bounds.width();
            widthSum += width;
            locationModel.tab = s;
            locationModel.useWidth = width;
            tabLocationList.add(locationModel);
        }
        mPadding = DeviceUtil.getPixelFromDip(getContext(), 10);
        mWidth = DeviceUtil.getScreenWidth(getContext()) - mPadding - mPadding;

        //如果宽度足够，则均分剩余的空间，如果不够，则均分所有空间
        int startLeft = 0;
        if (mWidth > widthSum) {
            int itemWidth = (mWidth - widthSum) / 2 / showLocationList.size();
            for (int i = 0; i < tabLocationList.size(); i++) {
                ShowLocationModel locationModel = tabLocationList.get(i);
                locationModel.leftWidth = itemWidth;
                locationModel.rightWidth = itemWidth;
                locationModel.startLocation = startLeft;
                startLeft = startLeft + locationModel.useWidth + locationModel.leftWidth + locationModel.rightWidth;
            }
        } else {
            for (int i = 0; i < tabLocationList.size(); i++) {
                ShowLocationModel locationModel = tabLocationList.get(i);
                locationModel.useWidth = mWidth / tabLocationList.size();
                startLeft = startLeft + locationModel.useWidth;
            }
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

    protected void startViewAnimation(ShowLocationModel baselocationModel, ShowLocationModel oldLocationModel, ShowLocationModel locationModel) {
        // True:图片停在动画结束位置
        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(mAnimView0, "translationX", oldLocationModel.startLocation, locationModel.startLocation);
        float scaleX = (float) locationModel.useWidth / (float) baselocationModel.useWidth;
        Log.i("lxltest", "translationX:" + oldLocationModel.startLocation + ",new:" + locationModel.startLocation);
        Log.i("lxltest", "scaleX:" + scaleX);
        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(mAnimView0, "scaleX", 1f, scaleX);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(moveAnim).with(scaleAnim);
        mAnimView0.setPivotX(0);
//        animSet.play(moveAnim);
        animSet.setDuration(300);
        animSet.start();
    }

    public LayoutParams getLinearLayoutLayoutParams(int width) {
        LayoutParams lp = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
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

    public int getmTabSize() {
        return mTabSize;
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

    class ShowLocationModel {
        String tab = "";
        int useWidth;//字体使用的宽度
        int leftWidth;//左侧宽度
        int rightWidth;//右侧宽度
        int startLocation;//字体开始的位置
        //动画开始的位置 = startLocation + leftWidth
    }

}
