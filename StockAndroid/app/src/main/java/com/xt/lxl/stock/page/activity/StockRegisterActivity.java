package com.xt.lxl.stock.page.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.page.fragment.StockUserFragment;
import com.xt.lxl.stock.util.StockShowUtil;
import com.xt.lxl.stock.util.StringUtil;

/**
 * Created by xiangleiliu on 2017/8/24.
 * 账户注册
 */
public class StockRegisterActivity extends Activity implements View.OnClickListener {

    boolean isOversea = false;
    TextView mNextStep;
    View mCountryView;
    EditText mCountryEdit;
    EditText mPhoneEdit;
    TextView mSpecialZoneReg;
    TextView mCompanyReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_register_layout);
        initView();
        initListener();
    }

    private void initView() {
        mNextStep = (TextView) findViewById(R.id.stock_register_next_step);
        mCountryView = findViewById(R.id.stock_register_country_view);
        mCountryEdit = (EditText) findViewById(R.id.stock_register_country_edit);
        mPhoneEdit = (EditText) findViewById(R.id.stock_register_phone_edit);
        mSpecialZoneReg = (TextView) findViewById(R.id.stock_register_tv_SpecialZoneReg);
        mCompanyReg = (TextView) findViewById(R.id.stock_register_tv_CompanyReg);
    }

    private void initListener() {
        mNextStep.setOnClickListener(this);
        mSpecialZoneReg.setOnClickListener(this);
        mCompanyReg.setOnClickListener(this);
        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mNextStep.setEnabled(true);
                } else {
                    mNextStep.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_register_next_step) {
            String phone = mPhoneEdit.getText().toString();
            String country = "86";
            if (isOversea) {
                country = mCountryEdit.getText().toString();
            }
            if (!checkInputPhone(phone, country)) {
                return;
            }

            if (isOversea) {
                country = mCountryEdit.getText().toString();
            }
            handleGoToNext(country, phone);
        } else if (id == R.id.stock_register_tv_SpecialZoneReg) {
            if (isOversea) {
                isOversea = false;
                mSpecialZoneReg.setText("大陆用户注册");
                mCountryView.setVisibility(View.GONE);
            } else {
                isOversea = true;
                mCountryView.setVisibility(View.VISIBLE);
                mSpecialZoneReg.setText("非大陆用户注册");
            }
        } else if (id == R.id.stock_register_tv_CompanyReg) {
            StockShowUtil.showToastOnMainThread(this, "暂不支持该种方式注册");
        }
    }

    private boolean checkInputPhone(String phone, String country) {
        if (StringUtil.emptyOrNull(phone)) {
            StockShowUtil.showToastOnMainThread(this, "请输入手机号");
            return false;
        }
        if (StringUtil.emptyOrNull(country) || country.equals("86")) {
            //大陆手机号
            if (phone.length() != 11) {
                StockShowUtil.showToastOnMainThread(this, "请输入正确的手机号码");
                return false;
            }
        }
        //海外不验证
        return true;
    }

    //跳转到短信验证码，验证码在下一个界面发送
    private void handleGoToNext(String country, String phone) {
        Intent intent = new Intent();
        intent.setClass(this, StockVerificationActivity.class);
        intent.putExtra(StockVerificationActivity.INPUT_PHONE, phone);
        intent.putExtra(StockVerificationActivity.INPUT_COUNTRY, country);
        startActivityForResult(intent, StockUserFragment.REGISTER_FROM_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(Activity.RESULT_OK);
        finish();
    }
}
