package com.tanchaoyin.diandian.module.Gank.view;

import com.tanchaoyin.diandian.base.BaseView;
import com.tanchaoyin.diandian.bean.GankDaily;
import com.tanchaoyin.diandian.common.DataLoadState;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public interface IGankDailyView extends BaseView {

    void updateGankListView(List<GankDaily> data, DataLoadState state);
}
