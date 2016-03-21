package com.tanchaoyin.diandian.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class AttrsHelper {

    public static int getColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        int color = typedValue.data;

        return color;
    }
}
