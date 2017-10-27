package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xt.lxl.stock.R;


/**
 * <p>封装InfoBar功能，用于显示信息Logo + 标题 + 内容 + Arrow的式样，具体显示效果如下：</p>
 * <p>----------------------------------------</p>
 * <p> Logo 	*Label 			Value 	 Arrow </p>
 * <p>----------------------------------------</p>
 * <p>Logo - 需要显示的logo图片</p>
 * <p>Label - 标签文字，可设置必选项标识 "*"</p>
 * <p>Value - 当前设置/回显的内容，可设置特定的格式以支持多行显示</p>
 * <p>Arrow - 为箭头图片，默认向右</p>
 * <p>Value内容部分自适应显示宽度，剩余空间由Label标题部分自适应占满。</p>
 * <p>
 * <p>
 * <b>XML attributes</b>
 * <p>
 * 公共挪到了酒店，酒店这边独立设置，不在依赖公共。并且删除部分未使用功能
 *
 * @author xiangleiliu
 *         再添加一种hint
 */
public class StockInfoBar extends FrameLayout {

    private TextView mValueText;//
    private TextView mHintText;
    private ImageView mIconView;

    public StockInfoBar(Context context) {
        this(context, null);
    }

    public StockInfoBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockInfoBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.stock_view_info_bar, this);
        initFromAttributes(context, attrs);
        initView();
    }


    public void initFromAttributes(Context context, AttributeSet attrs) {
        // 通过xml style设置默认参数
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StockInfoBar);
        if (attrs == null || a == null) {
            return;
        }

        a.recycle();
    }

    public void bindInfoData(String valueText, String hintText) {
        mValueText.setText(valueText);
        mHintText.setText(hintText);
    }

    /**
     * 创建child view对象，并添加到父容器中
     * <p>Value内容部分自适应显示宽度，剩余空间由Label标题部分自适应占满。</p>
     */
    protected void initView() {
        mValueText = (TextView) findViewById(R.id.stock_value_desc);
        mHintText = (TextView) findViewById(R.id.stock_hint_desc);
        mIconView = (ImageView) findViewById(R.id.stock_info_bar_arrow);
    }

    /**
     * 设置Value文字
     *
     * @param text 需要显示的Value文本
     */
    public void setValueText(CharSequence text) {
        if (mValueText != null) {
            mValueText.setText(text);
        }
    }
}