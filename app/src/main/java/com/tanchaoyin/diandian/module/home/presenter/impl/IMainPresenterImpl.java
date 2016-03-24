package com.tanchaoyin.diandian.module.home.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
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

import de.greenrobot.event.EventBus;

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
        EventBus.getDefault().register(this);
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
        EventBus.getDefault().unregister(this);
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

    public void onEventMainThread(NotifyEvent event){
        switch (event.getType()){
            case NotifyEvent.REFRESH_LIST:
                break;
            case NotifyEvent.CREATE_NOTE:
                break;
            case NotifyEvent.UPDATE_NOTE:
                break;
            case NotifyEvent.CHANGE_THEME:
                view.reCreate();
                break;
        }
    }

    public static class NotifyEvent<T>{
        public static final int REFRESH_LIST = 0;
        public static final int CREATE_NOTE = 1;
        public static final int UPDATE_NOTE = 2;
        public static final int CHANGE_THEME = 3;
        public static final int CHANGE_ITEM_LAYOUT = 4;
        public static final int CHANGE_MENU_GRAVITY = 5;
        private int type;
        private T data;
        @IntDef({REFRESH_LIST, CREATE_NOTE, UPDATE_NOTE, CHANGE_THEME,
                CHANGE_ITEM_LAYOUT, CHANGE_MENU_GRAVITY})
        public @interface Type {
        }

        public @Type int getType() {
            return type;
        }

        public void setType(@Type int type) {
            this.type = type;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
