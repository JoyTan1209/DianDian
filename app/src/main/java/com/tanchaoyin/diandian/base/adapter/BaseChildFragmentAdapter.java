package com.tanchaoyin.diandian.base.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.tanchaoyin.diandian.base.BaseFragment;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class BaseChildFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;

    /**
     * 更新频道，前面固定的不更新，后面一律更新
     *
     * @param fragment
     */
    public void updateFragments(BaseFragment fragment) {
            final FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.commit();

        notifyDataSetChanged();
    }

    private BaseFragment mFragment;

    public BaseChildFragmentAdapter(FragmentManager fm,BaseFragment fragment) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return mTitles.get(position);
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //得到缓存的fragment
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        //得到tag，这点很重要
        String fragmentTag = fragment.getTag();

        if (((BaseFragment) fragment).isPostUpdate()) {
            //如果这个fragment需要更新
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            //移除旧的fragment
            ft.remove(fragment);
            //换成新的fragment
            fragment = mFragments.get(position);
            //添加新fragment时必须用前面获得的tag，这点很重要
            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();

            KLog.e("如果这个fragment需要更新");

            //复位更新标志
                    ((BaseFragment) fragment).setPostUpdate(false);
        }
        return fragment;
        // return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }*/
}
