package com.yusen.logistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.citzx.cslibrary.net.NetBean;
import com.citzx.cslibrary.net.RequestCallBack;
import com.citzx.cslibrary.net.XutilHttpHelp;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.view.AbsBaseAdapter;
import com.google.gson.reflect.TypeToken;
import com.yusen.logistics.R;
import com.yusen.logistics.base.APIConfig;
import com.yusen.logistics.base.BaseActivity;
import com.yusen.logistics.bean.YunDanListBean;

import org.xutils.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YunDanListActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_righttext)
    TextView tvRighttext;
    AbsBaseAdapter adapter;
    @Bind(R.id.lv_dingdanliebiao)
    ListView lvDingdanliebiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yun_dan_list);
        ButterKnife.bind(this);
        tvTitle.setText("订单列表");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.finish();
            }
        });
        getDingDanList();
    }

    @OnClick({R.id.iv_back, R.id.tv_righttext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_righttext:
                break;
        }
    }

    List<YunDanListBean> list = new ArrayList<>();

    /**
     * 获取运单列表
     */
    private void getDingDanList() {
        RequestParams params = new RequestParams(APIConfig.ShangPin.getShangPin);
        params.addBodyParameter("DataType", "Waybill_List");
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, YunDanListBean> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, YunDanListBean>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getDatas() != null) {
                        list = responseT.getDatas();
                        setData();
                    }
                }
            }
        });
    }

    private void setData() {
        adapter = new AbsBaseAdapter<YunDanListBean>(this, R.layout.item_dingdanlist) {
            @Override
            protected void bindDatas(ViewHolder viewHolder, YunDanListBean data, int position) {
                viewHolder.bindTextView(R.id.tv_danhao, "单号："+data.getW_OrderNo());
                viewHolder.bindTextView(R.id.tv_shouhuoren, "收货人:"+data.getW_S_Name());
                viewHolder.bindTextView(R.id.tv_shouhuodizhi, "收货地址："+data.getW_S_Province()+data.getW_S_City()+data.getW_S_District()+data.getW_S_Address());
                SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy/MM/dd");//定义
                try {
                    Date d1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(data.getW_DeliveryDate() + "");//定义起始日期
                    viewHolder.bindTextView(R.id.tv_fahuoshijian, sdf0.format(d1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };
        adapter.setDatas(list);
        lvDingdanliebiao.setAdapter(adapter);
        lvDingdanliebiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(me,DingDanActivity.class);
                intent.putExtra("WID",list.get(position).getW_ID());
                startActivity(intent);
            }
        });
    }
}
