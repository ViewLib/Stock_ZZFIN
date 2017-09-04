package com.xt.lxl.stock.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.util.StringUtil;

/**
 * Created by lxl
 */
public class StockEditableBar extends LinearLayout implements View.OnClickListener {
    private EditText mEditText;
    private ImageView mCleanImage;
    private IgetInputContentListener inputContentListener;

    public StockEditableBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.hotel_item_edit_bar_view_layout, this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (EditText) findViewById(R.id.stock_edit_text);
        mCleanImage = (ImageView) findViewById(R.id.stock_edit_clean);
        mEditText.addTextChangedListener(mTextWacher);
        mEditText.setOnFocusChangeListener(mFocusChangeListener);
        mCleanImage.setOnClickListener(this);
    }

    public OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setSelected(hasFocus);
            setCleanBtnVisible(hasFocus && !StringUtil.emptyOrNull(mEditText.getText().toString()));
        }
    };

    public TextWatcher mTextWacher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            Log.i("TEST", "s:" + s);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (s.length() > 6) {
//                mEditText.setText(s.subSequence(0, 6));
//            }
//            Log.i("TEST", "s:" + s);
        }

        @Override
        public void afterTextChanged(Editable s) {
            setCleanBtnVisible(s.toString().length() > 0);
            if (inputContentListener != null) {
                inputContentListener.getInput(s.toString().trim());
            }
        }
    };


    public void setInputListener(IgetInputContentListener inputListener) {
        this.inputContentListener = inputListener;
    }

    public void setCleanBtnVisible(boolean isVisible) {
        mCleanImage.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_edit_clean) {
            mEditText.setText(null);
            setCleanBtnVisible(false);
        }
    }

    public interface IgetInputContentListener {
        void getInput(String inputString);
    }
}
