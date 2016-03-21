package com.tanchaoyin.diandian.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.socks.library.KLog;
import com.tanchaoyin.diandian.R;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class GlideUtils {

    private static final String TAG = "GlideUtils";

        /**
         * glide加载图片
         *
         * @param view view
         * @param url  url
         */
        public static void display(ImageView view, String url) {
            displayUrl(view, url, R.mipmap.img_default_gray);
        }


        /**
         * glide加载图片
         *
         * @param view         view
         * @param url          url
         * @param defaultImage defaultImage
         */
        private static void displayUrl(final ImageView view, String url, @DrawableRes int defaultImage) {
            // 不能崩
            if (view == null) {
                KLog.e("GlideUtils -> display -> imageView is null");
                return;
            }
            Context context = view.getContext();
            // View你还活着吗？
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }

            try {
                Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(defaultImage)
                        .crossFade()
                        .centerCrop()
                        .into(view)
                        .getSize((width, height) -> {
                            if (!view.isShown()) {
                                view.setVisibility(View.VISIBLE);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void displayNative(final ImageView view, @DrawableRes int resId) {
            // 不能崩
            if (view == null) {
                KLog.e("GlideUtils -> display -> imageView is null");
                return;
            }
            Context context = view.getContext();
            // View你还活着吗？
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }

            try {
                Glide.with(context)
                        .load(resId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .centerCrop()
                        .into(view)
                        .getSize((width, height) -> {
                            if (!view.isShown()) {
                                view.setVisibility(View.VISIBLE);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public static void displayCircleHeader(ImageView view, @DrawableRes int res) {
            // 不能崩
            if (view == null) {
                KLog.e("GlideUtils -> display -> imageView is null");
                return;
            }
            Context context = view.getContext();
            // View你还活着吗？
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }

            try {
                Glide.with(context)
                        .load(res)
                        .centerCrop()
                        .placeholder(R.mipmap.img_default_gray)
                        .bitmapTransform(new GlideCircleTransform(context))
                        .crossFade()
                        .into(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
