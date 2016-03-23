package com.tanchaoyin.diandian.module.home.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.BasePresenterImpl;
import com.tanchaoyin.diandian.module.home.presenter.IMainPresenter;
import com.tanchaoyin.diandian.module.home.view.MainView;
import com.tanchaoyin.diandian.module.setting.ui.SettingActivity;
import com.tanchaoyin.diandian.utils.PreferenceUtils;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/23.
 */
public class IMainPresenterImpl extends BasePresenterImpl<MainView,List<String>> implements IMainPresenter,MenuItemCompat.OnActionExpandListener {

    private Context mContext;
    private List<String> drawerList;
    private PreferenceUtils mPreferenceUtils;
    private boolean isRightHandMode = false;

    public IMainPresenterImpl(Context context,MainView view, List<String> drawerList) {
        super(view);
        this.mContext = context;
        this.drawerList = drawerList;
        initDrawer();
    }

    @Override
    public void initDrawer() {
        view.initDrawerView(drawerList);
        view.setDrawerItemChecked(0);
        view.setToolbarTitle(drawerList.get(0));
    }

    @Override
    public void initMenuGravity() {
        boolean end = mPreferenceUtils.getBooleanParam(mContext.getString(R.string.right_hand_mode_key));
        if (end){
            view.setMenuGravity(Gravity.END);
        }else {
            view.setMenuGravity(Gravity.START);
        }
        isRightHandMode = end;
    }

    @Override
    public void onDrawerItemSelect(int position) {

    }

    @Override
    public void onDrawerOpened() {

        view.setToolbarTitle(mContext.getResources().getString(R.string.app_name));
    }

    @Override
    public void onDrawerClosed() {

        view.setToolbarTitle(drawerList.get(0));
    }

    @Override
    public void initItemLayoutManager() {

    }

    @Override
    public void OnNavigationOnClick() {
        view.openOrCloseDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(int id) {
        switch (id){
            case R.id.setting: startSettingActivity();
                return true;
            case R.id.about: startAboutActivity();
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
       return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    public void startSettingActivity(){
        Intent intent = new Intent(mContext, SettingActivity.class);
        mContext.startActivity(intent);
    }

    public void startAboutActivity(){
        /*Intent intent = new Intent(mContext, AboutActivity.class);
        mContext.startActivity(intent);*/
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (view.isDrawerOpen()){
                view.closeDrawer();
            }else {
                view.moveTaskToBack();
            }
            return true;
        }
        return false;
    }
}
