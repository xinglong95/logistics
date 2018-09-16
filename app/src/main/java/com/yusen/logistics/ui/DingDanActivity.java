package com.yusen.logistics.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.barcodescandemo.ScannerInerface;
import com.citzx.cslibrary.net.NetBean;
import com.citzx.cslibrary.net.RequestCallBack;
import com.citzx.cslibrary.net.XutilHttpHelp;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.utils.MTextUtils;
import com.citzx.cslibrary.utils.ToastUtil;
import com.citzx.cslibrary.view.AbsBaseAdapter;
import com.google.gson.reflect.TypeToken;
import com.yusen.logistics.R;
import com.yusen.logistics.base.APIConfig;
import com.yusen.logistics.base.BaseActivity;
import com.yusen.logistics.base.ConstantConfig;
import com.yusen.logistics.bean.ShangPinInfoBean;
import com.yusen.logistics.bean.ShangPinItem;
import com.yusen.logistics.bean.ShangPinJiShuBean;
import com.yusen.logistics.bean.SubmitDingDanBean;
import com.yusen.logistics.utils.FileService;

import org.xutils.http.RequestParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DingDanActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_righttext)
    TextView tvRighttext;
    @Bind(R.id.lv_dingdanliebiao)
    ListView lvDingdanliebiao;
    @Bind(R.id.tv_gongji)
    TextView tvGongji;
    @Bind(R.id.btn_tijiaodingdan)
    Button btnTijiaodingdan;
    @Bind(R.id.tv_yundandanhao)
    TextView tvYundandanhao;
    @Bind(R.id.ll_yundandanhao)
    LinearLayout llYundandanhao;
    @Bind(R.id.et_shijizhongliang)
    EditText etShijizhongliang;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;
    ScannerInerface Controll = new ScannerInerface(this);
    //    List<ShangPinInfoBean> list = new ArrayList<>();
    List<ShangPinJiShuBean> list_num = new ArrayList<>();
    AbsBaseAdapter adapter;
    final FileService fs = new FileService(me);
    SubmitDingDanBean submitDingDanBean;
    String wid;
    //6923410717242
    //6920312611029
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ding_dan);
        ButterKnife.bind(this);
        Controll.open();
        Controll.enablePlayBeep(true);
        Controll.setOutputMode(1);//使用广播模式0为模拟输出  1为广播模式发送
        Controll.unlockScanKey();
        submitDingDanBean = new SubmitDingDanBean();
        mFilter = new IntentFilter("android.intent.action.SCANRESULT");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 此处获取扫描结果信息
                final String scanResult = intent.getStringExtra("value");
                int barocodelen = intent.getIntExtra("length", 0);
                int type = intent.getIntExtra("type", 0);
                getShangPinInfo(scanResult);
            }
        };
        adapter = new AbsBaseAdapter<ShangPinJiShuBean>(this, R.layout.adapter_list) {
            @Override
            protected void bindDatas(ViewHolder viewHolder, final ShangPinJiShuBean data, final int position) {
                viewHolder.bindTextView(R.id.tv_name, position + 1 + "、" + data.getBean().getP_Name());
                viewHolder.bindTextView(R.id.tv_zhongliang, data.getBean().getP_Weight() + "g");
                viewHolder.bindTextView(R.id.tv_jiage, data.getBean().getP_Price());
                viewHolder.bindTextView(R.id.tv_shuliang, data.getCount() + "");
                ImageView iv_shanchu= (ImageView) viewHolder.getView(R.id.iv_shanchu);
                iv_shanchu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.getCount()>1){
                            int count=list_num.get(position).getCount()-1;
                            list_num.get(position).setCount(count);
                        }else{
                            list_num.remove(position);
                        }
                        adapter.setDatas(list_num);
                    }
                });
            }
        };
        adapter.setDatas(list_num);
        lvDingdanliebiao.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                me.finish();
            }
        });
        wid=getIntent().getStringExtra("WID");
        if (MTextUtils.notEmpty(wid)){
            tvTitle.setText("编辑订单");
            getShangPinList(wid);
            getYundanInfo(wid);
            llYundandanhao.setVisibility(View.VISIBLE);
        }else{
            tvTitle.setText("创建订单");
            ReadInfo();
            llYundandanhao.setVisibility(View.GONE);
        }
    }
    /**
     * 获取商品信息
     */
    private void getYundanInfo(final String wid) {
//        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.getShangPin);
        params.addBodyParameter("DataType", "Waybill_Main_Detail");
        params.addBodyParameter("wid", wid);
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<SubmitDingDanBean, ?> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<SubmitDingDanBean, ?>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getData()!=null){
                        tvYundandanhao.setText(responseT.getData().getW_OrderNo());
                        etShijizhongliang.setText(responseT.getData().getW_Actual_Weight());
                        submitDingDanBean=responseT.getData();
                    }
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                }
            }
        });
    }
    /**
     * 获取商品信息列表
     */
    private void getShangPinList(final String wid) {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.getShangPin);
        params.addBodyParameter("DataType", "Waybill_Sub_Detail");
        params.addBodyParameter("wid", wid);
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, ShangPinInfoBean> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, ShangPinInfoBean>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getDatas() != null) {
                        for (int i = 0; i < responseT.getDatas().size(); i++) {
                            ShangPinJiShuBean bean=new ShangPinJiShuBean();
                            double count=Double.parseDouble(responseT.getDatas().get(i).getWS_FirstCount());
                            bean.setCount((int)count);
                            bean.setBean(responseT.getDatas().get(i));
                            list_num.add(bean);
                        }
//                         list_num=AssembleData(responseT.getDatas());
                         adapter.setDatas(list_num);
                         getAllWei_Pric();
                    }
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                }
            }
        });
    }
    /**
     * 获取商品信息
     */
    private void getShangPinInfo(final String barcode) {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.AddShangPin);
        params.addBodyParameter("DataType", "Product_Scan");
        params.addBodyParameter("barcode", barcode);
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<ShangPinInfoBean, ?> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<ShangPinInfoBean, ?>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getData() != null) {
                        boolean isNew = true;
                        if (list_num != null && list_num.size() != 0) {
                            for (int i = 0; i < list_num.size(); i++) {
                                if (responseT.getData().getP_ID().equals(list_num.get(i).getBean().getP_ID())) {
                                    int count = list_num.get(i).getCount() + 1;
                                    list_num.get(i).setCount(count);
                                    isNew = false;
                                    break;
                                }
                            }
                        }
                        if (isNew) {
                            ShangPinJiShuBean bean = new ShangPinJiShuBean();
                            bean.setCount(1);
                            bean.setBean(responseT.getData());
                            list_num.add(bean);
                        }
//                        list.add(responseT.getData());
//                        list_num=AssembleData(list);
                        adapter.setDatas(list_num);
                        getAllWei_Pric();
                    }
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                }
            }
        });
    }

    private List<ShangPinJiShuBean> AssembleData(List<ShangPinInfoBean> data) {
        //用来记录运算后的数据
        Map<String, ShangPinJiShuBean> resultMap = new LinkedHashMap<>();
        /**
         * 运算到的位置记录
         */
        int countIndex = 0;

        while (countIndex < data.size()) {
            String s = data.get(countIndex).getP_ID();
            //如果这个值运算过 不再运算
            if (resultMap.get(s) != null) {
                countIndex++;
                continue;
            }
            ShangPinJiShuBean bean = new ShangPinJiShuBean();
            bean.setBean(data.get(countIndex));
            for (int i = countIndex; i < data.size(); i++) {
                if (data.get(i).getP_ID().equals(s)) {
                    int count = bean.getCount();
                    count++;
                    bean.setCount(count);
                }
            }
            resultMap.put(s, bean);
            countIndex++;
        }
        return new ArrayList<ShangPinJiShuBean>(resultMap.values());

    }

    double allpic = 0;
    double allwei = 0;

    /**
     * 计算总共的重量和价钱
     */
    private void getAllWei_Pric() {
        allpic = 0;
        allwei = 0;
        for (int i = 0; i < list_num.size(); i++) {
            allpic = allpic + Double.parseDouble(list_num.get(i).getBean().getP_Price()) * list_num.get(i).getCount();
            allwei = allwei + Double.parseDouble(list_num.get(i).getBean().getP_Weight()) * list_num.get(i).getCount();
        }
        tvGongji.setText("共计" + allwei + "g，" + allpic + "元");
    }

    /**
     * 指定139的物理按键（中间黄色按键）按下来出发扫描
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 141 || keyCode == 140 || keyCode == 139) {
            Controll.scan_start();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {   //按键弹起，停止扫描
        if (keyCode == 141 || keyCode == 140 || keyCode == 139) {
            Controll.scan_stop();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        // 注销获取扫描结果的广播+
        this.unregisterReceiver(mReceiver);
        if (MTextUtils.isEmpty(wid)){
            SaveInfo();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        Controll.close();
        super.onDestroy();
    }

    @OnClick(R.id.btn_tijiaodingdan)
    public void onViewClicked() {
//        list_num=new ArrayList<>();
        if (list_num.size()==0){
            ToastUtil.showShort("请扫描商品");
        }else if(MTextUtils.isEmpty(etShijizhongliang.getText().toString())||Double.parseDouble(etShijizhongliang.getText().toString())>40000){
            ToastUtil.showShort("请填写实际重量且不能超过40kg");
        }else{
            List<ShangPinInfoBean> items = new ArrayList<>();
            for (int i = 0; i < list_num.size(); i++) {
                ShangPinInfoBean item = new ShangPinInfoBean();
                item=list_num.get(i).getBean();
                double count = (double) list_num.get(i).getCount();
                double price = Double.parseDouble(list_num.get(i).getBean().getP_Price());
                item.setWS_FirstCount(list_num.get(i).getCount()+"");
                item.setWS_Money((count * price) + "");
                items.add(item);
            }
            submitDingDanBean.setSub(items);
            Intent intent = new Intent(me, AddIDCardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("DingDan", submitDingDanBean);
            intent.putExtras(bundle);
            intent.putExtra("weight",etShijizhongliang.getText().toString());
            startActivity(intent);
        }

//        me.finish();
    }

    String dingdaninfostr;

    private void ReadInfo() {
        try {
            dingdaninfostr = fs.read(ConstantConfig.PATH_CACHE_AREA + "/logdingdan.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (MTextUtils.notEmpty(dingdaninfostr)) {
            list_num = GsonUtils.parseJsonList(dingdaninfostr, new TypeToken<List<ShangPinJiShuBean>>() {
            }.getType());
            adapter.setDatas(list_num);
            getAllWei_Pric();
        }
    }

    private void SaveInfo() {
        try {
            fs.saveToSD("logdingdan.txt", GsonUtils.toJSON(list_num));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
