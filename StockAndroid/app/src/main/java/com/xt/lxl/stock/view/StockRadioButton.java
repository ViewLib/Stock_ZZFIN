package com.xt.lxl.stock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.xt.lxl.stock.R;

/**
 * Created by xiangleiliu on 2017/9/1.
 */
public class StockRadioButton extends RadioButton {
    public StockRadioButton(Context context) {
        this(context, null);
    }

    public StockRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFromAttributes(context, attrs);
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StockRadioButton);

        Drawable drawable = a.getDrawable(R.styleable.StockRadioButton_stock_radio_drawable_src);
        if (drawable != null) {
            int direction = a.getInt(R.styleable.StockRadioButton_stock_radio_drawable_direction, 0);
            if (direction < 0 || direction > 3) {
                direction = 0;
            }
            int width = a.getDimensionPixelSize(R.styleable.StockRadioButton_stock_radio_drawable_width, 0);
            int height = a.getDimensionPixelSize(R.styleable.StockRadioButton_stock_radio_drawable_height, 0);
            int padding = a.getDimensionPixelSize(R.styleable.StockRadioButton_stock_radio_drawable_padding, 0);
            setCompoundDrawablePadding(padding);
            setCompoundDrawable(drawable, direction, width, height);
        }

        a.recycle();
    }

    /**
     * 设置TextView的CompoundDrawable
     *
     * @param drawable  CompoundDrawable对象
     * @param direction 显示方向
     * @param width     显示宽度, 等于0则按drawable实际宽度显示
     * @param height    显示高度, 等于0则按drawable实际高度显示
     */
    public void setCompoundDrawable(Drawable drawable, int direction, int width, int height) {
        if (drawable != null) {
            drawable.setBounds(0, 0,
                    width == 0 ? drawable.getIntrinsicWidth() : width,
                    height == 0 ? drawable.getIntrinsicHeight() : height);
        }

        switch (direction) {
            case 0:
                setCompoundDrawables(drawable, null, null, null);
                break;
            case 1:
                setCompoundDrawables(null, drawable, null, null);
                break;
            case 2:
                setCompoundDrawables(null, null, drawable, null);
                break;
            case 3:
                setCompoundDrawables(null, null, null, drawable);
                break;
            default:
                break;
        }
    }
}
