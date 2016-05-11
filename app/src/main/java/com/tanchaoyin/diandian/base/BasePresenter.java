package com.tanchaoyin.diandian.base;

/**
 * Presenter 基类
 *
 * Created by TanChaoyin on 2016/3/8.
 */
public interface BasePresenter {

    void onResume();

    void onStart ();

    void onPause();

    void onStop ();

    void onDestroy();
}
