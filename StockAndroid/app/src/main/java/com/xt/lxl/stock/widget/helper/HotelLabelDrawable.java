package com.xt.lxl.stock.widget.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;

import com.xt.lxl.stock.model.model.StockIndexChangeModel;
import com.xt.lxl.stock.util.DeviceUtil;

/**
 * Created by wang_zx on 2015/12/17.
 */
public class HotelLabelDrawable extends Drawable {

    private final static float DEFAULT_FONT_SIZE = 10f;
    protected int mMeasuredWidth = 0;
    protected int mMeasuredHeight = 0;

    Context mContext;

    private Paint mFramePaint = new Paint();
    private Paint mMainBackgroundPaint = new Paint();

    private HotelLabelTextLayoutMaker mTextLayoutMaker = HotelLabelTextLayoutMaker.getInstance();
    private Layout mMainTextLayout;

    private float mLabelRadius = 0;

    private StockIndexChangeModel mViewModel = new StockIndexChangeModel();

    public HotelLabelDrawable(Context context) {
        mContext = context;
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setAntiAlias(true);
        mMainBackgroundPaint.setAntiAlias(true);
    }

    public void setLabelModel(StockIndexChangeModel changeModel) {
        mViewModel = changeModel;
        nullLayouts();
        invalidateSelf();
    }

    private void nullLayouts() {
        mMainTextLayout = null;
    }

    private void buildTextLayout() {
        mFramePaint.setColor(parseColor(mViewModel.mBgColor, Color.WHITE));
        mMainBackgroundPaint.setColor(parseColor(mViewModel.mBgColor, Color.WHITE));
        if (!TextUtils.isEmpty(mViewModel.mShowText)) {
            int mTextSize = getFontSize(mViewModel.mTextSize, DEFAULT_FONT_SIZE);
            int mTextColor = parseColor(mViewModel.mTextColor, Color.BLACK);
            mMainTextLayout = mTextLayoutMaker.makeTextLayout(mViewModel.mShowText, mTextSize, mTextColor);
        }
    }

    private int parseColor(String color, int defaultColor) {
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return defaultColor;
        }
    }

    private int getFontSize(float fontSize, float defaultFontSize) {
        if (fontSize > 0) {
            return DeviceUtil.getPixelFromDip(mContext, fontSize);
        } else {
            return DeviceUtil.getPixelFromDip(mContext, defaultFontSize);
        }
    }

    public void measure(int width, int heightMeasureSpec) {
        if (mMainTextLayout == null && mViewModel != null) {
            buildTextLayout();
        }
        mMeasuredWidth = 0;
        mMeasuredHeight = 0;
        if (mMainTextLayout != null) {
            mMeasuredWidth += getSingleLabelWidth(mMainTextLayout);
        }
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        mMeasuredWidth = width;
        if (heightMode == View.MeasureSpec.EXACTLY) {
            mMeasuredHeight = heightSize;
        }

    }

    private float getSingleLabelWidth(Layout layout) {
        return layout.getLineWidth(0);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        Rect bounds = getBounds();
        canvas.translate(bounds.left, bounds.top);
        float mainLabelWidth = drawSingleLabel(canvas, mMainTextLayout, mMainBackgroundPaint);
//        canvas.save();
//        canvas.translate(mainLabelWidth, 0);
//        canvas.restore();
//        drawFrame(canvas);//
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private float drawSingleLabel(Canvas canvas, Layout textLayout, Paint backgroundPaint) {
        //绘制底色
        float start = 0;
        float end = mMeasuredWidth;
        if (mViewModel.mShowIndex < 0) {
            start = (float) (mMeasuredWidth * (1 + mViewModel.mShowIndex));
        } else if (mViewModel.mShowIndex > 0) {
            end = (float) (mMeasuredWidth * mViewModel.mShowIndex);
        }

        //绘制背景
        drawSingleLabelBackground(canvas, backgroundPaint, start, end);

        //绘制字体
        if (textLayout != null) {
            Rect bounds = getBounds();
            int height = bounds.height();
            float labelWidth = getSingleLabelWidth(textLayout);
            if (textLayout == mMainTextLayout) {
                labelWidth = bounds.width();
            }
            canvas.save();

            float dx = 0;
            float dy = (height - textLayout.getHeight() + textLayout.getTopPadding() + textLayout.getBottomPadding()) / 2;
            if (mViewModel.mShowLocation == StockIndexChangeModel.SHOW_LOCATION_RIGHT) {
                dx = 10;
            } else if (mViewModel.mShowLocation == StockIndexChangeModel.SHOW_LOCATION_LEFT) {
                dx = mMeasuredWidth - textLayout.getLineWidth(0) - 10;
            } else {
                dx = (mMeasuredWidth - textLayout.getLineWidth(0)) / 2;
            }
            canvas.translate(dx, dy);
            textLayout.draw(canvas);
            canvas.restore();
            return labelWidth;
        }
        return 0;
    }

    private void drawSingleLabelBackground(Canvas canvas, Paint paint, double start, double end) {
        Rect bounds = getBounds();
        int height = bounds.height();
        RectF rectF = new RectF((float) start, 0, (float) end, height);
        canvas.drawRoundRect(rectF, mLabelRadius, mLabelRadius, paint);
    }

    private void drawFrame(Canvas canvas) {
        Rect bounds = getBounds();
        int width = bounds.width();
        int height = bounds.height();
        float halfStrokeWidth = mFramePaint.getStrokeWidth() / 2;
        RectF rectF = new RectF(halfStrokeWidth, halfStrokeWidth, width - halfStrokeWidth, height - halfStrokeWidth);
        canvas.drawRoundRect(rectF, mLabelRadius, mLabelRadius, mFramePaint);
    }

    public final int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    public final int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    public void layout(int left, int top, int right, int bottom) {
        setBounds(left, top, right, bottom);
    }
}
