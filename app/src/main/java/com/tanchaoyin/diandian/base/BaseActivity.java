package com.tanchaoyin.diandian.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.tanchaoyin.diandian.BuildConfig;
import com.tanchaoyin.diandian.MainActivity;
import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.annotation.ActivityFragmentInject;
import com.tanchaoyin.diandian.app.AppManager;
import com.tanchaoyin.diandian.module.Gank.ui.GnakActivity;
import com.tanchaoyin.diandian.utils.GlideCircleTransformUtil;
import com.tanchaoyin.diandian.utils.RxBus;
import com.tanchaoyin.diandian.utils.SharedPreferencesUtil;
import com.tanchaoyin.diandian.utils.SlidrUtil;
import com.tanchaoyin.diandian.utils.ViewUtil;
import com.tanchaoyin.diandian.utils.slidr.model.SlidrInterface;

import rx.Observable;
import rx.functions.Action1;

/**
 * Activity 基类
 *
 * Created by TanChaoyin on 2016/3/8.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView, View.OnClickListener {

    /**
     * 将代理类通用行为抽出来
     */
    protected T presenter;

    /**
     * 标示该activity是否可滑动退出,默认false
     */
    protected boolean enableSlidr;

    /**
     * 布局的id
     */
    protected int contentViewId;

    /**
     * 是否存在NavigationView
     */
    protected boolean hasNavigationView;

    /**
     * 滑动布局
     */
    protected DrawerLayout drawerLayout;
    /**
     * 侧滑导航布局
     */
    protected NavigationView navigationView;

    private Class mClass;

    /**
     * 菜单的id
     */
    private int menuId;

    /**
     * Toolbar标题
     */
    private int toolbarTitle;

    /**
     * 默认选中的菜单项
     */
    private int menuDefaultCheckedItem;

    /**
     * Toolbar左侧按钮的样式
     */
    private int toolbarIndicator;

    /**
     * 控制滑动与否的接口
     */
    protected SlidrInterface slidrInterface;

    /**
     * 结束Activity的可观测对象
     */
    private Observable<Boolean> finishObservable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {

            ActivityFragmentInject annotation = getClass().getAnnotation(ActivityFragmentInject.class);
            contentViewId = annotation.contentViewId();
            enableSlidr = annotation.enableSlidr();
            hasNavigationView = annotation.hasNavigationView();
            menuId = annotation.menuId();
            toolbarTitle = annotation.toolbarTitle();
            toolbarIndicator = annotation.toolbarIndicator();
            menuDefaultCheckedItem = annotation.menuDefaultCheckedItem();
        } else {
            throw new RuntimeException(
                    "Class must add annotations of ActivityFragmentInitParams.class");
        }

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        setContentView(contentViewId);

        if (enableSlidr && !SharedPreferencesUtil.readBoolean("disableSlide")) {
            // 默认开启侧滑，默认是整个页码侧滑
            slidrInterface = SlidrUtil
                    .initSlidrDefaultConfig(this, SharedPreferencesUtil.readBoolean("enableSlideEdge"));
        }

        initToolbar();

        if (hasNavigationView) {
            initNavigationView();
//            initFinishRxBus();
        }

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.onDestroy();
        }

        if (finishObservable != null) {
            RxBus.get().unregister("finish", finishObservable);
        }

        ViewUtil.fixInputMethodManagerLeak(this);
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onClick(View v) {

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (toolbarTitle != -1) setToolbarTitle(toolbarTitle);
            if (toolbarIndicator != -1) {
                setToolbarIndicator(toolbarIndicator);
            } else {
                setToolbarIndicator(R.mipmap.ic_menu_back);
            }
        }
    }

    protected void setToolbarIndicator(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(resId);
        }
    }

    protected void setToolbarTitle(String str) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(str);
        }
    }

    protected void setToolbarTitle(int strId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(strId);
        }
    }

    protected ActionBar getToolbar() {
        return getSupportActionBar();
    }

    protected View getDecorView() {
        return getWindow().getDecorView();
    }

    protected abstract void initView();

    private void initNavigationView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // 为4.4透明状态栏布局延伸到状态栏做适配
            drawerLayout.setFitsSystemWindows(false);
            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(
                    R.id.coordinator_layout);
            if (coordinatorLayout != null) {
                // CoordinatorLayout设为true才能把布局延伸到状态栏
                coordinatorLayout.setFitsSystemWindows(true);
            }
        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (menuDefaultCheckedItem != -1) navigationView.setCheckedItem(menuDefaultCheckedItem);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        if (item.isChecked()) return true;
                        switch (item.getItemId()) {
                            case R.id.action_gank:
                                mClass = GnakActivity.class;
                                break;
                            case R.id.action_video:
                                mClass = MainActivity.class;
                                break;
                            case R.id.action_photo:
                                mClass = MainActivity.class;
                                break;
                            case R.id.action_settings:
                                mClass = MainActivity.class;
                                break;
                        }
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return false;
                    }
                });
        navigationView.post(new Runnable() {
            @Override
            public void run() {
                final ImageView imageView = (ImageView) BaseActivity.this.findViewById(R.id.avatar);
                Glide.with(navigationView.getContext()).load(R.mipmap.ic_launcher).crossFade()
                        .transform(new GlideCircleTransformUtil(navigationView.getContext()))
                        .into(imageView);
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mClass != null) {
                    showActivityReorderToFront(BaseActivity.this, mClass, false);
                    mClass = null;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    /**
     * 订阅结束自己的事件，这里使用来结束导航的Activity
     */
    private void initFinishRxBus() {
        finishObservable = RxBus.get().register("finish", Boolean.class);
        finishObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean themeChange) {
                try {
                    if (themeChange && !AppManager.getAppManager().getCurrentNavActivity().getName()
                            .equals(BaseActivity.this.getClass().getName())) {
                        finish();
                    } else if (!themeChange) {
                        // 这个是入口新闻页面退出时发起的通知所有导航页面退出的事件
                        finish();
                        KLog.e("结束：" + BaseActivity.this.getClass().getName());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    KLog.e("找不到此类");
                }
            }
        });
    }

    protected void showSnackbar(String msg) {
        Snackbar.make(getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackbar(int id) {
        Snackbar.make(getDecorView(), id, Snackbar.LENGTH_SHORT).show();
    }

    public void showActivityReorderToFront(Activity aty, Class<?> cls, boolean backPress) {

        if (null != cls) {
            AppManager.getAppManager().orderNavActivity(cls.getName(), backPress);

            KLog.e("跳转回去：" + cls.getName());

            Intent intent = new Intent();
            intent.setClass(aty, cls);
            // 此标志用于启动一个Activity的时候，若栈中存在此Activity实例，则把它调到栈顶。不创建多一个
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            aty.startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (0 != menuId) {
            getMenuInflater().inflate(menuId, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerLayout != null && item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else if (item.getItemId() == android.R.id.home && toolbarIndicator == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                // 返回键时未关闭侧栏时关闭侧栏
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            } else if (!(this instanceof MainActivity) && hasNavigationView) {
                try {
                    showActivityReorderToFront(this,
                            AppManager.getAppManager().getLastNavActivity(), true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    KLog.e("找不到类名啊");
                }
                return true;
            } else if (this instanceof MainActivity) {
                // NewsActivity发送通知结束所有导航的Activity
                RxBus.get().post("finish", false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
