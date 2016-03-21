package com.tanchaoyin.diandian.http.service;

import com.tanchaoyin.diandian.bean.GankDaily;
import com.tanchaoyin.diandian.bean.BaseGankData;
import com.tanchaoyin.diandian.bean.GankData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
public interface GankService {

    /**
     * @param year  year
     * @param month month
     * @param day   day
     * @return Observable<GankDaily>
     */
    @GET("day/{year}/{month}/{day}")
    Observable<GankDaily> getDaily(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day
    );

    /**
     * 找妹子、Android、iOS、前端、扩展资源、休息视频
     *
     * @param type 数据类型
     * @param size 数据个数
     * @param page 第几页
     * @return Observable<GankWelfare>
     */
    @GET("data/{type}/{size}/{page}")
    Observable<GankData> getData(
            @Path("type") String type,
            @Path("size") int size,
            @Path("page") int page
    );
}
