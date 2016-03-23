package com.tanchaoyin.diandian.module.home.presenter;

import com.tanchaoyin.diandian.base.BasePresenter;

/**
 * Created by TanChaoyin on 2016/3/23.
 */
public interface IMainPresenter extends BasePresenter {

    void initDrawer();

    void initMenuGravity();

    void onDrawerItemSelect(int position);

    void onDrawerOpened();

    void onDrawerClosed();

    void initItemLayoutManager();

    void OnNavigationOnClick();

    boolean onOptionsItemSelected(int id);

    boolean onKeyDown(int keyCode);
}
