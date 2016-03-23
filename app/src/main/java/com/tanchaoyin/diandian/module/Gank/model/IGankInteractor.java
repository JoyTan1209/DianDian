package com.tanchaoyin.diandian.module.gank.model;

import com.tanchaoyin.diandian.callback.RequestCallback;

import rx.Subscription;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
public interface IGankInteractor<T> {
    Subscription requestGankList(RequestCallback<T> callback, String type, int size, int startPage);
}
