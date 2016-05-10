package com.tanchaoyin.diandian.utils;

import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.tanchaoyin.diandian.api.gank.GankType;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class GanKDataTagUtil {

    /**
     * @param dataTagTV dataTagTV
     * @param url       url
     */
    public static void setTag(int type,TextView dataTagTV, String url) {
        String key = UrlMatch.processUrl(url);
        GradientDrawable drawable = (GradientDrawable) dataTagTV.getBackground();
        if (UrlMatch.url2Content.containsKey(key)) {
            drawable.setColor(UrlMatch.url2Color.get(key));
            dataTagTV.setText(UrlMatch.url2Content.get(key));
        } else {
            if (type == GankType.video) {
                drawable.setColor(UrlMatch.OTHER_VIDEO_COLOR);
                dataTagTV.setText(UrlMatch.OTHER_VIDEO_CONTENT);
            } else {
                // github 的要特殊处理
                if (url.contains(UrlMatch.GITHUB_PREFIX)) {
                    drawable.setColor(UrlMatch.url2Color.get(UrlMatch.GITHUB_PREFIX));
                    dataTagTV.setText(UrlMatch.url2Content.get(UrlMatch.GITHUB_PREFIX));
                } else {
                    drawable.setColor(UrlMatch.OTHER_BLOG_COLOR);
                    dataTagTV.setText(UrlMatch.OTHER_BLOG_CONTENT);
                }
            }
        }
    }
}
