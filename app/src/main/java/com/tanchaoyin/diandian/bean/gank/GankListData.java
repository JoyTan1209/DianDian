package com.tanchaoyin.diandian.bean.gank;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
public class GankListData implements Serializable {

    @JsonProperty("results")
    public ArrayList<BaseGankData> results;
}
