package com.xt.lxl.stock.page;

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

/**
 * Created by xiangleiliu on 2017/9/2.
 */
public class StockUserFragment extends Fragment {

    TextView mUserName;
    TextView mUserPhone;
    ImageView mUserIcon;
    LinearLayout mOptionContainer;


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
        mUserName = (TextView) view.findViewById(R.id.stock_user_nickname);
        mUserPhone = (TextView) view.findViewById(R.id.stock_user_phone);
        mUserIcon = (ImageView) view.findViewById(R.id.stock_user_icon);
        mOptionContainer = (LinearLayout) view.findViewById(R.id.stock_user_option);

    }
}
