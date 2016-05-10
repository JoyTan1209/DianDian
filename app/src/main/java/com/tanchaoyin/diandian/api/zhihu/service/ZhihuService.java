package com.tanchaoyin.diandian.api.zhihu.service;

import com.tanchaoyin.diandian.bean.zhihu.ZhihuDaily;
import com.tanchaoyin.diandian.bean.zhihu.ZhihuStory;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
public interface ZhihuService {

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> getTheDaily(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);
}
