package com.tanchaoyin.diandian.module.Gank.presenter.impl;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.base.BasePresenterImpl;
import com.tanchaoyin.diandian.bean.BaseGankData;
import com.tanchaoyin.diandian.bean.GankDaily;
import com.tanchaoyin.diandian.common.DataLoadState;
import com.tanchaoyin.diandian.http.GankApi;
import com.tanchaoyin.diandian.module.Gank.model.IGankDailyInteractor;
import com.tanchaoyin.diandian.module.Gank.model.impl.IGankDailyInteractorImpl;
import com.tanchaoyin.diandian.module.Gank.presenter.IGankDataPresenter;
import com.tanchaoyin.diandian.module.Gank.view.IGankDailyView;
import com.tanchaoyin.diandian.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class IGankDailyDataPresenterImpl extends BasePresenterImpl<IGankDailyView,List<GankDaily>> implements IGankDataPresenter {

    private boolean hasInit;

    private boolean isRefresh = true;

    private int startPage = 0;

    private EasyDate currentDate;

    private IGankDailyInteractor<List<GankDaily>> iGankDailyInteractor;

    public IGankDailyDataPresenterImpl(IGankDailyView gankDailyView) {
        super(gankDailyView);
        long time = System.currentTimeMillis();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        this.currentDate = new EasyDate(mCalendar);
        iGankDailyInteractor = new IGankDailyInteractorImpl();
        subscription = iGankDailyInteractor.requestGankDailyList(this, currentDate);
        this.startPage = 1;
    }

    @Override
    public void beforeRequest() {
        super.beforeRequest();
        if (!hasInit) {
            view.showProgress();
        }
    }

    @Override
    public void requestError(String msg) {
        super.requestError(msg);
        view.updateGankListView(null,isRefresh ? DataLoadState.STATE__LOAD_MORE_FAIL:DataLoadState.STATE__LOAD_MORE_FAIL);
    }

    @Override
    public void requestSuccess(List<GankDaily> data) {
        super.requestSuccess(data);

        if (null != data)
            startPage ++;

        KLog.e("请求成功");
        view.updateGankListView(data,
                isRefresh ? DataLoadState.STATE_REFRESH_SUCCESS : DataLoadState.STATE_LOAD_MORE_SUCCESS);
    }

    @Override
    public void refreshData() {
        isRefresh = true;
        subscription = iGankDailyInteractor.requestGankDailyList(this,currentDate);
    }

    @Override
    public void loadMoreData() {
        isRefresh = false;
        subscription = iGankDailyInteractor.requestGankDailyList(this,currentDate);
    }

    /**
     * 查每日干货需要的特殊的类
     */
    public class EasyDate implements Serializable {
        private Calendar calendar;

        public EasyDate(Calendar calendar) {
            this.calendar = calendar;
        }

        public int getYear() {
            return calendar.get(Calendar.YEAR);
        }

        public int getMonth() {
            return calendar.get(Calendar.MONTH) + 1;
        }

        public int getDay() {
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        public List<EasyDate> getPastTime() {
            List<EasyDate> easyDates = new ArrayList<>();
            for (int i = 0; i < GankApi.DEFAULT_DAILY_SIZE; i++) {
                /*
                 * - (page * DateUtils.ONE_DAY) 翻到哪页再找 一页有DEFAULT_DAILY_SIZE这么长
                 * - i * DateUtils.ONE_DAY 往前一天一天 找呀找
                 */
                long time = this.calendar.getTimeInMillis() - ((startPage - 1) * GankApi.DEFAULT_DAILY_SIZE * DateUtils.ONE_DAY) - i * DateUtils.ONE_DAY;
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);
                EasyDate date = new EasyDate(c);
                easyDates.add(date);
            }
            return easyDates;
        }

    }
}
