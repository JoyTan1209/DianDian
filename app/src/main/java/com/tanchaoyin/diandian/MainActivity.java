package com.tanchaoyin.diandian;

import com.tanchaoyin.diandian.annotation.ActivityFragmentInject;
import com.tanchaoyin.diandian.app.AppManager;
import com.tanchaoyin.diandian.base.BaseActivity;
import com.tanchaoyin.diandian.bean.BaseGankData;
import com.tanchaoyin.diandian.common.DataLoadState;
import com.tanchaoyin.diandian.http.GankType;
import com.tanchaoyin.diandian.module.Gank.presenter.IGankDataPresenter;
import com.tanchaoyin.diandian.module.Gank.presenter.impl.IGankDataPresenterImpl;
import com.tanchaoyin.diandian.module.Gank.view.IGankListView;

import java.util.List;

@ActivityFragmentInject(contentViewId = R.layout.activity_main,
        menuId = R.menu.menu_main,
        hasNavigationView = true,
        toolbarTitle = R.string.gank,
        toolbarIndicator = R.mipmap.ic_ab_drawer,
        menuDefaultCheckedItem = R.id.action_gank)
public class MainActivity extends BaseActivity<IGankDataPresenter> implements IGankListView {

    @Override
    protected void initView() {

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        presenter = new IGankDataPresenterImpl(this, GankType.android+"");
    }

    @Override
    public void updateGankListView(List<BaseGankData> data, DataLoadState state, String type) {

    }
}
