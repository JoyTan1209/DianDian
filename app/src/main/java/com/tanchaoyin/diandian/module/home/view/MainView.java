package com.tanchaoyin.diandian.module.home.view;

import android.support.v4.app.Fragment;

import com.tanchaoyin.diandian.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/23.
 */
public interface MainView extends BaseView {

    void setToolbarTitle(String title);

    void initLeftMenu(ArrayList<Fragment> fragments, ArrayList<Integer> titles);

    void initDrawerView(List<String> list);

    void setDrawerItemChecked(int position);

    boolean isDrawerOpen();

    void closeDrawer();

    void openOrCloseDrawer();

    void setMenuGravity(int gravity);

    void moveTaskToBack();

    void reCreate();
}
