package com.tanchaoyin.diandian.module.Gank.presenter.impl;

import com.tanchaoyin.diandian.base.BasePresenterImpl;
import com.tanchaoyin.diandian.module.Gank.presenter.IGankPresenter;
import com.tanchaoyin.diandian.module.Gank.view.IGankView;

import java.util.HashMap;


/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class IGankPresenterImpl extends BasePresenterImpl<IGankView,HashMap<Integer,String>> implements IGankPresenter {

    public IGankPresenterImpl(IGankView gankView) {
        super(gankView);
        view.initRxBusEvent();
        view.initViewPager();
    }

    @Override
    public void requestSuccess(HashMap<Integer, String> data) {
        super.requestSuccess(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initGankType() {

    }
}
