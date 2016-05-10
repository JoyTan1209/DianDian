package com.tanchaoyin.diandian.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;
import com.tanchaoyin.diandian.annotation.ActivityFragmentInject;
import com.tanchaoyin.diandian.app.App;

import butterknife.ButterKnife;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView, View.OnClickListener {

    // 将代理类通用行为抽出来
    protected T presenter;

    protected View fragmentRootView;

    protected int contentViewId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentRootView = inflater.inflate(getLayoutView(), container, false);

        ButterKnife.bind(fragmentRootView);

        initView(fragmentRootView);

        return fragmentRootView;
    }

    protected abstract void initView(View fragmentRootView);

    protected abstract @LayoutRes
    int getLayoutView();

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = (ViewGroup) fragmentRootView.getParent();
        if (null != parent) {
            parent.removeView(fragmentRootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
        // 使用 RefWatcher 监控 Fragment
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
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
}
