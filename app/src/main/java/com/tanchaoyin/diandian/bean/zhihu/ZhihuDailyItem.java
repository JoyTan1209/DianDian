package com.tanchaoyin.diandian.bean.zhihu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhihuDailyItem implements Serializable {

    @JsonProperty("images")
    public String[] images;

    @JsonProperty("type")
    public int type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("date")
    public String date;
}
