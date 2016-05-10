package com.tanchaoyin.diandian.module.zhihu.view;

import com.tanchaoyin.diandian.base.BaseView;
import com.tanchaoyin.diandian.bean.zhihu.ZhihuDaily;
import com.tanchaoyin.diandian.common.DataLoadState;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
public interface IZhihuView extends BaseView {

    void updateZhihuListView(ZhihuDaily data, DataLoadState state);
}
