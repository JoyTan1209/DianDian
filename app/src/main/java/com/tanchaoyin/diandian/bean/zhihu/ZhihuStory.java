package com.tanchaoyin.diandian.bean.zhihu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhihuStory implements Serializable {

    @JsonProperty("body")
    public String body;

    @JsonProperty("title")
    public String title;

    @JsonProperty("image")
    public String image;

    @JsonProperty("share_url")
    public String mShareUrl;

    @JsonProperty("css")
    public String[] css;
}
