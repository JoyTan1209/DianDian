package com.tanchaoyin.diandian.module.zhihu.presenter.impl;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.base.BasePresenterImpl;
import com.tanchaoyin.diandian.bean.zhihu.ZhihuDaily;
import com.tanchaoyin.diandian.common.DataLoadState;
import com.tanchaoyin.diandian.module.zhihu.model.IZhihuInteractor;
import com.tanchaoyin.diandian.module.zhihu.model.impl.IZhihuInteractorImpl;
import com.tanchaoyin.diandian.module.zhihu.presenter.IZhihuPresenter;
import com.tanchaoyin.diandian.module.zhihu.view.IZhihuView;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
public class IZhihuPresenterImpl extends BasePresenterImpl<IZhihuView,ZhihuDaily> implements IZhihuPresenter {

    private boolean hasInit;

    private boolean isRefresh = true;

    private IZhihuInteractor<ZhihuDaily> iZhihuInteractor;

    private static IZhihuPresenterImpl iZhihuPresenterImplInstance;

    public static IZhihuPresenterImpl getInstance(IZhihuView view) {
        if (null == iZhihuPresenterImplInstance) {
            iZhihuPresenterImplInstance = new IZhihuPresenterImpl(view);
        }

        return iZhihuPresenterImplInstance;
    }

    public IZhihuPresenterImpl(IZhihuView view) {
        super(view);
        iZhihuInteractor = new IZhihuInteractorImpl();
        iZhihuInteractor.requestZhihuDailyList(this);
    }

    @Override
    public void beforeRequest() {
        super.beforeRequest();
        if (!hasInit) {
            view.showProgress();
        }
    }

    @Override
    public void requestComplete() {
        super.requestComplete();
    }

    @Override
    public void requestSuccess(ZhihuDaily data) {
        super.requestSuccess(data);
        KLog.e("请求成功");
        view.updateZhihuListView(data,isRefresh ? DataLoadState.STATE_REFRESH_SUCCESS : DataLoadState.STATE_LOAD_MORE_SUCCESS);
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        view.updateZhihuListView(null,isRefresh ? DataLoadState.STATE_REFRESH_SUCCESS : DataLoadState.STATE_LOAD_MORE_SUCCESS);
    }

    @Override
    public void refreshData() {
        isRefresh = true;
        subscription = iZhihuInteractor.requestZhihuDailyList(this);
    }

    @Override
    public void loadMoreData(String date) {
        isRefresh = false;
        subscription = iZhihuInteractor.requestDailyList(this, date);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != iZhihuInteractor) {
            iZhihuInteractor = null;
        }
        if (null != subscription) {
            subscription = null;
        }
        if (null != view) {
            view = null;
        }
        if (null != iZhihuPresenterImplInstance) {
            iZhihuPresenterImplInstance = null;
        }
    }
}
