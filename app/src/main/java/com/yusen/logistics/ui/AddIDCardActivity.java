package com.yusen.logistics.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.citzx.cslibrary.core.LibraryApplication;
import com.citzx.cslibrary.net.NetBean;
import com.citzx.cslibrary.net.RequestCallBack;
import com.citzx.cslibrary.net.XutilHttpHelp;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.utils.MTextUtils;
import com.citzx.cslibrary.utils.MyTimeUtils;
import com.citzx.cslibrary.utils.ToastUtil;
import com.citzx.cslibrary.utils.Tools;
import com.citzx.cslibrary.utils.addressselect.bean.City;
import com.citzx.cslibrary.utils.addressselect.bean.County;
import com.citzx.cslibrary.utils.addressselect.bean.Province;
import com.citzx.cslibrary.utils.addressselect.bean.Street;
import com.citzx.cslibrary.utils.addressselect.widget.AddressSelector;
import com.citzx.cslibrary.utils.addressselect.widget.BottomDialog;
import com.citzx.cslibrary.utils.addressselect.widget.OnAddressSelectedListener;
import com.code19.library.L;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.yusen.logistics.R;
import com.yusen.logistics.base.APIConfig;
import com.yusen.logistics.base.LOGApplication;
import com.yusen.logistics.base.PhotoSelectBaseActivity;
import com.yusen.logistics.bean.SubmitDingDanBean;
import com.yusen.logistics.utils.FileService;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddIDCardActivity extends PhotoSelectBaseActivity implements OnAddressSelectedListener, AddressSelector.OnDialogCloseListener, AddressSelector.onSelectorAreaPositionListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_righttext)
    TextView tvRighttext;
    @Bind(R.id.iv_idcard_z)
    ImageView ivIdcardZ;
    @Bind(R.id.iv_idcard_f)
    ImageView ivIdcardF;
    @Bind(R.id.et_xingming)
    EditText etXingming;
    @Bind(R.id.et_xingbie)
    EditText etXingbie;
    @Bind(R.id.et_minzu)
    EditText etMinzu;
    @Bind(R.id.et_chushengnianyue)
    EditText etChushengnianyue;
    @Bind(R.id.et_dizhi)
    EditText etDizhi;
    @Bind(R.id.et_shenfenzhenghao)
    EditText etShenfenzhenghao;
    @Bind(R.id.btn_tijiaodingdan)
    Button btnTijiaodingdan;
    @Bind(R.id.et_shouhuoren)
    EditText etShouhuoren;
    @Bind(R.id.et_shouhuodianhua)
    EditText etShouhuodianhua;
    @Bind(R.id.tv_shouhuodiqu)
    TextView tvShouHuodiqu;
    @Bind(R.id.et_shouhuoxiangxidizhi)
    EditText etShouhuoxiangxidizhi;
    @Bind(R.id.ll_shenfenzhengxinxi)
    LinearLayout llShenfenzhengxinxi;
    @Bind(R.id.et_fahuoren)
    EditText etFahuoren;
    @Bind(R.id.et_fahuoshouji)
    EditText etFahuoshouji;
    @Bind(R.id.tv_fahuodiqu)
    TextView tvFahuodiqu;
    @Bind(R.id.et_fahuoxiangxidizhi)
    EditText etFahuoxiangxidizhi;
    BottomDialog dialog_china;
    BottomDialog dialog_japan;
    SubmitDingDanBean submitDingDanBean;
    String shijiweight;
    @Bind(R.id.et_daigouren)
    EditText etDaigouren;
    @Bind(R.id.et_daigoudianhua)
    EditText etDaigoudianhua;
    @Bind(R.id.tv_fahuoshijian)
    TextView tvFahuoshijian;
    final FileService fs = new FileService(me);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idcard);
        ButterKnife.bind(this);
        tvTitle.setText("补充订单信息");
        submitDingDanBean = new SubmitDingDanBean();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            submitDingDanBean = (SubmitDingDanBean) bundle.getSerializable("DingDan");
        }
        shijiweight = getIntent().getStringExtra("weight");
        initAccessToken();
        setFaHuoReniNFO();
        if (!submitDingDanBean.getW_ID().equals("0")){
            setText();
        }
    }
    private void setText(){
        etXingming.setText(submitDingDanBean.getIdCard_Name());
        etXingbie.setText(submitDingDanBean.getIdCard_Sex());
        etMinzu.setText(submitDingDanBean.getIdCard_National());
        etDizhi.setText(submitDingDanBean.getIdCard_Address());
        etChushengnianyue.setText(submitDingDanBean.getIdCard_BrithDate());
        etShenfenzhenghao.setText(submitDingDanBean.getIdCard_Number());

        etFahuoren.setText(submitDingDanBean.getW_F_Name());
        etFahuoshouji.setText(submitDingDanBean.getW_F_Tel());
        etFahuoxiangxidizhi.setText(submitDingDanBean.getW_F_Address());
        tvFahuodiqu.setText(submitDingDanBean.getW_F_Province()+submitDingDanBean.getW_F_City()+submitDingDanBean.getW_F_District());

        etShouhuoren.setText(submitDingDanBean.getW_S_Name());
        etShouhuodianhua.setText(submitDingDanBean.getW_S_Tel());
        etShouhuoxiangxidizhi.setText(submitDingDanBean.getW_S_Address());
        tvShouHuodiqu.setText(submitDingDanBean.getW_S_Province()+submitDingDanBean.getW_S_City()+submitDingDanBean.getW_S_District());
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy/MM/dd");//定义
        try {
            Date d1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(submitDingDanBean.getW_DeliveryDate() + "");//定义起始日期
            tvFahuoshijian.setText(sdf0.format(d1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        etDaigouren.setText(submitDingDanBean.getW_D_Name());
        etDaigoudianhua.setText(submitDingDanBean.getW_D_Tel());

        x.image().bind(ivIdcardZ,submitDingDanBean.getIdCard_Head());
        x.image().bind(ivIdcardF,submitDingDanBean.getIdCard_Tail());
        String path_z=submitDingDanBean.getIdCard_Head().substring(submitDingDanBean.getIdCard_Head().indexOf("/IdCard") + 1);
        String path_f=submitDingDanBean.getIdCard_Tail().substring(submitDingDanBean.getIdCard_Tail().indexOf("/IdCard") + 1);
        shijiweight=submitDingDanBean.getW_Actual_Weight();
        submitDingDanBean.setIdCard_Head("/Image/"+path_z);
        submitDingDanBean.setIdCard_Tail("/Image/"+path_f);



    }
    /**
     * 获取数据
     */
    private void getText() {
        submitDingDanBean.setIdCard_Name(etXingming.getText().toString());
        submitDingDanBean.setIdCard_Sex(etXingbie.getText().toString());
        submitDingDanBean.setIdCard_National(etMinzu.getText().toString());
        submitDingDanBean.setIdCard_Address(etDizhi.getText().toString());
        submitDingDanBean.setIdCard_BrithDate(etChushengnianyue.getText().toString());
        submitDingDanBean.setIdCard_Number(etShenfenzhenghao.getText().toString());

        submitDingDanBean.setW_F_Name(etFahuoren.getText().toString());
        submitDingDanBean.setW_F_Tel(etFahuoshouji.getText().toString());
        submitDingDanBean.setW_F_Address(etFahuoxiangxidizhi.getText().toString());

        submitDingDanBean.setW_S_Name(etShouhuoren.getText().toString());
        submitDingDanBean.setW_S_Tel(etShouhuodianhua.getText().toString());
        submitDingDanBean.setW_S_Address(etShouhuoxiangxidizhi.getText().toString());

        submitDingDanBean.setW_DeliveryDate(tvFahuoshijian.getText().toString());
        submitDingDanBean.setW_D_Name(etDaigouren.getText().toString());
        submitDingDanBean.setW_D_Tel(etDaigoudianhua.getText().toString());

//                * IdCard_Organ : 青岛市北公安局
//                * IdCard_BeginDate : 2011
//                * IdCard_EndDate : 2028
//                * IdCard_Head : /Image/IdCard/1.png
//                * IdCard_Tail : /Image/IdCard/2.png


    }

    private List<LocalMedia> localMedias_z = new ArrayList<>();
    private List<LocalMedia> localMedias_f = new ArrayList<>();

    @OnClick({R.id.iv_idcard_z, R.id.iv_idcard_f, R.id.btn_tijiaodingdan, R.id.iv_back, R.id.tv_righttext, R.id.tv_shouhuodiqu, R.id.tv_fahuodiqu,R.id.tv_fahuoshijian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_idcard_z:
                SeletPicture(PictureConfig.SINGLE, 1, localMedias_z, 11);
                break;
            case R.id.iv_idcard_f:
                SeletPicture(PictureConfig.SINGLE, 1, localMedias_f, 12);
                break;
            case R.id.btn_tijiaodingdan:
                if (isNull()) {
                    postDingDan();
                }
                break;
            case R.id.iv_back:
                me.finish();
                break;

            case R.id.tv_shouhuodiqu:
                LibraryApplication.setAddressType("China");
                if (dialog_china != null) {
                    dialog_china.show();
                } else {
                    dialog_china = new BottomDialog(this);
                    dialog_china.setOnAddressSelectedListener(this);
                    dialog_china.setDialogDismisListener(this);
                    dialog_china.setTextSize(14);//设置字体的大小
                    dialog_china.setIndicatorBackgroundColor(android.R.color.holo_red_dark);//设置指示器的颜色
                    dialog_china.setTextSelectedColor(android.R.color.holo_red_dark);//设置字体获得焦点的颜色
                    dialog_china.setTextUnSelectedColor(android.R.color.black);//设置字体没有获得焦点的颜色
//            dialog.setDisplaySelectorArea("31",1,"2704",1,"2711",0,"15582",1);//设置已选中的地区
                    dialog_china.setSelectorAreaPositionListener(this);
                    dialog_china.show();
                }
                break;
            case R.id.tv_fahuodiqu:
                LibraryApplication.setAddressType("Japan");
                if (dialog_japan != null) {
                    dialog_japan.show();
                } else {
                    dialog_japan = new BottomDialog(this);
                    dialog_japan.setOnAddressSelectedListener(this);
                    dialog_japan.setDialogDismisListener(this);
                    dialog_japan.setTextSize(14);//设置字体的大小
                    dialog_japan.setIndicatorBackgroundColor(android.R.color.holo_red_dark);//设置指示器的颜色
                    dialog_japan.setTextSelectedColor(android.R.color.holo_red_dark);//设置字体获得焦点的颜色
                    dialog_japan.setTextUnSelectedColor(android.R.color.black);//设置字体没有获得焦点的颜色
//            dialog.setDisplaySelectorArea("31",1,"2704",1,"2711",0,"15582",1);//设置已选中的地区
                    dialog_japan.setSelectorAreaPositionListener(this);
                    dialog_japan.show();
                }
                break;
            case R.id.tv_fahuoshijian:
                TimePickerView pvTime = new TimePickerBuilder(me, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvFahuoshijian.setText(MyTimeUtils.toString(date, new SimpleDateFormat("yyyy-MM-dd")));
                    }
                }).setSubCalSize(15)
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示，年月日时分秒
                        .setTitleColor(Color.WHITE)//标题文字颜色
                        .setSubmitColor(getResources().getColor(R.color.white))//确定按钮文字颜色
                        .setCancelColor(getResources().getColor(R.color.white))//取消按钮文字颜色
                        .setTitleBgColor(getResources().getColor(R.color.black))//标题背景颜色 Night mode
                        .setBgColor(getResources().getColor(R.color.black)).build();//滚轮背景颜色 Night mode.build();
                pvTime.show();
                break;
        }
    }

    /**
     */
    private void initAccessToken() {
        OCR.getInstance(getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                String msg = error.toString();
            }
        }, getApplicationContext());
    }

    private void recIDCard(final String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(50);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    if (idCardSide.equals("front")) {
                        setIDCardInfo(result);
                    } else if (idCardSide.equals("back")) {
                        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy/MM/dd");//定义
                        try {
                            Date d1 = new SimpleDateFormat("yyyyMMdd").parse(result.getSignDate().getWords());//定义起始日期
                            submitDingDanBean.setIdCard_BeginDate(sdf0.format(d1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            Date d2 = new SimpleDateFormat("yyyyMMdd").parse(result.getExpiryDate().getWords());//定义起始日期
                            submitDingDanBean.setIdCard_EndDate(sdf0.format(d2));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        submitDingDanBean.setIdCard_Organ(result.getIssueAuthority().getWords());
                    }
                }
            }

            @Override
            public void onError(OCRError error) {
//                alertText("", error.getMessage());
                String msg = error.toString();
                ToastUtil.showShort(msg);
            }
        });
    }

    private void setIDCardInfo(IDCardResult result) {
        etXingming.setText(result.getName().toString());
        etXingbie.setText(result.getGender().toString());
        etMinzu.setText(result.getEthnic().toString());
        String str=result.getBirthday().toString();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy/MM/dd");//定义
        try {
            Date d1 = new SimpleDateFormat("yyyyMMdd").parse(str);//定义起始日期
            etChushengnianyue.setText(sdf0.format(d1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        etShenfenzhenghao.setText(result.getIdNumber().toString());
        etDizhi.setText(result.getAddress().toString());
    }

    private void setFaHuoReniNFO() {
        etFahuoren.setText(LOGApplication.getUserinfo().getO_Name());
        etFahuoshouji.setText(LOGApplication.getUserinfo().getO_Tel());
        tvFahuodiqu.setText(LOGApplication.getUserinfo().getO_Province() + LOGApplication.getUserinfo().getO_City() + LOGApplication.getUserinfo().getO_District());
        etFahuoxiangxidizhi.setText(LOGApplication.getUserinfo().getO_Address());
        submitDingDanBean.setW_F_Province(LOGApplication.getUserinfo().getO_Province());
        submitDingDanBean.setW_F_City(LOGApplication.getUserinfo().getO_City());
        submitDingDanBean.setW_F_District(LOGApplication.getUserinfo().getO_District());
    }

    String idcardpath_z;
    String idcardpath_f;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == 11) {
                localMedias_z = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                idcardpath_z = localMedias_z.get(0).getCompressPath();
                x.image().bind(ivIdcardZ, idcardpath_z);
                recIDCard("front", idcardpath_z);
                postFile(idcardpath_z, 1);//1代表正面
                llShenfenzhengxinxi.setVisibility(View.VISIBLE);
            } else if (requestCode == 12) {
                localMedias_f = PictureSelector.obtainMultipleResult(data);
                idcardpath_f = localMedias_f.get(0).getCompressPath();
                x.image().bind(ivIdcardF, idcardpath_f);
                recIDCard("back", idcardpath_f);
                postFile(idcardpath_f, 0);//0代表反面
//                llShenfenzhengxinxi.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * @param path 上传文件
     */
    private void postFile(String path, final int type) {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.getShangPin);
        params.setMultipart(true);
        params.addBodyParameter("DataType", "Waybill_Image_file");
        params.addBodyParameter("File1", new File(path));
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                dismissLoadingDialog();
                NetBean<String, ?> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<String, ?>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (type == 1) {
                        submitDingDanBean.setIdCard_Head(responseT.getInfo());
                    } else {
                        submitDingDanBean.setIdCard_Tail(responseT.getInfo());
                    }
                }else{
                    ToastUtil.showShort(responseT.getInfo());
                }
            }
        });
    }

    /**
     * 提交订单
     */
    private void postDingDan() {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.getShangPin);
        params.addBodyParameter("DataType", "Waybill_Save");
        params.addBodyParameter("weight", shijiweight);
        params.addBodyParameter("tab","["+GsonUtils.toJSON(submitDingDanBean)+"]");
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                dismissLoadingDialog();
                NetBean<SubmitDingDanBean, ?> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<SubmitDingDanBean, ?>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (submitDingDanBean.getW_ID().equals("0")){
                        DeleteInfo();
                    }
                    LOGApplication.removeAllActiviyies();
                    ToastUtil.showShort("提交完成");
                }else{
                    ToastUtil.showShort(responseT.getInfo());
                }
            }
        });
    }
    /**
     * @return 判空
     */
    private boolean isNull(){
        getText();
        boolean isNull=true;
        ArrayList<String> strs = new ArrayList<>();
        strs = Tools.CheckNullByReflect4Keys(submitDingDanBean, new String[]{"Brand", "Brand_ID","P_type", "P_typeid","W_DeliveryDate","W_D_Name","W_D_Tel"});
        LinkedHashMap<String, String> linkedHashMaps = new LinkedHashMap<>();
        linkedHashMaps.put("W_S_Name", "请填写收货人姓名");
        linkedHashMaps.put("W_S_Tel", "请填写收货人电话");
        linkedHashMaps.put("W_S_Province", "请选择收货地区");
        linkedHashMaps.put("W_S_Address", "请填写收货详细地址");
        linkedHashMaps.put("IdCard_Name", "请上传身份证正面");
        linkedHashMaps.put("IdCard_Organ", "请上传身份证反面");
        linkedHashMaps.put("W_F_Name", "请填写发货人姓名");
        linkedHashMaps.put("W_F_Tel", "请填写发货人电话");
        linkedHashMaps.put("W_F_Province", "请选择发货地区");
        linkedHashMaps.put("W_F_Address", "请填写发货详细地址");
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
    @Override
    public void dialogclose() {
        if (dialog_china != null) {
            dialog_china.dismiss();
        }
        if (dialog_japan != null) {
            dialog_japan.dismiss();
        }
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {

    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s = (province == null ? "" : province.C_Province) + (city == null ? "" : city.C_City) + (county == null ? "" : county.C_District) +
                (street == null ? "" : street.C_Street);
        if (LibraryApplication.getAddressType().equals("China")) {
            submitDingDanBean.setW_S_Province(province.C_Province);
            submitDingDanBean.setW_S_City(city.C_City);
            submitDingDanBean.setW_S_District(county.C_District);
            tvShouHuodiqu.setText(s);
        } else if (LibraryApplication.getAddressType().equals("Japan")) {
            submitDingDanBean.setW_F_Province(province.C_Province);
            submitDingDanBean.setW_F_City(city.C_City);
            submitDingDanBean.setW_F_District(county.C_District);
            tvFahuodiqu.setText(s);
        }
        if (dialog_china != null) {
            dialog_china.dismiss();
        }
        if (dialog_japan != null) {
            dialog_japan.dismiss();
        }
    }
    private void DeleteInfo() {
        try {
            fs.saveToSD("logdingdan.txt", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
