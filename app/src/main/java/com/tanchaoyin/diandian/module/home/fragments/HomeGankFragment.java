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

        List<BaseFragment> fragments = new ArrayList<>();
        final List<String> title = new ArrayList<>();

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

                // 23.2.0 TabLayout的一个Bug：对Tab做删减，二次调用setupWithViewPager报 Tab belongs to a different TabLayout，还有持有的sTabPool内存泄露
                // 解决方法：https://code.google.com/p/android/issues/detail?id=201827
                // sTabPool的size只有16，现在直接创建16个无用的tab存到sTabPool，因为sTabPool在TabLayout.removeAllTabs()调用release方法貌似是清空不了它持有的数据，
                // 于是后面在tabLayout.setupWithViewPager时不会再被它重用导致 Tab belongs to a different TabLayout问题了
                // 23.2.1 已经修复此bug
                /*TabLayout.Tab uselessTab;
                for (int j = 0; j < 16; j++) {
                    uselessTab = tabLayout.newTab();
                }*/

            } else {
                final BaseFragmentAdapter adapter = (BaseFragmentAdapter) viewPager.getAdapter();
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
}
