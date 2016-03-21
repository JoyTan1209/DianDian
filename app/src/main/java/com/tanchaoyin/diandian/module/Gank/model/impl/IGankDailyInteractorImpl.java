package com.tanchaoyin.diandian.module.Gank.model.impl;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.bean.GankDaily;
import com.tanchaoyin.diandian.callback.RequestCallback;
import com.tanchaoyin.diandian.http.manager.RetrofitManager;
import com.tanchaoyin.diandian.module.Gank.model.IGankDailyInteractor;
import com.tanchaoyin.diandian.module.Gank.presenter.impl.IGankDailyDataPresenterImpl;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class IGankDailyInteractorImpl implements IGankDailyInteractor<List<GankDaily>> {

    @Override
    public Subscription requestGankDailyList(RequestCallback<List<GankDaily>> callback, IGankDailyDataPresenterImpl.EasyDate easyDate) {
        KLog.e("GankDaily数据列表" + "--");
        return RetrofitManager.getInstance()
                .getGankDailyListObservable(easyDate)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 订阅之前回调回去显示加载动画
                        callback.beforeRequest();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        KLog.e("错误处理" + throwable + throwable.getLocalizedMessage());
                    }
                }).subscribe(new Subscriber<List<GankDaily>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getLocalizedMessage() + "\n" + e);
                        callback.requestError(e.getLocalizedMessage() + "\n" + e);
                    }

                    @Override
                    public void onNext(List<GankDaily> gankDailies) {
                        callback.requestSuccess(gankDailies);
                    }
                });
    }
}
