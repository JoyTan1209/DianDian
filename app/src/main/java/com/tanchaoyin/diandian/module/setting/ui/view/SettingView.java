package com.tanchaoyin.diandian.module.setting.ui.view;

import android.preference.Preference;

import com.tanchaoyin.diandian.base.BaseView;

/**
 * Created by TanChaoyin on 2016/3/22.
 */
public interface SettingView extends BaseView {
    void findPreference();
    void setRightHandModePreferenceChecked(boolean checked);
    void setCardLayoutPreferenceChecked(boolean checked);
    void setFeedbackPreferenceSummary(CharSequence c);
    void setFeedbackPreferenceClickListener(Preference.OnPreferenceClickListener l);
    void initPreferenceListView(android.view.View view);
    void showSnackbar(String message);
    void showThemeChooseDialog();
    boolean isResume();
    void reload();
}
