package com.tanchaoyin.diandian.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GankDaily implements Serializable {

    @JsonProperty("results")
    public DailyResults results;

    @JsonProperty("category")
    public ArrayList<String> category;

    public class DailyResults {

        @JsonProperty("福利")
        public ArrayList<BaseGankData> welfareData;

        @JsonProperty("Android")
        public ArrayList<BaseGankData> androidData;

        @JsonProperty("iOS")
        public ArrayList<BaseGankData> iosData;

        @JsonProperty("前端")
        public ArrayList<BaseGankData> jsData;

        @JsonProperty("休息视频")
        public ArrayList<BaseGankData> videoData;

        @JsonProperty("拓展资源")
        public ArrayList<BaseGankData> resourcesData;

        @JsonProperty("App")
        public ArrayList<BaseGankData> appData;

        @JsonProperty("瞎推荐")
        public ArrayList<BaseGankData> recommendData;

    }
}
