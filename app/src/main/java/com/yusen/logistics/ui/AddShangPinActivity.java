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
import android.widget.TextView;

import com.android.barcodescandemo.ScannerInerface;
import com.citzx.cslibrary.net.NetBean;
import com.citzx.cslibrary.net.RequestCallBack;
import com.citzx.cslibrary.net.XutilHttpHelp;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.utils.ToastUtil;
import com.citzx.cslibrary.utils.Tools;
import com.code19.library.L;
import com.google.gson.reflect.TypeToken;
import com.yusen.logistics.R;
import com.yusen.logistics.base.APIConfig;
import com.yusen.logistics.base.BaseActivity;
import com.yusen.logistics.bean.LeiMuBean;
import com.yusen.logistics.bean.PinPaiBean;
import com.yusen.logistics.bean.ShangPinInfoBean;
import com.yusen.logistics.bean.SubmitShangPinBean;
import com.yusen.logistics.utils.SerachSelectDialog;

import org.xutils.http.RequestParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddShangPinActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_righttext)
    TextView tvRighttext;
    @Bind(R.id.tv_tiaoma)
    TextView tvTiaoma;
    @Bind(R.id.et_shangpimingcheng)
    EditText etShangpimingcheng;
    @Bind(R.id.tv_pinpai)
    TextView tvPinpai;
    @Bind(R.id.tv_leimu)
    TextView tvLeimu;
    @Bind(R.id.et_shangpinguige)
    EditText etShangpinguige;
    @Bind(R.id.et_shangpinjianjie)
    EditText etShangpinjianjie;
    @Bind(R.id.et_shangpinzhongliang)
    EditText etShangpinzhongliang;
    @Bind(R.id.et_danjia)
    EditText etDanjia;
    @Bind(R.id.et_tiji)
    EditText etTiji;
    @Bind(R.id.btn_tijiaodingdan)
    Button btnTijiaodingdan;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;
    ScannerInerface Controll = new ScannerInerface(this);
    SubmitShangPinBean bean=new SubmitShangPinBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shang_pin);
        ButterKnife.bind(this);
        tvTitle.setText("上传商品");
        getLeiMuShuJu();
        getPinPaiShuJu();
        Controll.open();
        Controll.enablePlayBeep(true);
        Controll.setOutputMode(1);//使用广播模式0为模拟输出  1为广播模式发送
        Controll.unlockScanKey();
        mFilter = new IntentFilter("android.intent.action.SCANRESULT");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 此处获取扫描结果信息
                final String scanResult = intent.getStringExtra("value");
                int barocodelen = intent.getIntExtra("length", 0);
                int type = intent.getIntExtra("type", 0);
                tvTiaoma.setText(scanResult);
                getShangPinInfo(scanResult);
            }
        };
    }

    /**
     * 获取商品信息
     */
    private void getShangPinInfo(String barcode) {
        RequestParams params = new RequestParams(APIConfig.ShangPin.getShangPin);
        params.addBodyParameter("DataType", "Product_Scan");
        params.addBodyParameter("barcode",barcode);
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<ShangPinInfoBean, ?> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<ShangPinInfoBean, ?>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getData()!=null){
                        setText(responseT.getData());
                    }
                }
            }
        });
    }
    boolean isUpdate=false;
    private void setText(ShangPinInfoBean infobean){
        isUpdate=true;
        bean.setPid(infobean.getP_ID());
        tvTiaoma.setText(infobean.getP_Number());
        etShangpimingcheng.setText(infobean.getP_Name());
        tvPinpai.setText(infobean.getP_Brand());
        bean.setBrandid(infobean.getP_BrandID());
        tvLeimu.setText(infobean.getP_Type());
        bean.setP_typeid(infobean.getP_TypeID());
        etShangpinguige.setText(infobean.getP_Spec());
        etShangpinjianjie.setText(infobean.getP_Function());
        etShangpinzhongliang.setText(infobean.getP_Weight());
        etDanjia.setText(infobean.getP_Price());
        etTiji.setText(infobean.getP_Volume());
    }
    /**
     * @return 判空
     */
    private boolean isNull(){
        getText();
        boolean isNull=true;
        ArrayList<String> strs = new ArrayList<>();
        strs = Tools.CheckNullByReflect4Keys(bean, new String[]{"Brand", "Brand","P_type", "P_typeid"});
        LinkedHashMap<String, String> linkedHashMaps = new LinkedHashMap<>();
        linkedHashMaps.put("Barcode", "请扫描商品");
        linkedHashMaps.put("Name", "请填写商品名称");
        linkedHashMaps.put("Spec", "请填写商品规格");
        linkedHashMaps.put("Funcation", "请填写商品简介功能效果");
        linkedHashMaps.put("Weight", "请填写商品重量");
        linkedHashMaps.put("Price", "请填写商品单价");
        linkedHashMaps.put("Volume", "请填写商品体积");
        Iterator iterator = linkedHashMaps.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            boolean isok = false;
            for (String s : strs) {
                if (key.toString().equals(s)) {
                    L.d("isNull", linkedHashMaps.get(key));
                    ToastUtil.showShort(linkedHashMaps.get(key));
                    isok = true;
                    isNull = false;
                }
            }
            if (isok) break;
        }
        return isNull;
    }
    private void getText(){
        bean.setBarcode(tvTiaoma.getText().toString());
        bean.setBrand(tvPinpai.getText().toString());
//        bean.setBrandid("");
        bean.setP_type(tvLeimu.getText().toString());
//        bean.setP_typeid("");
        bean.setName(etShangpimingcheng.getText().toString());
        bean.setFuncation(etShangpinjianjie.getText().toString());
        bean.setSpec(etShangpinguige.getText().toString());
        bean.setWeight(etShangpinzhongliang.getText().toString());
        bean.setPrice(etDanjia.getText().toString());
        bean.setVolume(etTiji.getText().toString());
    }
    List<String> pinpainame;
    List<PinPaiBean> pinPaiBeans;
    private void getPinPaiShuJu() {
        RequestParams params = new RequestParams(APIConfig.ShangPin.AddShangPin);
        params.addBodyParameter("DataType", "Brand_List");
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, PinPaiBean> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, PinPaiBean>>() {
                                }.getType());
                if (responseT.isOk()) {
                    pinpainame = new ArrayList<>();
                    pinPaiBeans=responseT.getDatas();
                    if (pinPaiBeans!= null && pinPaiBeans.size() != 0) {
                        for (int i = 0; i < pinPaiBeans.size(); i++) {
                            pinpainame.add(pinPaiBeans.get(i).getB_Name());
                        }
                    }
                }
            }
        });
    }
    List<String> leimuname;
    List<LeiMuBean> leiMuBeans;
    private void getLeiMuShuJu() {
        RequestParams params = new RequestParams(APIConfig.ShangPin.AddShangPin);
        params.addBodyParameter("DataType", "ProductType_List");
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, LeiMuBean> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, LeiMuBean>>() {
                                }.getType());
                if (responseT.isOk()) {
                    leimuname = new ArrayList<>();
                    leiMuBeans=responseT.getDatas();
                    if (leiMuBeans != null && leiMuBeans.size() != 0) {
                        for (int i = 0; i < leiMuBeans.size(); i++) {
                            leimuname.add(leiMuBeans.get(i).getPT_Name());
                        }
                    }
                }
            }
        });
    }
    public void OpenPinPaiDiaLog() {
        if (pinpainame==null&&pinpainame.size()==0){
            getPinPaiShuJu();
            ToastUtil.showShort("品牌获取失败！正在重新获取");
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(me);
        alert.setListData(pinpainame);
        alert.setTitle("请选择品牌");
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                tvPinpai.setText(info);
                for (PinPaiBean paiBean : pinPaiBeans) {
                    if (paiBean.getB_Name().equals(info)){
                        bean.setBrandid(paiBean.getB_ID());
                        break;
                    }
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9, 0.9, me);
    }

    public void OpenLeiMuDiaLog() {
        if (leimuname==null&&leimuname.size()==0){
            getLeiMuShuJu();
            ToastUtil.showShort("类目获取失败！正在重新获取");
            return;
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(me);
        alert.setListData(leimuname);
        alert.setTitle("请选择类目");
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                tvLeimu.setText(info);
                for (LeiMuBean muBean : leiMuBeans) {
                    if (muBean.getPT_Name().equals(info)){
                        bean.setP_typeid(muBean.getPT_ID());
                        break;
                    }
                }
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9, 0.9, me);
    }
    /**
     * 更新商品
     */
    private void updateShangPin() {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.AddShangPin);
        params.addBodyParameter("DataType", "Product_Update");
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                params.addBodyParameter(field.getName(), field.get(bean) + "");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, PinPaiBean> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, PinPaiBean>>() {
                                }.getType());
                if (responseT.isOk()) {
                    dismissLoadingDialog();
                    ToastUtil.showShort("更新完成!");
                    me.finish();
                }
            }
        });
    }
    /**
     * 提交商品
     */
    private void postShangPin() {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.AddShangPin);
        params.addBodyParameter("DataType", "Product_Insert");
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                params.addBodyParameter(field.getName(), field.get(bean) + "");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                NetBean<?, PinPaiBean> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, PinPaiBean>>() {
                                }.getType());
                if (responseT.isOk()) {
                    dismissLoadingDialog();
                    ToastUtil.showShort("上传完成!");
                    me.finish();
                }
            }
        });
    }
    @OnClick({R.id.iv_back,  R.id.tv_pinpai, R.id.tv_leimu, R.id.btn_tijiaodingdan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                me.finish();
                break;
            case R.id.tv_pinpai:
                OpenPinPaiDiaLog();
                break;
            case R.id.tv_leimu:
                OpenLeiMuDiaLog();
                break;
            case R.id.btn_tijiaodingdan:
                if (isNull()){
                    if (isUpdate){
                        updateShangPin();
                    }else{
                        postShangPin();
                    }

                }
                break;
        }
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
        // 注销获取扫描结果的广播
        this.unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        Controll.close();
        super.onDestroy();
    }
}
