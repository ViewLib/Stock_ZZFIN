package com.xt.lxl.stock.page.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.page.activity.StockRegisterActivity;
import com.xt.lxl.stock.util.FormatUtil;
import com.xt.lxl.stock.util.HotelViewHolder;
import com.xt.lxl.stock.util.StockShowUtil;
import com.xt.lxl.stock.util.StockUser;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.widget.dialog.HotelCustomDialog;
import com.xt.lxl.stock.widget.view.StockBannerView;

/**
 * Created by xiangleiliu on 2017/9/2.
 */
public class StockMainUserFragment extends Fragment implements View.OnClickListener {

    public static final int REGISTER_FROM_USER = 101;

    RelativeLayout mUserInfo;
    TextView mUserName;
    TextView mUserID;
    TextView mUserRegisterDate;

    RelativeLayout mUserRegister;
    TextView mUserRegisterBtn;

    ImageView mUserIcon;
    LinearLayout mUserSetting;

    StockBannerView mStockUserSettingBtn;
    StockBannerView mStockUserCommentBtn;
    StockBannerView mStockUserFeedbackBtn;
    StockBannerView mStockUserAboutUsBtn;
    StockBannerView mStockUserExitLoginBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_main_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        //注册的用户展示用户信息
        bindDate(StockUser.getStockUser(getContext()));
        initCheck();
    }

    private void initView(View view) {
        mUserInfo = (RelativeLayout) view.findViewById(R.id.stock_user_info);
        mUserName = (TextView) view.findViewById(R.id.stock_user_nickname);
        mUserID = (TextView) view.findViewById(R.id.stock_user_id);
        mUserRegisterDate = (TextView) view.findViewById(R.id.stock_user_registerdata);
        mUserIcon = (ImageView) view.findViewById(R.id.stock_user_icon);

        mUserRegister = (RelativeLayout) view.findViewById(R.id.stock_user_register);
        mUserRegisterBtn = (TextView) view.findViewById(R.id.stock_user_register_btn);

        mStockUserSettingBtn = (StockBannerView) view.findViewById(R.id.stock_user_setting_btn);
        mStockUserCommentBtn = (StockBannerView) view.findViewById(R.id.stock_user_comment_btn);
        mStockUserFeedbackBtn = (StockBannerView) view.findViewById(R.id.stock_user_feedback_btn);
        mStockUserAboutUsBtn = (StockBannerView) view.findViewById(R.id.stock_user_aboutus_btn);
        mStockUserExitLoginBtn = (StockBannerView) view.findViewById(R.id.stock_user_exitlogin_btn);
    }

    private void initListener() {
        mStockUserExitLoginBtn.setOnClickListener(this);
        mUserRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_user_exitlogin_btn) {
            HotelCustomDialog dialog = new HotelCustomDialog();
            dialog.setContent("是否确定要退出", "退出", "取消");
            dialog.setDialogBtnClick(new HotelCustomDialog.HotelDialogBtnClickListener() {
                @Override
                public void leftBtnClick(HotelCustomDialog dialog) {
                    StockUser stockUser = StockUser.getStockUser(getContext());
                    stockUser.clearUser(getContext());
                    stockUser.setExit(true);
                    bindDate(stockUser);
                    StockShowUtil.showToastOnMainThread(getContext(), "退出成功");
                }

                @Override
                public void rightBtnClick(HotelCustomDialog dialog) {
                    dialog.dismiss();
                }
            });
        } else if (id == R.id.stock_user_register_btn) {
            jumpToRegister();
        }
    }

    private void initCheck() {
        StockUser stockUser = StockUser.getStockUser(getContext());
        if (stockUser.getUserId() == 0) {
            //提示注册
            HotelCustomDialog dialog = new HotelCustomDialog();
            dialog.setTitle("注册");
            dialog.setContent("您还没有注册，是否立即前往注册页？", "注册", "取消");
            dialog.setDialogBtnClick(new HotelCustomDialog.HotelDialogBtnClickListener() {
                @Override
                public void leftBtnClick(HotelCustomDialog dialog) {
                    jumpToRegister();
                    dialog.dismiss();
                }

                @Override
                public void rightBtnClick(HotelCustomDialog dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show(getFragmentManager(), "register");
            return;
        }
    }

    private void bindDate(StockUser stockUser) {
        boolean isLogin = stockUser.getUserId() > 0 && !stockUser.isExit();
        if (isLogin) {
            mUserInfo.setVisibility(View.VISIBLE);
            mUserRegister.setVisibility(View.GONE);
            String nickName = stockUser.getNickName();
            if (StringUtil.emptyOrNull(nickName)) {
                nickName = "用户" + stockUser.getUserId();
            }
            mUserName.setText(nickName);
            mUserID.setText(String.valueOf(stockUser.getUserId()));
            HotelViewHolder.showText(mUserRegisterDate, FormatUtil.DATE_2_YYYY_MM_DD(stockUser.getCreateTime()));
            return;
        }
        mUserInfo.setVisibility(View.GONE);
        mUserRegister.setVisibility(View.VISIBLE);
    }

    /**
     * 跳转注册界面
     */

    private void jumpToRegister() {
        Intent intent = new Intent();
        intent.setClass(getContext(), StockRegisterActivity.class);
        startActivityForResult(intent, REGISTER_FROM_USER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_FROM_USER) {
            StockUser stockUser = StockUser.getStockUser(getContext());
            bindDate(stockUser);
        }
    }

}
