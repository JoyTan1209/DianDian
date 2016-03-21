package com.tanchaoyin.diandian.base;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.callback.RequestCallback;

import rx.Subscription;

/**
 * Created by TanChaoyin on 2016/3/8.
 */
public class BasePresenterImpl<T extends BaseView, V> implements BasePresenter,RequestCallback<V> {

    protected Subscription subscription;

    protected T view;

    public BasePresenterImpl(T view) {

        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        if (null != subscription && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
        }

        view = null;
    }

    @Override
    public void beforeRequest() {
        view.showProgress();
    }

    @Override
    public void requestError(String msg) {
        KLog.e(msg);
        view.toast(msg);
        view.hideProgress();
    }

    @Override
    public void requestComplete() {
        view.hideProgress();
    }

    @Override
    public void requestSuccess(V data) {

    }
}
