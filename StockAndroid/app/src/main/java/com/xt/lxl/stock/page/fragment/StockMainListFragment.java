package com.xt.lxl.stock.page.fragment;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockListCallBacks;
import com.xt.lxl.stock.listener.inter.ActionStockCallBackInter;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.page.activity.StockDetailActivity;
import com.xt.lxl.stock.page.activity.StockSearchActivity;
import com.xt.lxl.stock.page.adapter.StockListAdapter;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataShowUtil;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.StockUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/9/2.
 */
public class StockMainListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final int RequestCodeForSearch = 1;
    StockListCallBacks mCallBacks = new StockListCallBacks();
    TextView mStockKeywordEditText;//编辑按钮
    ListView mStockListView;
    StockListAdapter mAdapter;
    Handler mHander = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_main_selflist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                intent.setClass(getActivity(), StockSearchActivity.class);
                startActivityForResult(intent, RequestCodeForSearch);
            }
        };
        mCallBacks.mActionCallBack = new ActionStockCallBackInter() {
            @Override
            public void actionStockCallBack(final View v, final StockViewModel stockViewModel) {
                PopupWindow pop = new PopupWindow();
                View view = View.inflate(getActivity(), R.layout.pop_menu_dialog, null);
                pop.setContentView(view);
                pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                view.findViewById(R.id.stock_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vv) {
                        StockViewModel item = stockViewModel;
                        DataSource.deleteStockCode(getActivity(), item.stockCode);
                        initData();
                    }
                });
                pop.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
                pop.setFocusable(true);
                int screenWidth = DeviceUtil.getScreenWidth(getContext());
                int pixelFromDip = DeviceUtil.getPixelFromDip(getContext(), 100);
                pop.showAsDropDown(v, screenWidth - pixelFromDip, pixelFromDip / 10 * -1);
            }
        };
        mStockKeywordEditText.setOnClickListener(this);
        mStockListView.setOnItemClickListener(this);
        mStockListView.setOnItemLongClickListener(this);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<StockViewModel> stockList = new ArrayList<>();
                List<String> saveStockCodeList = DataSource.getSaveStockCodeList(getActivity());
                if (saveStockCodeList.size() == 0) {
                    saveStockCodeList = DataSource.getDefaultStockCodeList();
                }
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
        mStockKeywordEditText = (TextView) view.findViewById(R.id.stock_keyword_edit_text);
        mStockListView = (ListView) view.findViewById(R.id.stock_list);
    }

    private void bindData() {
        mAdapter = new StockListAdapter(getContext(), mCallBacks);
        mStockListView.setAdapter(mAdapter);
        mAdapter.initOneStockViewModel(new ArrayList<StockViewModel>());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StockViewModel stockViewModel = (StockViewModel) parent.getItemAtPosition(position);
        if ("上证指数".equals(stockViewModel.stockName) || "深证成指".equals(stockViewModel.stockName) || "创业板指".equals(stockViewModel.stockName)) {
            return;
        }
        if (stockViewModel.showType == StockViewModel.STOCK_SHOW_TYPE_NORMAL) {
            Intent intent = new Intent(getActivity(), StockDetailActivity.class);
            intent.putExtra(StockDetailActivity.STOCK_DETAIL, stockViewModel);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        StockViewModel stockViewModel = (StockViewModel) parent.getItemAtPosition(position);
        if (stockViewModel.showType == StockViewModel.STOCK_SHOW_TYPE_NORMAL) {
            mCallBacks.mActionCallBack.actionStockCallBack(view, stockViewModel);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_keyword_edit_text) {
            mCallBacks.mAddCallBack.onClick(null);
        } else if (id == R.id.stock_self_optional) {

        } else if (id == R.id.stock_self_quotation) {
            StockUtil.showToastOnMainThread(getActivity(), "暂不支持该动能");
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
