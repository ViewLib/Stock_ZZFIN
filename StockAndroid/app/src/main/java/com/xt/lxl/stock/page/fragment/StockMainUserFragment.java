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
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.page.activity.StockRegisterActivity;
import com.xt.lxl.stock.util.FormatUtil;
import com.xt.lxl.stock.util.HotelViewHolder;
import com.xt.lxl.stock.util.StockUser;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.widget.view.StockBannerView;
import com.xt.lxl.stock.widget.dialog.HotelCustomDialog;

/**
 * Created by xiangleiliu on 2017/9/2.
 */
public class StockMainUserFragment extends Fragment implements View.OnClickListener {

    public static final int REGISTER_FROM_USER = 101;

    TextView mUserName;
    TextView mUserID;
    TextView mUserRegisterDate;
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
        initCheck();
    }

    private void initView(View view) {
        mUserName = (TextView) view.findViewById(R.id.stock_user_nickname);
        mUserID = (TextView) view.findViewById(R.id.stock_user_id);
        mUserRegisterDate = (TextView) view.findViewById(R.id.stock_user_registerdata);
        mUserIcon = (ImageView) view.findViewById(R.id.stock_user_icon);
        mUserSetting = (LinearLayout) view.findViewById(R.id.stock_self_setting);

        mStockUserSettingBtn = (StockBannerView) view.findViewById(R.id.stock_user_setting_btn);
        mStockUserCommentBtn = (StockBannerView) view.findViewById(R.id.stock_user_comment_btn);
        mStockUserFeedbackBtn = (StockBannerView) view.findViewById(R.id.stock_user_feedback_btn);
        mStockUserAboutUsBtn = (StockBannerView) view.findViewById(R.id.stock_user_aboutus_btn);
        mStockUserExitLoginBtn = (StockBannerView) view.findViewById(R.id.stock_user_exitlogin_btn);
    }

    private void initListener() {
        mStockUserExitLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_user_exitlogin_btn) {
            StockUser stockUser = StockUser.getStockUser(getContext());
            stockUser.clearUser(getContext());
            stockUser.setExit(true);
            bindDate(stockUser);
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
        //注册的用户展示用户信息
        bindDate(stockUser);
    }

    private void bindDate(StockUser stockUser) {
        if (stockUser.getUserId() == 0 && !stockUser.isExit()) {
            return;
        }
        String nickName = stockUser.getNickName();
        if (StringUtil.emptyOrNull(nickName)) {
            nickName = "用户" + stockUser.getUserId();
        }
        mUserName.setText(nickName);
        mUserID.setText(String.valueOf(stockUser.getUserId()));
        HotelViewHolder.showText(mUserRegisterDate, FormatUtil.DATE_2_YYYY_MM_DD(stockUser.getCreateTime()));
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
