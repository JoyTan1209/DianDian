package com.tanchaoyin.diandian.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TanChaoyin on 2016/3/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GankData {

    @JsonProperty("results")
    public ArrayList<BaseGankData> results;
}
