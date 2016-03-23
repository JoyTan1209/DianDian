package com.tanchaoyin.diandian.module.home.adapter;

import android.content.Context;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.adapter.SimpleListAdapter;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/23.
 */
public class DrawerListAdapter extends SimpleListAdapter {

    public DrawerListAdapter(Context mContext, List<String> list) {
        super(mContext, list);
    }

    @Override
    protected int getLayout() {
        return R.layout.drawer_list_item_layout;
    }
}
