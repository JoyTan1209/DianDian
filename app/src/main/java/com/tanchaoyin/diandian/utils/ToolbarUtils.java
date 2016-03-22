package com.tanchaoyin.diandian.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.BaseActivity;

/**
 * Created by TanChaoyin on 2016/3/22.
 */
public class ToolbarUtils {

    public static void initToolbar(Toolbar toolbar, AppCompatActivity activity){
        if (toolbar == null || activity == null)
            return;
        if (activity instanceof BaseActivity){
            toolbar.setBackgroundColor(((BaseActivity) activity).getColorPrimary());
        }else {
            toolbar.setBackgroundColor(activity.getResources().getColor(R.color.toolbar_bg_color));
        }
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.toolbar_title_color));
        toolbar.collapseActionView();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_back);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
