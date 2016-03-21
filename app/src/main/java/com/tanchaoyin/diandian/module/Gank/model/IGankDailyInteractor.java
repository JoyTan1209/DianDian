package com.tanchaoyin.diandian.module.Gank.model;

import com.tanchaoyin.diandian.callback.RequestCallback;
import com.tanchaoyin.diandian.module.Gank.presenter.impl.IGankDailyDataPresenterImpl;

import rx.Subscription;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public interface IGankDailyInteractor<T> {

    Subscription requestGankDailyList(RequestCallback<T> callback, IGankDailyDataPresenterImpl.EasyDate easyDate);
}
