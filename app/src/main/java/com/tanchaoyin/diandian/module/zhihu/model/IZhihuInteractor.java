package com.tanchaoyin.diandian.module.zhihu.model;

import com.tanchaoyin.diandian.callback.RequestCallback;

import rx.Subscription;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
public interface IZhihuInteractor<T> {

    Subscription requestZhihuDailyList(RequestCallback<T> callback);

    Subscription requestDailyList(RequestCallback<T> callback, String date);
}
