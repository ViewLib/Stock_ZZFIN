package com.xt.lxl.stock.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.list.StockAdapter;
import com.xt.lxl.stock.listener.StockListCallBacks;
import com.xt.lxl.stock.model.StockViewModel;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataShowUtil;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.StockShowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/9/2.
 */
public class StockListFragment extends Fragment implements View.OnClickListener {

    public static final int RequestCodeForSearch = 1;
    StockListCallBacks mCallBacks = new StockListCallBacks();
    EditText mStockKeywordEditText;//编辑按钮
    ListView mStockListView;
    StockAdapter mAdapter;
    Handler mHander = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_list_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        TextView text = (TextView) view.findViewById(R.id.main_show_text);
//        text.setText("StockListFragment");
        initData();
        initView(view);
        initListener();
        bindData();
    }

    private void initListener() {
        mCallBacks.mAddCallBack = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StockItemEditActivity.class);
                startActivityForResult(intent, RequestCodeForSearch);
            }
        };
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<StockViewModel> stockList = new ArrayList<>();
                List<String> saveStockCodeList = DataSource.getSaveStockCodeList(getActivity());
                List<List<String>> lists = DataShowUtil.divisionList(saveStockCodeList, 60);
                for (List<String> list : lists) {
                    stockList.addAll(StockSender.getInstance().requestStockModelByCode(list));
                }
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.initOneStockViewModel(stockList);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void initView(View view) {
        mStockKeywordEditText = (EditText) view.findViewById(R.id.stock_keyword_edit_text);
        mStockListView = (ListView) view.findViewById(R.id.stock_list);
    }

    private void bindData() {
        mAdapter = new StockAdapter(getContext(), mCallBacks);
        mStockListView.setAdapter(mAdapter);
        mAdapter.initOneStockViewModel(new ArrayList<StockViewModel>());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_keyword_edit_text) {
            mCallBacks.mAddCallBack.onClick(null);
        } else if (id == R.id.stock_self_optional) {

        } else if (id == R.id.stock_self_quotation) {
            StockShowUtil.showToastOnMainThread(getActivity(), "暂不支持该动能");
        } else if (id == R.id.back_btn) {
//            finish();
            Activity activity = getActivity();
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
            if (activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
                activity.getWindow().getDecorView().dispatchKeyEvent(keyEvent);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodeForSearch) {
            initData();
        }
    }
}
