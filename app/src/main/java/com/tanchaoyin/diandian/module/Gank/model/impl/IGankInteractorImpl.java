package com.tanchaoyin.diandian.module.gank.model.impl;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.api.gank.manager.RetrofitManagerGank;
import com.tanchaoyin.diandian.bean.gank.BaseGankData;
import com.tanchaoyin.diandian.callback.RequestCallback;
import com.tanchaoyin.diandian.module.gank.model.IGankInteractor;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
public class IGankInteractorImpl implements IGankInteractor<List<BaseGankData>> {
    @Override
    public Subscription requestGankList(final RequestCallback<List<BaseGankData>> callback, String type, int size, int startPage) {
        KLog.e("Gank数据列表" + type + size);
        return RetrofitManagerGank.getInstance()
                .getGankListObservable(type, size, startPage)
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
                        KLog.e("错误时处理" + throwable + "----" + throwable.getLocalizedMessage());
                    }
                }).subscribe(new Subscriber<ArrayList<BaseGankData>>() {
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
                    public void onNext(ArrayList<BaseGankData> baseGankDatas) {
                        callback.requestSuccess(baseGankDatas);
                    }
                });
    }
}
