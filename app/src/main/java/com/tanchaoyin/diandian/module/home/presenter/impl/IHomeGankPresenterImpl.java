package com.tanchaoyin.diandian.module.home.presenter.impl;

import com.tanchaoyin.diandian.base.BasePresenterImpl;
import com.tanchaoyin.diandian.module.home.view.IHomeGankView;
import com.tanchaoyin.diandian.module.home.presenter.IHomeGankPresenter;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/28.
 */
public class IHomeGankPresenterImpl extends BasePresenterImpl<IHomeGankView,List<String>> implements IHomeGankPresenter {

    public IHomeGankPresenterImpl(IHomeGankView view) {
        super(view);
        view.initViewPager();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
