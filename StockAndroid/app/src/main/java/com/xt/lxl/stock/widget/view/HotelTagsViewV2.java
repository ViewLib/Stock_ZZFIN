package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.StockShowUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by lxl on 2015/12/15;
 * Update by lxl on 2016/04/29.
 */
public class HotelTagsViewV2 extends ViewGroup {

    // 默认间距
    /**
     * 水平方向间隔
     */
    private int mHorizontalSpacing = DeviceUtil.getPixelFromDip(getContext(),
            10);
    /**
     * 竖直方向间隔
     */
    private int mVerticalSpacing = DeviceUtil.getPixelFromDip(getContext(),
            15);

    /**
     * 记录要展示的view
     */
    private List<ArrayList<View>> mViews = new ArrayList<ArrayList<View>>();

    // 基础属性-最大宽度
    private int mMaxWidth = 0;

    /**
     * 最后一个view是控制不显示(true)，还是显示一部分(false)
     */
    private boolean mIsNeedGrep = true;

    /**
     * 是否可换行 不可换行其实就是把最大行数设置为1
     */
    private boolean mIsCanMulti = false;

    /**
     * 必定显示的view，当一行显示不下时，该view在第二行展示. mCanWrap属性设置为true的时候，该属性无效
     */
    private View mMainView;

    /**
     * 最大行数
     */
    private int mMaxLine = 3;//

    /**
     * 是否自适应宽度
     */
    private boolean mWarpWidth = false;//

    public HotelTagsViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViews.clear();

        int maxShowWidth = mMaxWidth;
        if (maxShowWidth <= 0) {
            maxShowWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        // int maxShowHeight =
        // MeasureSpec.getSize(heightMeasureSpec);//高度还是不要设置了

        /** current line */
        int N = this.getChildCount();
        if (N == 0) {
            return;
        }

        int maxWidth = 0;
        int maxHeight = 0;

        /** each padding value */
        int paddingTop = this.getPaddingTop();
        int paddingBottom = this.getPaddingBottom();
        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();

        int[] params;
        if (mMainView == null) {
            params = measureMultiLine(N, maxShowWidth, paddingLeft,
                    paddingRight);
            maxWidth += (paddingRight + paddingLeft);
            maxHeight += (paddingTop + paddingBottom);
        } else {
            params = measureHaveMainViewLine(N, maxShowWidth, paddingLeft,
                    paddingRight);
            maxWidth += (paddingRight + paddingLeft);
            maxHeight += (paddingTop + paddingBottom);
        }
        // 设置view宽和高
        maxWidth = mWarpWidth ? params[0] : maxShowWidth;
        maxHeight = params[1];
        setMeasuredDimension(maxWidth, maxHeight);
    }

    /**
     * 指定了mMainView，会返回两行
     */
    private int[] measureHaveMainViewLine(int N, int maxShowWidth,
                                          int paddingLeft, int paddingRight) {

        ArrayList<View> line0 = new ArrayList<View>();
        mViews.add(line0);

        List<View> prepareViews = new ArrayList<View>();
        HashMap<View, int[]> whMap = new HashMap<View, int[]>();
        int aWidth = 0;
        int pWidth = 0;
        int maxWidth = 0;
        int maxHeight = 0;
        int horizontalWidth = 0;

        // 遍历分类普通和预存views
        for (int i = 0; i < N; i++) {
            View childView = this.getChildAt(i);
            measureChildView(childView);
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();
            if (i > 0) {
                horizontalWidth = mHorizontalSpacing;
            }
            if (childView == mMainView || prepareViews.size() > 0) {
                prepareViews.add(childView);
                pWidth += (measuredWidth + horizontalWidth);
            } else {
                line0.add(childView);
                aWidth += (measuredWidth + horizontalWidth);
            }
            int[] wh = new int[2];
            wh[0] = measuredWidth;
            wh[1] = measuredHeight;
            whMap.put(childView, wh);
        }

        if ((paddingLeft + paddingRight + aWidth + pWidth) <= maxShowWidth) {
            // 可以完全展示，一行展示
            line0.addAll(prepareViews);
        } else if (prepareViews.size() > 0) {
            // 不能完全展示，mainView二行展示
            View remove = prepareViews.remove(0);
            int[] removewh = whMap.get(remove);
            maxHeight += remove.getMeasuredHeight() + mVerticalSpacing;
            maxWidth = remove.getMeasuredWidth();
            maxWidth = removewh[0];

            ArrayList<View> line1 = new ArrayList<View>();
            line1.add(remove);
            mViews.add(line1);

            for (View view : prepareViews) {
                int[] wh = whMap.get(view);
                if ((aWidth + wh[0]) <= maxShowWidth) {
                    // 判断是否为第一个
                    aWidth += (wh[0] + (line0.size() > 0 ? horizontalWidth : 0));
                    line0.add(view);
                } else {
                    // 根据标记位判断是否添加最后一个
                    if (!mIsNeedGrep) {
                        line0.add(view);
                    }
                    break;
                }
            }
        }

        /** 宽度为第一行的宽度 */
        int realWidth = 0;
        int realHeight = 0;
        if (line0.size() > 0) {
            int INDEX = line0.size() - 1;
            for (int index = 0; index <= INDEX; index++) {
                realWidth += line0.get(index).getMeasuredWidth()
                        + mHorizontalSpacing;
                realHeight = Math.max(realHeight, line0.get(index)
                        .getMeasuredHeight());
            }
        }
        maxHeight += realHeight;
        maxWidth = Math.max(maxWidth, realWidth);

        return new int[]{maxWidth, maxHeight};
    }

    private int[] measureMultiLine(int N, int maxShowWidth, int paddingLeft,
                                   int paddingRight) {
        int[] maxParams = new int[2];

        int currentLine = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int verticalHeight = 0;
        int horizontalWidth = 0;
        boolean isFirst = true;

        for (int i = 0; i < N; i++) {
            View childView = this.getChildAt(i);
            measureChildView(childView);
            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();

            // 判断是否需要新建一行
            ArrayList<View> arrayList;

            if ((maxShowWidth - lineWidth - paddingRight - paddingLeft) >= measuredWidth
                    + horizontalWidth) {

                lineHeight = Math.max(lineHeight, measuredHeight);
                lineWidth += (measuredWidth + horizontalWidth);
                // 每行的第一个宽度间距为0，第二个起计算宽度间距
                horizontalWidth = mHorizontalSpacing;
            } else if (!mIsNeedGrep && isFirst) {
                isFirst = false;
                lineHeight = Math.max(lineHeight, measuredHeight);
                lineWidth += (measuredWidth + horizontalWidth);
                horizontalWidth = mHorizontalSpacing;
            } else {
                isFirst = true;
                maxParams[0] = maxParams[0] > lineWidth ? maxParams[0]
                        : lineWidth;
                maxParams[1] += (lineHeight + verticalHeight);
                verticalHeight = mVerticalSpacing;// 第二行开始每行增加间距
                if (++currentLine >= mMaxLine) {
                    return maxParams;
                }
                lineWidth = measuredWidth;
                lineHeight = measuredHeight;
            }

            if (mViews.size() > currentLine) {
                arrayList = mViews.get(currentLine);
            } else {
                arrayList = new ArrayList<View>();
                mViews.add(arrayList);
            }
            arrayList.add(childView);
        }

        maxParams[0] = maxParams[0] > lineWidth ? maxParams[0] : lineWidth;
        maxParams[1] += (lineHeight + verticalHeight);// 加上最后一行的最大高度
        return maxParams;
    }

    private void measureChildView(View childView) {
        int cwMeasureSpec = MeasureSpec.UNSPECIFIED;
        int chMeasureSpec = MeasureSpec.UNSPECIFIED;
        LayoutParams lp = childView.getLayoutParams();
        if (lp.width >= 0) {
            cwMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width,
                    MeasureSpec.AT_MOST);
        }
        if (lp.height >= 0) {
            chMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height,
                    MeasureSpec.AT_MOST);
        }
        childView.measure(cwMeasureSpec, chMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lineHeight = 0;
        int startHeight = top;

        for (ArrayList<View> list : mViews) {
            for (View view : list) {
                int viewLeftMargin = 0;
                if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams viewLayoutParams = (LinearLayout.LayoutParams) view
                            .getLayoutParams();
                    viewLeftMargin = viewLayoutParams.leftMargin;
                }

                int measuredHeight = view.getMeasuredHeight();
                int measuredWidth = view.getMeasuredWidth();
                if (viewLeftMargin < 0) {
                    view.layout(viewLeftMargin + left - mHorizontalSpacing,
                            startHeight, viewLeftMargin + left + measuredWidth,
                            startHeight + measuredHeight);
                } else {
                    view.layout(left, startHeight, left + measuredWidth,
                            startHeight + measuredHeight);
                }
                left += (view.getMeasuredWidth() + mHorizontalSpacing);
                lineHeight = Math.max(measuredHeight, lineHeight);
            }
            startHeight = startHeight + lineHeight + mVerticalSpacing;
            left = getPaddingLeft();
        }

    }

    /**
     * 是否支持多行
     *
     * @param isCanMulti
     * @return
     */
    public HotelTagsViewV2 initIsCanMulti(boolean isCanMulti) {
        this.mIsCanMulti = isCanMulti;
        if (!mIsCanMulti) {
            mMaxLine = 1;
        }
        return this;
    }

    /**
     * mainView如果显示不开，则第二行显示
     *
     * @param view
     */
    public HotelTagsViewV2 initMainTagView(View view) {
        this.mMainView = view;
        return this;
    }

}