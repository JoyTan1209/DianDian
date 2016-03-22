package com.tanchaoyin.diandian.module.setting.ui.presenter.impl;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.preference.Preference;
import android.text.TextUtils;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.BasePresenterImpl;
import com.tanchaoyin.diandian.module.setting.ui.presenter.ISettingPresenter;
import com.tanchaoyin.diandian.module.setting.ui.view.SettingView;
import com.tanchaoyin.diandian.utils.PreferenceUtils;
import com.tanchaoyin.diandian.utils.ThemeUtils;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/22.
 */
public class ISettingPresenterImpl extends BasePresenterImpl<SettingView,List<String>> implements ISettingPresenter {

    private boolean isRightHandMode = false;
    private boolean isCardLayout = false;
    private PreferenceUtils mPreferenceUtils;
    private Context mContext;

    public ISettingPresenterImpl(Context mContext,SettingView settingView) {
        super(settingView);
        this.mContext = mContext;
        view.findPreference();
    }


    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (view.isResume() && preference == null){
            return false;
        }
        String key = preference.getKey();
        if (TextUtils.equals(key, getString(mContext, R.string.right_hand_mode_key))){
            isRightHandMode = !isRightHandMode;
            mPreferenceUtils.saveParam(getString(mContext, R.string.right_hand_mode_key), isRightHandMode);
        }

        if (TextUtils.equals(key, getString(mContext, R.string.card_note_item_layout_key))){
            isCardLayout = !isCardLayout;
            mPreferenceUtils.saveParam(getString(mContext, R.string.card_note_item_layout_key), isCardLayout);
        }

        if (TextUtils.equals(key, getString(mContext, R.string.change_theme_key))){
            view.showThemeChooseDialog();
        }

        if (TextUtils.equals(key, getString(mContext, R.string.pay_for_me_key))){
           /* Intent intent = new Intent(mContext, PayActivity.class);
            mContext.startActivity(intent);*/
        }

        if (TextUtils.equals(key, mContext.getString(R.string.give_favor_key))){
            giveFavor();
        }
        return false;
    }

    @Override
    public void initFeedbackPreference() {

        Uri uri = Uri.parse("mailto:lgpszu@163.com");
        final Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (infos == null || infos.size() <= 0){
            view.setFeedbackPreferenceSummary(mContext.getString(R.string.no_email_app_tip));
            return;
        }
        Preference.OnPreferenceClickListener l = (preference -> {
            mContext.startActivity(intent);
            return true;
        });
        view.setFeedbackPreferenceClickListener(l);

    }

    @Override
    public void initOtherPreference() {
        isCardLayout = mPreferenceUtils.getBooleanParam(getString(mContext,
                R.string.card_note_item_layout_key), true);
        isRightHandMode = mPreferenceUtils.getBooleanParam(getString(mContext,
                R.string.right_hand_mode_key));
        view.setCardLayoutPreferenceChecked(isCardLayout);
        view.setRightHandModePreferenceChecked(isRightHandMode);
    }

    @Override
    public void giveFavor() {
        try{
            Uri uri = Uri.parse("market://details?id="+ mContext.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onThemeChoose(int position) {
        int value = ThemeUtils.getCurrentTheme(mContext).getIntValue();
        if (value != position) {
            mPreferenceUtils.saveParam(getString(mContext, R.string.change_theme_key), position);
            notifyChangeTheme();
        }
    }

    @Override
    public void notifyChangeTheme() {
       /* if (event == null){
            event = new MainPresenter.NotifyEvent<>();
        }
        event.setType(MainPresenter.NotifyEvent.CHANGE_THEME);
        //post change theme event immediately
        EventBus.getDefault().post(event);*/
        view.reload();
    }

    @Override
    public String getString(Context context, int string) {
        if (context != null)
            return context.getString(string);
        return "";
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
