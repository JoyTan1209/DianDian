package com.tanchaoyin.diandian.module.gank.view;

import com.tanchaoyin.diandian.base.BaseView;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public interface IGankView extends BaseView {

    void initViewPager();

    void initRxBusEvent();

    void setToolbarTitle(String title);
}
