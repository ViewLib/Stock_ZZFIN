package com.xt.lxl.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.xt.lxl.stock.util.HotelCommonRecycleBin;

/**
 * Created by xiangleiliu on 2017/8/27.
 * 封装好的展示涨跌幅的text
 */
public class StockChangeText extends View {

    private HotelLabelDrawable mLeftDiaplayDrawable = null;
    private HotelLabelDrawable mRightDiaplayDrawable = null;
    protected int mWidth;
    protected int mHeight;

    public StockChangeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StockChangeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void recycle(HotelCommonRecycleBin recycleBin) {
        recycle(mLeftDiaplayDrawable, recycleBin);
        recycle(mRightDiaplayDrawable, recycleBin);
    }

    private static void recycle(HotelLabelDrawable drawables, HotelCommonRecycleBin recycleBin) {
        recycleBin.addScrapObject(drawables);
    }

    public void refreshLabelDrawables(HotelLabelDrawable leftDrawable, HotelLabelDrawable rightDrawable) {
        mLeftDiaplayDrawable = leftDrawable;
        mRightDiaplayDrawable = rightDrawable;
        requestLayout();
        invalidate();
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getMode(heightMeasureSpec);
        //计算左边的宽高
        mLeftDiaplayDrawable.measure(mWidth / 2, heightMeasureSpec);
        //计算右边的宽高
        mRightDiaplayDrawable.measure(mWidth / 2, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutDrawables();
    }

    private void layoutDrawables() {
        int offsetX = 0;
        int offsetY = 0;
        int middleX = mWidth / 2;
        int endX = mWidth;
//        int leftMeasuredWidth = mLeftDiaplayDrawable.getMeasuredWidth();
        int leftMeasuredHeight = mLeftDiaplayDrawable.getMeasuredHeight();
//        int rightMeasuredWidth = mRightDiaplayDrawable.getMeasuredWidth();
        int rightMeasuredHeight = mRightDiaplayDrawable.getMeasuredHeight();
        mLeftDiaplayDrawable.layout(offsetX, offsetY, middleX, leftMeasuredHeight);
        mRightDiaplayDrawable.layout(middleX, offsetY, mWidth, rightMeasuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLeftDiaplayDrawable.draw(canvas);
        mRightDiaplayDrawable.draw(canvas);
    }
}
