package com.xt.lxl.stock.page;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xt.lxl.stock.R;
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

    public String phone = "";
    public String country = "86";
    Handler mHandler = new Handler();
    EventHandler eh = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
//                    注册操作

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //发送证码成功，开始刷新重发倒计时的操作
                    String country = "";
                    String phone = "";
                    String code = "";
                    pollingResend(65);
//                    SMSSDK.submitVerificationCode(country, phone, code);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表

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
        sendVerificationCode(phone);
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
                    return;
                }
                mResendBtn.setText("重新发送(" + surplus + ")");
                pollingResend(surplus - 1);
            }
        }, 1000);
    }

    private void initData() {
        phone = getIntent().getExtras().getString(INPUT_PHONE);
        country = getIntent().getExtras().getString(INPUT_COUNTRY);
    }

    private void initView() {
        mVerificationCodeEdit = (EditText) findViewById(R.id.stock_et_VerificationCode);
        mNextStep = (TextView) findViewById(R.id.stock_btn_NextStep);
        mResendBtn = (TextView) findViewById(R.id.btn_ResendPhoneCode);
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
        if (id == R.id.btn_ResendPhoneCode) {
            sendVerificationCode(phone);
        } else if (id == R.id.stock_register_next_step) {
            //验证
            submitVerificationCode();
        }
    }

    /**
     * 发送验证码
     */
    private void sendVerificationCode(String phone) {
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
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }


}
