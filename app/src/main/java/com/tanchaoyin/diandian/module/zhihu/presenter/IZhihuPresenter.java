package com.tanchaoyin.diandian.module.zhihu.presenter;

import com.tanchaoyin.diandian.base.BasePresenter;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
public interface IZhihuPresenter extends BasePresenter {

    void refreshData();

    void loadMoreData(String date);
}
