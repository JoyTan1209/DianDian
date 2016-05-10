package com.tanchaoyin.diandian.module.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.BaseActivity;
import com.tanchaoyin.diandian.base.adapter.SimpleListAdapter;
import com.tanchaoyin.diandian.module.home.adapter.DrawerListAdapter;
import com.tanchaoyin.diandian.module.home.presenter.IMainPresenter;
import com.tanchaoyin.diandian.module.home.presenter.impl.IMainPresenterImpl;
import com.tanchaoyin.diandian.module.home.view.MainView;
import com.tanchaoyin.diandian.utils.ToolbarUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity<IMainPresenter> implements MainView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.left_drawer_listview)
    ListView mDrawerMenuListView;
    @Bind(R.id.left_drawer)
    View drawerRootView;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment currentFragment;
    private ArrayList<Fragment> mFragments;
    private ArrayList<Integer> mTitles;

    private IMainPresenter mainPresenter;

    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        launchWithNoAnim();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolbar(){
        ToolbarUtils.initToolbar(toolbar, this);
    }

    @Override
    protected void initView() {

        List<String> drawerList = Arrays.asList(this.getResources()
                .getStringArray(R.array.drawer_content));
        mainPresenter = new IMainPresenterImpl(this,this,drawerList);
        mainPresenter.initLeftMenu();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        if (null != toolbar) {
            toolbar.setNavigationOnClickListener((view) -> mainPresenter.OnNavigationOnClick());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void initLeftMenu(ArrayList<Fragment> fragments, ArrayList<Integer> titles) {
        mFragments = fragments;
        mTitles = titles;
        switchFragment(mFragments.get(0), getString(titles.get(0)));
    }

    @Override
    public void initDrawerView(List<String> list) {
        SimpleListAdapter adapter = new DrawerListAdapter(this, list);
        mDrawerMenuListView.setAdapter(adapter);
       /* mDrawerMenuListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) ->
                mainPresenter.onDrawerItemSelect(position));*/
        mDrawerMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout linearLayout = (LinearLayout) view;
                TextView textView = (TextView) linearLayout.getChildAt(0);
                textView.getText();
                switchFragment(mFragments.get(position), textView.getText().toString());
                title = textView.getText().toString();
                mainPresenter.onDrawerClosed(title);
                closeDrawer();
            }
        });

//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0)
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                mainPresenter.onDrawerOpened();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                mainPresenter.onDrawerClosed(title);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(getCompactColor(R.color.drawer_scrim_color));
    }

    @Override
    public void setDrawerItemChecked(int position) {
        mDrawerMenuListView.setItemChecked(position, true);
    }

    @Override
    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(drawerRootView);
    }

    @Override
    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(drawerRootView)) {
            mDrawerLayout.closeDrawer(drawerRootView);
        }
    }

    @Override
    public void openOrCloseDrawer() {
        if (mDrawerLayout.isDrawerOpen(drawerRootView)) {
            mDrawerLayout.closeDrawer(drawerRootView);
        } else {
            mDrawerLayout.openDrawer(drawerRootView);
        }
    }

    @Override
    public void setMenuGravity(int gravity) {
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) drawerRootView.getLayoutParams();
        params.gravity = gravity;
        drawerRootView.setLayoutParams(params);
    }

    @Override
    public void moveTaskToBack() {
        super.moveTaskToBack(true);
    }

    @Override
    public void reCreate() {
        super.recreate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mainPresenter.onKeyDown(keyCode) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       /* MenuItem searchItem = menu.findItem(R.id.action_search);
        //searchItem.expandActionView();
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ComponentName componentName = getComponentName();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));
        searchView.setQueryHint(getString(R.string.search_note));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerAdapter.getFilter().filter(s);
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, mainPresenter);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (mainPresenter.onOptionsItemSelected(item.getItemId())){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment, String title) {
        Slide slideTransition;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Gravity.START部分机型崩溃java.lang.IllegalArgumentException: Invalid slide direction
            slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(700);
            fragment.setEnterTransition(slideTransition);
            fragment.setExitTransition(slideTransition);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.replace, fragment).commit();
        currentFragment = fragment;
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(title);
    }
}
