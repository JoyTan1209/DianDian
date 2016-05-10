package com.tanchaoyin.diandian.module.gank.presenter.impl;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.base.BasePresenterImpl;
import com.tanchaoyin.diandian.bean.gank.BaseGankData;
import com.tanchaoyin.diandian.common.DataLoadState;
import com.tanchaoyin.diandian.module.gank.model.IGankInteractor;
import com.tanchaoyin.diandian.module.gank.model.impl.IGankInteractorImpl;
import com.tanchaoyin.diandian.module.gank.presenter.IGankDataPresenter;
import com.tanchaoyin.diandian.module.gank.view.IGankListView;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
public class IGankDataPresenterImpl extends BasePresenterImpl<IGankListView,List<BaseGankData>> implements IGankDataPresenter {

    private boolean hasInit;

    private boolean isRefresh = true;

    private int startPage;

    private String gankType;

    private IGankInteractor<List<BaseGankData>> iGankInteractor;

    public IGankDataPresenterImpl(IGankListView view,String type) {
        super(view);
        iGankInteractor = new IGankInteractorImpl();
        subscription = iGankInteractor.requestGankList(this,type,10,startPage);
        gankType = type;
    }

    @Override
    public void beforeRequest() {
        super.beforeRequest();
        if (!hasInit) {
            view.showProgress();
        }
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        view.updateGankListView(null,isRefresh ? DataLoadState.STATE__LOAD_MORE_FAIL:DataLoadState.STATE__LOAD_MORE_FAIL,gankType);
    }

    @Override
    public void requestSuccess(List<BaseGankData> data) {
        super.requestSuccess(data);

        KLog.e("请求成功");
        if (data != null) {
            startPage += 20;
        }
        view.updateGankListView(data,
                isRefresh ? DataLoadState.STATE_REFRESH_SUCCESS : DataLoadState.STATE_LOAD_MORE_SUCCESS,gankType);
    }

    @Override
    public void refreshData() {
        startPage = 0;
        isRefresh = true;
        subscription = iGankInteractor.requestGankList(this,gankType,0,startPage);
    }

    @Override
    public void loadMoreData() {
        isRefresh = false;
        subscription = iGankInteractor.requestGankList(this,gankType,0,startPage);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
