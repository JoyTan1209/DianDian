package com.tanchaoyin.diandian.http;

import android.util.SparseArray;

import com.tanchaoyin.diandian.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TanChaoyin on 2016/3/17.
 */
public class GankTypeDict {

    public static final int DONT_SWITCH = -1;

   /* public static int[] menuIds = new int[]{
            R.id.navigation_daily,
            R.id.navigation_welfare,
            R.id.navigation_android,
            R.id.navigation_ios,
            R.id.navigation_js,
            R.id.navigation_video,
            R.id.navigation_resources,
            R.id.navigation_app
    };*/
    public static final SparseArray<Integer> menuId2TypeDict = new SparseArray<>();

    public static final HashMap<Integer, String> type2UrlTypeDict = new HashMap<>();
    public static final HashMap<String, Integer> urlType2TypeDict = new HashMap<>();

    public static final HashMap<String, Integer> urlType2ColorDict = new HashMap<>();

    static {
      /*  menuId2TypeDict.put(R.id.navigation_daily, GankType.daily);
        menuId2TypeDict.put(R.id.navigation_welfare, GankType.welfare);
        menuId2TypeDict.put(R.id.navigation_android, GankType.android);
        menuId2TypeDict.put(R.id.navigation_ios, GankType.ios);
        menuId2TypeDict.put(R.id.navigation_js, GankType.js);
        menuId2TypeDict.put(R.id.navigation_resources, GankType.resources);
        menuId2TypeDict.put(R.id.navigation_video, GankType.video);
        menuId2TypeDict.put(R.id.navigation_app, GankType.app);*/

        type2UrlTypeDict.put(GankType.welfare, GankApi.DATA_TYPE_WELFARE);
        type2UrlTypeDict.put(GankType.android, GankApi.DATA_TYPE_ANDROID);
        type2UrlTypeDict.put(GankType.ios, GankApi.DATA_TYPE_IOS);
        type2UrlTypeDict.put(GankType.video, GankApi.DATA_TYPE_REST_VIDEO);
        type2UrlTypeDict.put(GankType.resources, GankApi.DATA_TYPE_EXTEND_RESOURCES);
        type2UrlTypeDict.put(GankType.js, GankApi.DATA_TYPE_JS);
        type2UrlTypeDict.put(GankType.app, GankApi.DATA_TYPE_APP);
        type2UrlTypeDict.put(GankType.recommend, GankApi.DATA_TYPE_RECOMMEND);
        for (Map.Entry<Integer, String> entry : type2UrlTypeDict.entrySet()) {
            urlType2TypeDict.put(entry.getValue(), entry.getKey());
        }

        urlType2ColorDict.put(GankApi.DATA_TYPE_WELFARE, 0xffFFAEC9);
        urlType2ColorDict.put(GankApi.DATA_TYPE_ANDROID, 0xff388E3C);
        urlType2ColorDict.put(GankApi.DATA_TYPE_IOS, 0xff0377BC);
        urlType2ColorDict.put(GankApi.DATA_TYPE_REST_VIDEO, 0xff039588);
        urlType2ColorDict.put(GankApi.DATA_TYPE_EXTEND_RESOURCES, 0xff546E7A);
        urlType2ColorDict.put(GankApi.DATA_TYPE_JS, 0xffEF6C02);
        urlType2ColorDict.put(GankApi.DATA_TYPE_APP, 0xffC02E34);
        urlType2ColorDict.put(GankApi.DATA_TYPE_RECOMMEND, 0xff4527A0);
    }
}
