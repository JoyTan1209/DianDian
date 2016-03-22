package com.tanchaoyin.diandian.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tanchaoyin.diandian.R;

import java.util.List;

/**
 * Created by TanChaoyin on 2016/3/22.
 */
public class ColorsListAdapter extends BaseListAdapter<Integer> {

    private int checkItem;

    public ColorsListAdapter(Context context, List<Integer> list) {
        super(context, list);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.colors_image_layout, null);
            holder = new Holder();
            holder.imageView1 = (ImageView)convertView.findViewById(R.id.img_1);
            holder.imageView2 = (ImageView)convertView.findViewById(R.id.img_2);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }
        holder.imageView1.setImageResource(list.get(position));
        if (checkItem == position){
            holder.imageView2.setImageResource(R.mipmap.ic_done_white);
        }
        return convertView;
    }

    public int getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(int checkItem) {
        this.checkItem = checkItem;
    }

    static class Holder {
        ImageView imageView1;
        ImageView imageView2;
    }
}
