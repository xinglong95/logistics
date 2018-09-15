package com.citzx.cslibrary.utils.addressselect.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.citzx.cslibrary.R;
import com.citzx.cslibrary.core.LibraryApplication;
import com.citzx.cslibrary.net.NetBean;
import com.citzx.cslibrary.net.RequestCallBack;
import com.citzx.cslibrary.net.XutilHttpHelp;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.utils.Lists;
import com.citzx.cslibrary.utils.MTextUtils;
import com.citzx.cslibrary.utils.addressselect.bean.City;
import com.citzx.cslibrary.utils.addressselect.bean.County;
import com.citzx.cslibrary.utils.addressselect.bean.Province;
import com.citzx.cslibrary.utils.addressselect.bean.Street;
import com.code19.library.L;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by smartTop on 2016/10/19.
 */

public class AddressSelector implements AdapterView.OnItemClickListener {

    private static final String AddRESS_URL = "http://120.27.125.113:8081/api/Waybill.ashx";
    private static final int INDEX_TAB_PROVINCE = 0;//省份标志
    private static final int INDEX_TAB_CITY = 1;//城市标志
    private static final int INDEX_TAB_COUNTY = 2;//乡镇标志
    private static final int INDEX_TAB_STREET = 3;//街道标志
    private int tabIndex = INDEX_TAB_PROVINCE; //默认是省份

    private static final int INDEX_INVALID = -1;
    private int provinceIndex = INDEX_INVALID; //省份的下标
    private int cityIndex = INDEX_INVALID;//城市的下标
    private int countyIndex = INDEX_INVALID;//乡镇的下标
    private int streetIndex = INDEX_INVALID;//街道的下标

    private Context context;
    private final LayoutInflater inflater;
    private View view;

    private View indicator;

    private LinearLayout layout_tab;
    private TextView textViewProvince;
    private TextView textViewCity;
    private TextView textViewCounty;
    private TextView textViewStreet;

    private ProgressBar progressBar;

    private ListView listView;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CountyAdapter countyAdapter;
    private StreetAdapter streetAdapter;
    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;
    private List<Street> streets;
    private OnAddressSelectedListener listener;
    private OnDialogCloseListener dialogCloseListener;
    private onSelectorAreaPositionListener selectorAreaPositionListener;

    private static final int WHAT_PROVINCES_PROVIDED = 0;
    private static final int WHAT_CITIES_PROVIDED = 1;
    private static final int WHAT_COUNTIES_PROVIDED = 2;
    private static final int WHAT_STREETS_PROVIDED = 3;
    //    private AddressDictManager addressDictManager;
    private ImageView iv_colse;
    private int selectedColor;
    private int unSelectedColor;
    public int provincePostion;
    public int cityPosition;
    public int countyPosition;
    public int streetPosition;
    @SuppressWarnings("unchecked")
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PROVINCES_PROVIDED: //更新省份列表
                    provinces = (List<Province>) msg.obj;
                    provinceAdapter.notifyDataSetChanged();
                    listView.setAdapter(provinceAdapter);

                    break;

                case WHAT_CITIES_PROVIDED: //更新城市列表
                    cities = (List<City>) msg.obj;
                    cityAdapter.notifyDataSetChanged();
                    if (Lists.notEmpty(cities)) {
                        // 以次级内容更新列表
                        listView.setAdapter(cityAdapter);
                        // 更新索引为次级
                        tabIndex = INDEX_TAB_CITY;
                    } else {
                        // 次级无内容，回调
                        callbackInternal();
                    }

                    break;

                case WHAT_COUNTIES_PROVIDED://更新乡镇列表
                    counties = (List<County>) msg.obj;
                    countyAdapter.notifyDataSetChanged();
                    if (Lists.notEmpty(counties)) {
                        listView.setAdapter(countyAdapter);
                        tabIndex = INDEX_TAB_COUNTY;
                    } else {
                        callbackInternal();
                    }

                    break;

                case WHAT_STREETS_PROVIDED://更新街道列表
                    streets = (List<Street>) msg.obj;
                    streetAdapter.notifyDataSetChanged();
                    if (Lists.notEmpty(streets)) {
                        listView.setAdapter(streetAdapter);
                        tabIndex = INDEX_TAB_STREET;
                    } else {
                        callbackInternal();
                    }

                    break;
            }

            updateTabsVisibility();
            updateProgressVisibility();
            updateIndicator();

            return true;
        }
    });


    public AddressSelector(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
//        addressDictManager = new AddressDictManager(context);
        initViews();
        initAdapters();
        retrieveProvinces();
    }

//    /**
//     * 得到数据库管理者
//     * @return
//     */
//    public AddressDictManager getAddressDictManager(){
//        return addressDictManager;
//    }

    /**
     * 初始化布局
     */
    private void initViews() {
        view = inflater.inflate(R.layout.address_selector, null);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);//进度条
        this.iv_colse = (ImageView) view.findViewById(R.id.iv_colse);
        this.listView = (ListView) view.findViewById(R.id.listView);//listview
        this.indicator = view.findViewById(R.id.indicator); //指示器
        this.layout_tab = (LinearLayout) view.findViewById(R.id.layout_tab);
        this.textViewProvince = (TextView) view.findViewById(R.id.textViewProvince);//省份
        this.textViewCity = (TextView) view.findViewById(R.id.textViewCity);//城市
        this.textViewCounty = (TextView) view.findViewById(R.id.textViewCounty);//区 乡镇
        this.textViewStreet = (TextView) view.findViewById(R.id.textViewStreet);//街道

        this.textViewProvince.setOnClickListener(new OnProvinceTabClickListener());
        this.textViewCity.setOnClickListener(new OnCityTabClickListener());
        this.textViewCounty.setOnClickListener(new onCountyTabClickListener());
        this.textViewStreet.setOnClickListener(new OnStreetTabClickListener());

        this.listView.setOnItemClickListener(this);
        this.iv_colse.setOnClickListener(new onCloseClickListener());
        updateIndicator();
    }

    /**
     * 设置字体选中的颜色
     */
    public void setTextSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    /**
     * 设置字体没有选中的颜色
     */
    public void setTextUnSelectedColor(int unSelectedColor) {
        this.unSelectedColor = unSelectedColor;
    }

    /**
     * 设置字体的大小
     */
    public void setTextSize(float dp) {
        textViewProvince.setTextSize(dp);
        textViewCity.setTextSize(dp);
        textViewCounty.setTextSize(dp);
        textViewStreet.setTextSize(dp);
    }

    /**
     * 设置字体的背景
     */
    public void setBackgroundColor(int colorId) {
        layout_tab.setBackgroundColor(context.getResources().getColor(colorId));
    }

    /**
     * 设置指示器的背景
     */
    public void setIndicatorBackgroundColor(int colorId) {
        indicator.setBackgroundColor(context.getResources().getColor(colorId));
    }

    /**
     * 设置指示器的背景
     */
    public void setIndicatorBackgroundColor(String color) {
        indicator.setBackgroundColor(Color.parseColor(color));
    }

    /**
     * 初始化adapter
     */
    private void initAdapters() {
        provinceAdapter = new ProvinceAdapter();
        cityAdapter = new CityAdapter();
        countyAdapter = new CountyAdapter();
        streetAdapter = new StreetAdapter();
    }

    /**
     * 更新tab 指示器
     */
    private void updateIndicator() {
        view.post(new Runnable() {
            @Override
            public void run() {
                switch (tabIndex) {
                    case INDEX_TAB_PROVINCE: //省份
                        buildIndicatorAnimatorTowards(textViewProvince).start();
                        break;
                    case INDEX_TAB_CITY: //城市
                        buildIndicatorAnimatorTowards(textViewCity).start();
                        break;
                    case INDEX_TAB_COUNTY: //乡镇
                        buildIndicatorAnimatorTowards(textViewCounty).start();
                        break;
                    case INDEX_TAB_STREET: //街道
                        buildIndicatorAnimatorTowards(textViewStreet).start();
                        break;
                }
            }
        });
    }

    /**
     * tab 来回切换的动画
     *
     * @param tab
     * @return
     */
    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(indicator, "X", indicator.getX(), tab.getX());

        final ViewGroup.LayoutParams params = indicator.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (int) animation.getAnimatedValue();
                indicator.setLayoutParams(params);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);

        return set;
    }

    /**
     * 更新tab显示
     */
    private void updateTabsVisibility() {
        textViewProvince.setVisibility(Lists.notEmpty(provinces) ? View.VISIBLE : View.GONE);
        textViewCity.setVisibility(Lists.notEmpty(cities) ? View.VISIBLE : View.GONE);
        textViewCounty.setVisibility(Lists.notEmpty(counties) ? View.VISIBLE : View.GONE);
        textViewStreet.setVisibility(Lists.notEmpty(streets) ? View.VISIBLE : View.GONE);
        //按钮能不能点击 false 不能点击 true 能点击
        textViewProvince.setEnabled(tabIndex != INDEX_TAB_PROVINCE);
        textViewCity.setEnabled(tabIndex != INDEX_TAB_CITY);
        textViewCounty.setEnabled(tabIndex != INDEX_TAB_COUNTY);
        textViewStreet.setEnabled(tabIndex != INDEX_TAB_STREET);
        if (selectedColor != 0 && unSelectedColor != 0) {
            updateTabTextColor();
        }
    }

    /**
     * 更新字体的颜色
     */
    private void updateTabTextColor() {
        if (tabIndex != INDEX_TAB_PROVINCE) {
            textViewProvince.setTextColor(context.getResources().getColor(selectedColor));
        } else {
            textViewProvince.setTextColor(context.getResources().getColor(unSelectedColor));
        }
        if (tabIndex != INDEX_TAB_CITY) {
            textViewCity.setTextColor(context.getResources().getColor(selectedColor));
        } else {
            textViewCity.setTextColor(context.getResources().getColor(unSelectedColor));
        }
        if (tabIndex != INDEX_TAB_COUNTY) {
            textViewCounty.setTextColor(context.getResources().getColor(selectedColor));
        } else {
            textViewCounty.setTextColor(context.getResources().getColor(unSelectedColor));
        }
        if (tabIndex != INDEX_TAB_STREET) {
            textViewStreet.setTextColor(context.getResources().getColor(selectedColor));
        } else {
            textViewStreet.setTextColor(context.getResources().getColor(unSelectedColor));
        }

    }

    /**
     * 点击省份的监听
     */
    class OnProvinceTabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_PROVINCE;
            listView.setAdapter(provinceAdapter);

            if (provinceIndex != INDEX_INVALID) {
                listView.setSelection(provinceIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    /**
     * 点击城市的监听
     */
    class OnCityTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_CITY;
            listView.setAdapter(cityAdapter);

            if (cityIndex != INDEX_INVALID) {
                listView.setSelection(cityIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    /**
     * 点击区 乡镇的监听
     */
    class onCountyTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_COUNTY;
            listView.setAdapter(countyAdapter);

            if (countyIndex != INDEX_INVALID) {
                listView.setSelection(countyIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    /**
     * 点击街道的监听
     */
    class OnStreetTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_STREET;
            listView.setAdapter(streetAdapter);

            if (streetIndex != INDEX_INVALID) {
                listView.setSelection(streetIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    /**
     * 点击右边关闭dialog监听
     */
    class onCloseClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (dialogCloseListener != null) {
                dialogCloseListener.dialogclose();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE: //省份
                Province province = provinceAdapter.getItem(position);
                provincePostion = position;
                // 更新当前级别及子级标签文本
                textViewProvince.setText(province.C_Province);
                textViewCity.setText("请选择");
                textViewCounty.setText("请选择");
                textViewStreet.setText("请选择");
                //根据省份的id,从数据库中查询城市列表
                retrieveCitiesWith(province.C_Province);

                // 清空子级数据
                cities = null;
                counties = null;
                streets = null;
                cityAdapter.notifyDataSetChanged();
                countyAdapter.notifyDataSetChanged();
                streetAdapter.notifyDataSetChanged();
                // 更新已选中项
                this.provinceIndex = position;
                this.cityIndex = INDEX_INVALID;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;
                // 更新选中效果
                provinceAdapter.notifyDataSetChanged();
                break;
            case INDEX_TAB_CITY://城市
                City city = cityAdapter.getItem(position);
                cityPosition = position;
                textViewCity.setText(city.C_City);
                textViewCounty.setText("请选择");
                textViewStreet.setText("请选择");
                //根据城市的id,从数据库中查询城市列表
                retrieveCountiesWith(textViewProvince.getText().toString(), city.C_City);
                // 清空子级数据
                counties = null;
                streets = null;
                countyAdapter.notifyDataSetChanged();
                streetAdapter.notifyDataSetChanged();
                // 更新已选中项
                this.cityIndex = position;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;
                // 更新选中效果
                cityAdapter.notifyDataSetChanged();
                break;
            case INDEX_TAB_COUNTY:
                County county = countyAdapter.getItem(position);
                countyPosition = position;
                textViewCounty.setText(county.C_District);
                textViewStreet.setText("请选择");
                retrieveStreetsWith(county.C_District);

                streets = null;
                streetAdapter.notifyDataSetChanged();

                this.countyIndex = position;
                this.streetIndex = INDEX_INVALID;

                countyAdapter.notifyDataSetChanged();
                break;
            case INDEX_TAB_STREET:
                Street street = streetAdapter.getItem(position);
                streetPosition = position;
                textViewStreet.setText(street.C_Street);

                this.streetIndex = position;

                streetAdapter.notifyDataSetChanged();

                callbackInternal();
                if (selectorAreaPositionListener != null) {
                    selectorAreaPositionListener.selectorAreaPosition(provincePostion, cityPosition, countyPosition, streetPosition);
                }

                break;
        }
    }


    /**
     * 查询省份列表
     */
    private void retrieveProvinces() {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(AddRESS_URL);
        if (LibraryApplication.getAddressType().equals("China")) {
            params.addBodyParameter("DataType", "Province");
        } else if (LibraryApplication.getAddressType().equals("Japan")) {
            params.addBodyParameter("DataType", "Japan_Province");
        }
        XutilHttpHelp.getInstance().BaseInfoHttp(params, LibraryApplication.getContext(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, Province> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, Province>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getDatas() != null) {
                        List<Province> provinceList = responseT.getDatas();
                        handler.sendMessage(Message.obtain(handler, WHAT_PROVINCES_PROVIDED, provinceList));
                    }
                }
            }
        });

//        List<Province> provinceList = addressDictManager.getProvinceList();
//        handler.sendMessage(Message.obtain(handler, WHAT_PROVINCES_PROVIDED, provinceList));

    }

    /**
     * 根据省份id查询城市列表
     *
     * @param provincename 省份id
     */
    private void retrieveCitiesWith(String provincename) {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(AddRESS_URL);
        if (LibraryApplication.getAddressType().equals("China")) {
            params.addBodyParameter("DataType", "City");
        } else if (LibraryApplication.getAddressType().equals("Japan")) {
            params.addBodyParameter("DataType", "Japan_City");
        }
        params.addBodyParameter("province", provincename);
        XutilHttpHelp.getInstance().BaseInfoHttp(params, LibraryApplication.getContext(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, City> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, City>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getDatas() != null&&responseT.getDatas().size()!=0&& MTextUtils.notEmpty(responseT.getDatas().get(0).C_City)) {
                        List<City> cityList = responseT.getDatas();
                        handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, cityList));
                    }else{
                        List<City> cityList = new ArrayList<>();
                        handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, cityList));
                    }
                }
            }
        });
//        List<City> cityList = addressDictManager.getCityList(provinceId);
//        handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, cityList));
    }

    /**
     * 根据城市id查询乡镇列表
     *
     * @param cityName 城市id
     */
    private void retrieveCountiesWith(String provinceName, String cityName) {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(AddRESS_URL);
        if (LibraryApplication.getAddressType().equals("China")) {
            params.addBodyParameter("DataType", "District");
        } else if (LibraryApplication.getAddressType().equals("Japan")) {
            params.addBodyParameter("DataType", "Japan_District");
        }
        params.addBodyParameter("province", provinceName);
        params.addBodyParameter("city", cityName);
        XutilHttpHelp.getInstance().BaseInfoHttp(params, LibraryApplication.getContext(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, County> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, County>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getDatas() != null&&responseT.getDatas().size()!=0&&MTextUtils.notEmpty(responseT.getDatas().get(0).C_District)) {
                        List<County> countyList = responseT.getDatas();
                        handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, countyList));
                    }else{
                        List<County> countyList = new ArrayList<>();
                        handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, countyList));
                    }
                }
            }
        });

//        List<County> countyList = addressDictManager.getCountyList(cityId);
//        handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, countyList));
    }

    /**
     * 根据乡镇id查询乡镇列表
     *
     * @param countyName 乡镇id
     */
    private void retrieveStreetsWith(String countyName) {
        progressBar.setVisibility(View.VISIBLE);
//        List<Street> streetList = addressDictManager.getStreetList(countyId);//此处注释掉街道选择，以后需要在放开
        List<Street> streetList = new ArrayList<>();
        handler.sendMessage(Message.obtain(handler, WHAT_STREETS_PROVIDED, streetList));
    }

    /**
     * 省份 城市 乡镇 街道 都选中完 后的回调
     */
    private void callbackInternal() {
        if (listener != null) {
            Province province = provinces == null || provinceIndex == INDEX_INVALID ? null : provinces.get(provinceIndex);
            City city = cities == null || cityIndex == INDEX_INVALID ? null : cities.get(cityIndex);
            County county = counties == null || countyIndex == INDEX_INVALID ? null : counties.get(countyIndex);
            Street street = streets == null || streetIndex == INDEX_INVALID ? null : streets.get(streetIndex);

            listener.onAddressSelected(province, city, county, street);
        }
    }

    /**
     * 更新进度条
     */
    private void updateProgressVisibility() {
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();
        progressBar.setVisibility(itemCount > 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * 获得view
     *
     * @return
     */
    public View getView() {
        return view;
    }

    /**
     * 省份的adapter
     */
    class ProvinceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return provinces == null ? 0 : provinces.size();
        }

        @Override
        public Province getItem(int position) {
            return provinces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(getItem(position).C_ID);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Province item = getItem(position);
            holder.textView.setText(item.C_Province);

            boolean checked = provinceIndex != INDEX_INVALID && provinces.get(provinceIndex).C_ID.equals(item.C_ID);
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    /**
     * 城市的adaoter
     */
    class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cities == null ? 0 : cities.size();
        }

        @Override
        public City getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(getItem(position).C_ID);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            City item = getItem(position);
            holder.textView.setText(item.C_City);

            boolean checked = cityIndex != INDEX_INVALID && cities.get(cityIndex).C_ID.equals(item.C_ID);
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    /**
     * 乡镇的adapter
     */
    class CountyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return counties == null ? 0 : counties.size();
        }

        @Override
        public County getItem(int position) {
            return counties.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(getItem(position).C_ID);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            County item = getItem(position);
            holder.textView.setText(item.C_District);

            boolean checked = countyIndex != INDEX_INVALID && counties.get(countyIndex).C_ID.equals(item.C_ID);
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    /**
     * 街道的adapter
     */
    class StreetAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return streets == null ? 0 : streets.size();
        }

        @Override
        public Street getItem(int position) {
            return streets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(getItem(position).C_ID);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Street item = getItem(position);
            holder.textView.setText(item.C_Street);

            boolean checked = streetIndex != INDEX_INVALID && streets.get(streetIndex).C_ID.equals(item.C_ID);
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }


    public OnAddressSelectedListener getOnAddressSelectedListener() {
        return listener;
    }

    /**
     * 设置地址监听
     *
     * @param listener
     */
    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnDialogCloseListener {
        void dialogclose();
    }

    /**
     * 设置close监听
     */
    public void setOnDialogCloseListener(OnDialogCloseListener listener) {
        this.dialogCloseListener = listener;
    }

    public interface onSelectorAreaPositionListener {
        void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition);
    }

    public void setOnSelectorAreaPositionListener(onSelectorAreaPositionListener listener) {
        this.selectorAreaPositionListener = listener;
    }

//    /**
//     * 根据code 来显示选择过的地区
//     */
//    public void getSelectedArea(String provinceCode, int provincePostion, String cityCode, int cityPosition, String countyCode, int countyPosition, String streetCode, int streetPosition){
//        L.d("数据", "getSelectedArea省份id=" + provinceCode);
//        L.d("数据", "getSelectedArea城市id=" + cityCode);
//        L.d("数据", "getSelectedArea乡镇id=" + countyCode);
//        L.d("数据", "getSelectedArea 街道id=" + streetCode);
//        if(!TextUtils.isEmpty(provinceCode)){
//            Province province = addressDictManager.getProvinceBean(provinceCode);
//            textViewProvince.setText(province.name);
//            L.d("数据", "省份=" + province);
//            // 更新当前级别及子级标签文本
//            //根据省份的id,从数据库中查询城市列表
//            retrieveCitiesWith(province.id);
//
//            // 清空子级数据
//            cities = null;
//            counties = null;
//            streets = null;
//            cityAdapter.notifyDataSetChanged();
//            countyAdapter.notifyDataSetChanged();
//            streetAdapter.notifyDataSetChanged();
//            // 更新已选中项
//            this.provinceIndex = provincePostion;
//            this.cityIndex = INDEX_INVALID;
//            this.countyIndex = INDEX_INVALID;
//            this.streetIndex = INDEX_INVALID;
//            // 更新选中效果
//            provinceAdapter.notifyDataSetChanged();
//        }
//        if(!TextUtils.isEmpty(cityCode)){
//            City city = addressDictManager.getCityBean(cityCode);
//            textViewCity.setText(city.name);
//            L.d("数据", "城市=" + city.name);
//            //根据城市的id,从数据库中查询城市列表
//            retrieveCountiesWith(city.id);
//            // 清空子级数据
//            counties = null;
//            streets = null;
//            countyAdapter.notifyDataSetChanged();
//            streetAdapter.notifyDataSetChanged();
//            // 更新已选中项
//            this.cityIndex = cityPosition;
//            this.countyIndex = INDEX_INVALID;
//            this.streetIndex = INDEX_INVALID;
//            // 更新选中效果
//            cityAdapter.notifyDataSetChanged();
//
//        }
//        if(!TextUtils.isEmpty(countyCode)){
//            County county = addressDictManager.getCountyBean(countyCode);
//            textViewCounty.setText(county.name);
//            L.d("数据", "乡镇=" + county.name);
//            retrieveStreetsWith(county.id);
//
//            streets = null;
//            streetAdapter.notifyDataSetChanged();
//
//            this.countyIndex = countyPosition;
//            this.streetIndex = INDEX_INVALID;
//
//            countyAdapter.notifyDataSetChanged();
//        }
//        if(!TextUtils.isEmpty(streetCode)){
//            Street street = addressDictManager.getStreetBean(streetCode);
//            textViewStreet.setText(street.name);
//            L.d("数据", "街道=" + street.name);
//
//            this.streetIndex = streetPosition;
//
//            streetAdapter.notifyDataSetChanged();
//
//
//
//        }
//        callbackInternal();
//    }

}
