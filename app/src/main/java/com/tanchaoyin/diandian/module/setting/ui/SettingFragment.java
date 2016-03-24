package com.tanchaoyin.diandian.module.setting.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.adapter.ColorsListAdapter;
import com.tanchaoyin.diandian.module.setting.presenter.ISettingPresenter;
import com.tanchaoyin.diandian.module.setting.presenter.impl.ISettingPresenterImpl;
import com.tanchaoyin.diandian.module.setting.view.SettingView;
import com.tanchaoyin.diandian.utils.DialogUtils;
import com.tanchaoyin.diandian.utils.ThemeUtils;

import java.util.Arrays;
import java.util.List;

import io.github.xhinliang.mdpreference.CheckBoxPreference;
import io.github.xhinliang.mdpreference.Preference;

/**
 * Created by TanChaoyin on 2016/3/22.
 */
public class SettingFragment extends PreferenceFragment implements SettingView {

    public static final String PREFERENCE_FILE_NAME = "note.settings";
    private CheckBoxPreference rightHandModePreference;
    private Preference feedbackPreference;
    private CheckBoxPreference cardLayoutPreference;
    private Preference payMePreference;
    private Preference giveFavorPreference;
    private Preference everAccountPreference;
    ISettingPresenter settingPresenter;
    private SettingActivity activity;
    public static SettingFragment newInstance(){
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() != null && getActivity() instanceof SettingActivity){
            this.activity = (SettingActivity)getActivity();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        getPreferenceManager().setSharedPreferencesName(PREFERENCE_FILE_NAME);
        settingPresenter = new ISettingPresenterImpl(this.activity,this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,  android.preference.Preference preference) {
        settingPresenter.onPreferenceTreeClick(preference);
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void findPreference() {
        rightHandModePreference = (CheckBoxPreference)findPreference(getString(R.string.right_hand_mode_key));
        cardLayoutPreference = (CheckBoxPreference)findPreference(getString(R.string.card_note_item_layout_key));
        feedbackPreference = (Preference)findPreference(getString(R.string.advice_feedback_key));
        payMePreference = (Preference)findPreference(getString(R.string.pay_for_me_key));
        giveFavorPreference = (Preference)findPreference(getString(R.string.give_favor_key));
    }

    @Override
    public void setRightHandModePreferenceChecked(boolean checked) {
        rightHandModePreference.setChecked(checked);
    }

    @Override
    public void setCardLayoutPreferenceChecked(boolean checked) {
        cardLayoutPreference.setChecked(checked);
    }

    @Override
    public void setFeedbackPreferenceSummary(CharSequence c) {
        feedbackPreference.setSummary(c);
    }

    @Override
    public void setFeedbackPreferenceClickListener(Preference.OnPreferenceClickListener l) {
        feedbackPreference.setOnPreferenceClickListener(l);
    }

    @Override
    public void initPreferenceListView(View view) {
        ListView listView = (ListView)view.findViewById(android.R.id.list);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.grey)));
        listView.setDividerHeight((int) getResources().getDimension(R.dimen.preference_divider_height));
        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);
    }

    @Override
    public void showSnackbar(String message) {

    }

    @Override
    public void showThemeChooseDialog() {
        AlertDialog.Builder builder = DialogUtils.makeDialogBuilder(activity);
        builder.setTitle(R.string.change_theme);
        Integer[] res = new Integer[]{R.drawable.red_round, R.drawable.brown_round, R.drawable.blue_round,
                R.drawable.blue_grey_round, R.drawable.yellow_round, R.drawable.deep_purple_round,
                R.drawable.pink_round, R.drawable.green_round};
        List<Integer> list = Arrays.asList(res);
        ColorsListAdapter adapter = new ColorsListAdapter(getActivity(), list);
        adapter.setCheckItem(ThemeUtils.getCurrentTheme(activity).getIntValue());
        GridView gridView = (GridView) LayoutInflater.from(activity).inflate(R.layout.colors_panel_layout, null);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setCacheColorHint(0);
        gridView.setAdapter(adapter);
        builder.setView(gridView);
        final AlertDialog dialog = builder.show();
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            dialog.dismiss();
            settingPresenter.onThemeChoose(position);
        });

    }

    @Override
    public boolean isResume() {
        return false;
    }

    @Override
    public void reload() {
        if (activity != null){
            activity.reload(false);
        }
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
}
