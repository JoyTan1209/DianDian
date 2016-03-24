package com.tanchaoyin.diandian.app;

import android.app.Application;
import android.content.Context;

import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tanchaoyin.diandian.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by TanChaoyin on 2016/3/8.
 */
public class App extends Application {

    public static Context context;

    /**
     * 内存监控
     */
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        mRefWatcher = LeakCanary.install(this);

        context = this;

        // KLog 初始化
        KLog.init(BuildConfig.DEBUG);
    }

    /**
     *  获取内存监控
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {

        App app =   (App)context.getApplicationContext();

        return app.mRefWatcher;
    }


    /**
     * 获取ApplicationContext
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }
}
