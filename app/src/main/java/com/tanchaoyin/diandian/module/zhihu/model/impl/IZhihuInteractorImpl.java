package com.tanchaoyin.diandian.module.zhihu.model.impl;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.api.zhihu.manager.RetrofitManagerZhihu;
import com.tanchaoyin.diandian.bean.zhihu.ZhihuDaily;
import com.tanchaoyin.diandian.bean.zhihu.ZhihuDailyItem;
import com.tanchaoyin.diandian.callback.RequestCallback;
import com.tanchaoyin.diandian.module.zhihu.model.IZhihuInteractor;

import java.util.List;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
public class IZhihuInteractorImpl implements IZhihuInteractor<ZhihuDaily> {
    @Override
    public Subscription requestZhihuDailyList(RequestCallback<ZhihuDaily> callback) {
        return RetrofitManagerZhihu.getInstance()
                .getZhihuDailyObservable()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.beforeRequest();
                    }
                })
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String data = zhihuDaily.date;
                        for (ZhihuDailyItem zhihuDailyItem: zhihuDaily.stories) {
                            zhihuDailyItem.date = data;
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
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
                    public void onNext(ZhihuDaily zhihuDaily) {
                        callback.requestSuccess(zhihuDaily);
                    }
                });
    }

    @Override
    public Subscription requestDailyList(RequestCallback<ZhihuDaily> callback, String date) {
        return RetrofitManagerZhihu.getInstance()
                .getTheDailyObservable(date)
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String data = zhihuDaily.date;
                        for (ZhihuDailyItem zhihuDailyItem: zhihuDaily.stories) {
                            zhihuDailyItem.date = data;
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
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
                    public void onNext(ZhihuDaily zhihuDaily) {
                        callback.requestSuccess(zhihuDaily);
                    }
                });
    }
}
