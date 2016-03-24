package com.tanchaoyin.diandian.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tanchaoyin.diandian.BuildConfig;
import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.utils.RxBus;
import com.tanchaoyin.diandian.utils.SharedPreferencesUtil;
import com.tanchaoyin.diandian.utils.SlidrUtil;
import com.tanchaoyin.diandian.utils.ThemeUtils;
import com.tanchaoyin.diandian.utils.ToolbarUtils;
import com.tanchaoyin.diandian.utils.ViewUtil;
import com.tanchaoyin.diandian.utils.slidr.model.SlidrInterface;

import butterknife.ButterKnife;
import rx.Observable;

/**
 * Activity 基类
 *
 * Created by TanChaoyin on 2016/3/8.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    public final static String IS_START_ANIM = "IS_START_ANIM";
    public final static String IS_CLOSE_ANIM = "IS_CLOSE_ANIM";
    protected boolean isStartAnim = true;
    protected boolean isCloseAnim = true;
    /**
     * 将代理类通用行为抽出来
     */
    protected T presenter;

    /**
     * 标示该activity是否可滑动退出,默认false
     */
    protected boolean enableSlidr;

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

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        setContentView(getLayoutView());

        ButterKnife.bind(this);

        parseIntent(getIntent());

        initTheme();

        initWindow();

        showActivityInAnim();

        initToolbar();

        if (enableSlidr && !SharedPreferencesUtil.readBoolean("disableSlide")) {
            // 默认开启侧滑，默认是整个页码侧滑
            slidrInterface = SlidrUtil
                    .initSlidrDefaultConfig(this, SharedPreferencesUtil.readBoolean("enableSlideEdge"));
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

    /**
     * 增加了默认的返回finish事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @TargetApi(19)
    private void initWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getStatusBarColor());
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    private void initTheme(){
        ThemeUtils.Theme theme = ThemeUtils.getCurrentTheme(this);
        ThemeUtils.changeTheme(this, theme);
    }

    protected void showActivityInAnim(){
        if (isStartAnim) {
            overridePendingTransition(R.anim.activity_down_up_anim, R.anim.activity_exit_anim);
        }
    }

    //call before super.onCreate(savedInstanceState)
    protected void launchWithNoAnim() {
        isStartAnim = false;
    }

    private void parseIntent(Intent intent){
        if (intent != null) {
            isStartAnim = intent.getBooleanExtra(IS_START_ANIM, true);
            isCloseAnim = intent.getBooleanExtra(IS_CLOSE_ANIM, true);
        }
    }

    public int getStatusBarColor(){
        return getColorPrimary();
    }

    public int getColorPrimary(){
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public int getDarkColorPrimary(){
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    public int getCompactColor(@ColorRes int res){
        if (res <= 0)
            throw new IllegalArgumentException("resource id can not be less 0");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getColor(res);
        }
        return getResources().getColor(res);
    }

    protected void initToolbar(Toolbar toolbar){
        ToolbarUtils.initToolbar(toolbar, this);
    }

    protected void initToolbar(){

    }

    public void reload(boolean anim) {
        Intent intent = getIntent();
        if (!anim) {
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(BaseActivity.IS_START_ANIM, false);
        }
        finish();
        if (!anim) {
            overridePendingTransition(0, 0);
        }
        startActivity(intent);
    }

    protected abstract void initView();

    protected abstract @LayoutRes
    int getLayoutView();

}
