package com.tanchaoyin.diandian.module.gank.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.app.AppManager;
import com.tanchaoyin.diandian.base.BaseActivity;
import com.tanchaoyin.diandian.base.BaseFragment;
import com.tanchaoyin.diandian.base.adapter.BaseFragmentAdapter;
import com.tanchaoyin.diandian.http.GankType;
import com.tanchaoyin.diandian.module.gank.presenter.IGankPresenter;
import com.tanchaoyin.diandian.module.gank.presenter.impl.IGankPresenterImpl;
import com.tanchaoyin.diandian.module.gank.view.IGankView;
import com.tanchaoyin.diandian.utils.ToolbarUtils;
import com.tanchaoyin.diandian.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GnakActivity extends BaseActivity<IGankPresenter> implements IGankView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void initView() {

        AppManager.getAppManager().orderNavActivity(getClass().getName(), false);

        presenter = new IGankPresenterImpl(this);

        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_gnak;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ToolbarUtils.initToolbar(toolbar, this);
    }

    @Override
    public void initViewPager() {

        /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);*/

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
                }

                else {
                    final GankListFragment fragment = GankListFragment
                            .newInstance(val.toString(), postion);
                    fragments.add(fragment);
                }

                title.add(val.toString());
                postion ++;
            }

            if (viewPager.getAdapter() == null) {
                // 初始化ViewPager
                BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(),
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

    @Override
    public void initRxBusEvent() {

    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(R.string.app_name);
    }
}
