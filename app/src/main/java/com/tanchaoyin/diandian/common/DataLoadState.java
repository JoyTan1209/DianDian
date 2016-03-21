package com.tanchaoyin.diandian.common;

/**
 *
 * 数据加载状态
 *
 * Created by TanChaoyin on 2016/3/11.
 */
public enum DataLoadState {

    /**
     * 刷新成功
     */
    STATE_REFRESH_SUCCESS,

    /**
     * 属性失败
     */
    STATE_REFRESH_FAIL,

    /**
     * 加载更多成功
     */
    STATE_LOAD_MORE_SUCCESS,

    /**
     * 加载更多失败
     */
    STATE__LOAD_MORE_FAIL
}
