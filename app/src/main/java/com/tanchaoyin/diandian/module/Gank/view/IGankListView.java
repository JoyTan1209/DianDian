package com.tanchaoyin.diandian.module.gank.view;

import com.tanchaoyin.diandian.base.BaseView;
import com.tanchaoyin.diandian.bean.gank.BaseGankData;
import com.tanchaoyin.diandian.common.DataLoadState;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
public interface IGankListView extends BaseView {

    void updateGankListView(List<BaseGankData> data, DataLoadState state,String type);
}
