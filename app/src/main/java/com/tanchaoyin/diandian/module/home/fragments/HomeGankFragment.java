package com.tanchaoyin.diandian.module.home.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.BaseFragment;
import com.tanchaoyin.diandian.base.adapter.BaseFragmentAdapter;
import com.tanchaoyin.diandian.api.gank.GankType;
import com.tanchaoyin.diandian.module.gank.ui.GankDailyListFragment;
import com.tanchaoyin.diandian.module.gank.ui.GankListFragment;
import com.tanchaoyin.diandian.module.home.presenter.impl.IHomeGankPresenterImpl;
import com.tanchaoyin.diandian.module.home.view.IHomeGankView;
import com.tanchaoyin.diandian.module.home.presenter.IHomeGankPresenter;
import com.tanchaoyin.diandian.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by TanChaoyin on 2016/3/28.
 */
public class HomeGankFragment extends BaseFragment<IHomeGankPresenter> implements IHomeGankView {

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    List<String> title;

    List<BaseFragment> fragments;

    @Override
    protected void initView(View fragmentRootView) {

        ButterKnife.bind(this, fragmentRootView);

        presenter = new IHomeGankPresenterImpl(this);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_gank;
    }

    @Override
    public void initViewPager() {

        fragments = new ArrayList<>();
        title = new ArrayList<>();

        GankType.initMap();

        if (GankType.gankTypeMap != null) {

            int postion = 0;
            Iterator iter = GankType.gankTypeMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();

                if (key.equals(GankType.daily)) {
                    final GankDailyListFragment fragment = GankDailyListFragment
                            .newInstance(val.toString(), postion);
                    fragments.add(fragment);
                } else {
                    final GankListFragment fragment = GankListFragment
                            .newInstance(val.toString(), postion);
                    fragments.add(fragment);
                }

                title.add(val.toString());
                postion++;
            }

            if (viewPager.getAdapter() == null) {
                // 初始化ViewPager
                BaseFragmentAdapter adapter = new BaseFragmentAdapter(getActivity().getSupportFragmentManager(),
                        fragments, title);
                viewPager.setAdapter(adapter);
            } else {
                BaseFragmentAdapter adapter = (BaseFragmentAdapter) viewPager.getAdapter();
                adapter.updateFragments(fragments, title);
            }
            viewPager.setCurrentItem(0, false);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setScrollPosition(0, 0, true);
            // 根据Tab的长度动态设置TabLayout的模式
            ViewUtil.dynamicSetTablayoutMode(tabLayout);
        } else {
            toast("数据异常");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onResume();
        viewPager = null;
        tabLayout = null;
        fragments.clear();
        title.clear();
    }
}
