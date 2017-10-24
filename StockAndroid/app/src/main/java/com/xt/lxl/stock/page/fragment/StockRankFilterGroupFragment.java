package com.xt.lxl.stock.page.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;
import com.xt.lxl.stock.model.model.StockRankFilterItemModel;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.widget.view.StockCheckedTextView;

import java.util.List;


/**
 * Created by xiangleiliu on 2017/10/23.
 * 选股范围
 */
public class StockRankFilterGroupFragment extends StockRankFilterBaseFragment {

    LinearLayout mSubGroupView;
    LinearLayout mItemView;
    int padding = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_rank_filter_group_layout, null);
    }

    @Override
    protected void bindData() {
        if (filterGroupModel == null) {
            return;
        }
//        mSubGroupView.removeAllViews();
        padding = DeviceUtil.getPixelFromDip(getContext(), 10);
        //刷新group的
        List<StockRankFilterGroupModel> filterGroupList = filterGroupModel.filterGroupList;
        for (StockRankFilterGroupModel groupModel : filterGroupList) {
            final StockCheckedTextView checkedTextView = new StockCheckedTextView(getContext());
            checkedTextView.setCheckMarkDrawable(R.drawable.hotel_icon_radiobutton_selector);
            checkedTextView.setText(groupModel.groupName);
            checkedTextView.setPadding(padding, padding, padding, padding);
            checkedTextView.setTag(groupModel);
            checkedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //触发联动
                    selectGroup((StockCheckedTextView) v);
                }
            });
            mSubGroupView.addView(checkedTextView);
        }
        if (mSubGroupView.getChildCount() == 0) {
            return;
        }
        selectGroup((StockCheckedTextView) mSubGroupView.getChildAt(0));
    }

    //选择一个group的model
    private void selectGroup(StockCheckedTextView v) {
        mItemView.removeAllViews();
        for (int i = 0; i < mSubGroupView.getChildCount(); i++) {
            StockCheckedTextView checkedTextView = (StockCheckedTextView) mSubGroupView.getChildAt(i);
            checkedTextView.setChecked(false);
        }
        v.setChecked(true);
        StockRankFilterGroupModel groupModel = (StockRankFilterGroupModel) v.getTag();
        for (StockRankFilterItemModel itemModel : groupModel.filteList) {
            StockCheckedTextView itemCheckTv = new StockCheckedTextView(getContext());
            itemCheckTv.setText(itemModel.filterName);
            itemCheckTv.setTag(itemModel);
            itemCheckTv.setPadding(padding, padding, padding, padding);
            mItemView.addView(itemCheckTv);
        }


    }


    @Override
    protected void initView(View view) {
        mSubGroupView = (LinearLayout) view.findViewById(R.id.filter_sub_group_layout);
        mItemView = (LinearLayout) view.findViewById(R.id.filter_item_layout);
    }

}
