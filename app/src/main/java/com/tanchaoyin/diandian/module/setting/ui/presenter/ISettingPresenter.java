package com.tanchaoyin.diandian.module.setting.ui.presenter;

import android.content.Context;
import android.preference.Preference;

import com.tanchaoyin.diandian.base.BasePresenter;

/**
 * Created by TanChaoyin on 2016/3/22.
 */
public interface ISettingPresenter extends BasePresenter {

    boolean onPreferenceTreeClick(Preference preference);

    void initFeedbackPreference();

    void initOtherPreference();

    void giveFavor();

    void onThemeChoose(int position);

    void notifyChangeTheme();

    String getString(Context context, int string);
}
