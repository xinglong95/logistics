package com.citzx.cslibrary.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ken on 2016/3/8.
 * 重构的适配器
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> datas;
    private int resid;

    public AbsBaseAdapter(Context context, int resid){
        this.context = context;
        this.resid = resid;
        datas = new ArrayList<>();
    }

    public void setDatas(List<T> datas){
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<T> datas){
        this.datas.addAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(resid, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        //数据绑定
        bindDatas(viewHolder, datas.get(position), position);

        return convertView;
    }

    protected abstract void bindDatas(ViewHolder viewHolder, T data, int position);

    /**
     * ViewHolder主要用于缓存布局中的控件，避免多次findViewByID
     */
    public class ViewHolder{
        //缓存控件的集合
        Map<Integer, View> cachemap = new HashMap<>();
        //item布局对象
        View layoutView;

        public ViewHolder(View layoutView){
            this.layoutView = layoutView;
        }

        public View getView(int id){
            if(cachemap.containsKey(id)){
                return cachemap.get(id);
            } else {
                View view = layoutView.findViewById(id);
                cachemap.put(id, view);
                return view;
            }
        }

        /**
         * textview绑定数据的封装
         * @param id
         * @param
         * @return
         */
        public ViewHolder bindTextView(int id, CharSequence text){
            TextView tv = (TextView) this.getView(id);
            tv.setText(text);
            return this;
        }
        /**
         * textview换颜色
         * @param id
         * @param
         * @return
         */
        public ViewHolder bindTextViewTextColor(int id, int color){
            TextView tv = (TextView) this.getView(id);
            tv.setTextColor(color);
            return this;
        }
        /**
         * Imageview绑定数据的封装
         * @param id
         * @param
         * @return
         */
        public ViewHolder bindImageView(int id, int drawble){
            ImageView tv = (ImageView) this.getView(id);
            tv.setImageResource(drawble);
            return this;
        }
        /**
         * Imageview绑定数据的封装
         * @param id
         * @param
         * @return
         */
        public ViewHolder bindImageView4url(int id, String url){
            ImageView tv = (ImageView) this.getView(id);
            x.image().bind(tv,url);
            return this;
        }
        /**
         * Imageview绑定数据的封装
         * @param id
         * @param
         * @return
         */
        public ViewHolder bindViewVisibility(int id, int visibility){
            View v = this.getView(id);
            v.setVisibility(visibility);
            return this;
        }
    }
}
