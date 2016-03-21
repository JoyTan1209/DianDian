package com.tanchaoyin.diandian.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by TanChaoyin on 2016/3/11.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseGankData {

    // 发布人
    @JsonProperty("who")
    public String who;

    // 发布时间
    @JsonProperty("publishedAt")
    public Date publishedAt;

    // 标题
    @JsonProperty("desc")
    public String desc;

    // 类型， 一般都是"福利"
    @JsonProperty("type")
    public String type;

    // 图片url
    @JsonProperty("url")
    public String url;

    // 是否可用
    @JsonProperty("used")
    public Boolean used;

    // 对象id
    @JsonProperty("objectId")
    public String objectId;

    // 创建时间
    @JsonProperty("createdAt")
    public Date createdAt;

    // 更新时间
    @JsonProperty("updatedAt")
    public Date updatedAt;
}
