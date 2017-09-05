package com.xt.lxl.stock.page.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.util.LogUtil;
import com.xt.lxl.stock.util.StockShowUtil;
import com.xt.lxl.stock.util.StringUtil;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by xiangleiliu on 2017/8/24.
 * 验证码验证
 */
public class StockVerificationActivity extends Activity implements View.OnClickListener {

    public static final String INPUT_PHONE = "INPUT_PHONE";
    public static final String INPUT_COUNTRY = "INPUT_COUNTRY";

    private EditText mVerificationCodeEdit;
    private TextView mNextStep;
    private TextView mResendBtn;
    private TextView mStockSendcodeHint;

    public String mPhone = "";
    public String mCountry = "86";
    Handler mHandler = new Handler();
    EventHandler eh = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
//                    注册操作
                    sendUserRegister(mCountry, mPhone);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //发送证码成功，开始刷新重发倒计时的操作，并且验证码提示修改
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String showText = "验证码已发送到手机：\n";
                            if (!"86".equals(mCountry)) {
                                showText += "验证码已发送到手机：" + mCountry + mPhone;
                            }
                            mStockSendcodeHint.setText(showText);
                        }
                    });
                    pollingResend(65);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    LogUtil.LogI("国家列表");
                    sendVerificationCode(mCountry, mPhone);
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_verification_layout);
        initData();
        initView();
        initListener();
        initAction();
    }

    private void initAction() {
        sendGetSupportCountries();
        mResendBtn.setEnabled(false);
    }

    private void pollingResend(final int surplus) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                if (surplus <= 0) {
                    mResendBtn.setText("重新发送");
                    mResendBtn.setEnabled(true);
                    return;
                }
                mResendBtn.setText("重新发送(" + surplus + ")");
                pollingResend(surplus - 1);
            }
        }, 1000);
    }

    private void initData() {
        mPhone = getIntent().getExtras().getString(INPUT_PHONE);
        mCountry = getIntent().getExtras().getString(INPUT_COUNTRY);
    }

    private void initView() {
        mVerificationCodeEdit = (EditText) findViewById(R.id.stock_et_VerificationCode);
        mResendBtn = (TextView) findViewById(R.id.btn_resend_code);
        mNextStep = (TextView) findViewById(R.id.stock_verification_next_stepstock);
        mStockSendcodeHint = (TextView) findViewById(R.id.stock_tv_sendcode_hint);
    }

    private void initListener() {
        //3.0版本之后的初始化看这里（包括3.0）
        SMSSDK.registerEventHandler(eh); //注册短信回调
        mResendBtn.setOnClickListener(this);
        mNextStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_resend_code) {
            sendVerificationCode(mCountry, mPhone);
        } else if (id == R.id.stock_verification_next_stepstock) {
            //验证
            submitVerificationCode();
        }
    }

    private void sendGetSupportCountries() {
        SMSSDK.getSupportedCountries();
    }

    /**
     * 发送验证码
     */
    private void sendVerificationCode(String country, String phone) {
        SMSSDK.getVerificationCode(country, phone, new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String country, String phone) {
                //发送前调用，返回true不实际发送
                return false;
            }
        });
    }

    /**
     * 验证验证码
     */
    private void submitVerificationCode() {
        String code = mVerificationCodeEdit.getText().toString();
        if (StringUtil.emptyOrNull(code)) {
            StockShowUtil.showToastOnMainThread(this, "请输入验证码");
        }
        if (code.length() != 4) {
            StockShowUtil.showToastOnMainThread(this, "请输入正确的验证码");
        }
        mStockSendcodeHint.setText("验证中，请稍后.");
        SMSSDK.submitVerificationCode(mCountry, mPhone, code);
    }

    /**
     * 用户注册
     */
    private void sendUserRegister(String country, String phone) {
        //发送用户注册服务。
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }


}
