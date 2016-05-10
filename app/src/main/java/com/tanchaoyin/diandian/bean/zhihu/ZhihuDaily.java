package com.tanchaoyin.diandian.bean.zhihu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhihuDaily {

    @JsonProperty("date")
    public String date;

    @JsonProperty("top_stories")
    public ArrayList<ZhihuDailyItem> mZhihuDailyItems;

    @JsonProperty("stories")
    public ArrayList<ZhihuDailyItem> stories;
}
