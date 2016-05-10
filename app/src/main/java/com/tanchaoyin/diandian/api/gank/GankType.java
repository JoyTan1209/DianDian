package com.tanchaoyin.diandian.api.gank;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by TanChaoyin on 2016/3/16.
 */
public class GankType {
    public static final int daily = 22061;
    public static final int android = 22062;
    public static final int ios = 22063;
    public static final int js = 22064;
    public static final int resources = 22065;
    public static final int welfare = 22066;
    public static final int video = 22067;
    public static final int app = 22068;
    public static final int recommend = 22060;

    public static final Map<Integer,String> gankTypeMap = new LinkedHashMap<>();

    public static void initMap() {
        gankTypeMap.put(daily,"每日精彩");
        gankTypeMap.put(android,GankApi.DATA_TYPE_ANDROID);
        gankTypeMap.put(ios,GankApi.DATA_TYPE_IOS);
        gankTypeMap.put(js,GankApi.DATA_TYPE_JS);
        gankTypeMap.put(resources,GankApi.DATA_TYPE_EXTEND_RESOURCES);
        gankTypeMap.put(welfare,GankApi.DATA_TYPE_WELFARE);
        gankTypeMap.put(video,GankApi.DATA_TYPE_REST_VIDEO);
        gankTypeMap.put(app,GankApi.DATA_TYPE_APP);
        gankTypeMap.put(recommend,GankApi.DATA_TYPE_RECOMMEND);
    }
}
