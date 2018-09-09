package com.yusen.logistics.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.utils.ToastUtil;
import com.code19.library.L;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yusen.logistics.R;
import com.yusen.logistics.base.BaseActivity;
import com.yusen.logistics.base.ConstantConfig;
import com.yusen.logistics.base.PhotoSelectBaseActivity;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddIDCardActivity extends PhotoSelectBaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idcard);
        ButterKnife.bind(this);
        tvTitle.setText("上传身份证");
        initAccessToken();
    }
    private List<LocalMedia> localMedias_z = new ArrayList<>();
    private List<LocalMedia> localMedias_f = new ArrayList<>();
    @OnClick({R.id.iv_idcard_z, R.id.iv_idcard_f, R.id.btn_tijiaodingdan,R.id.iv_back, R.id.tv_righttext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_idcard_z:
                SeletPicture(PictureConfig.SINGLE, 1, localMedias_z, 11);
                break;
            case R.id.iv_idcard_f:
                SeletPicture(PictureConfig.SINGLE, 1, localMedias_f, 12);
                break;
            case R.id.btn_tijiaodingdan:
                break;
            case R.id.iv_back:
                me.finish();
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
        param.setImageQuality(20);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                      if (idCardSide.equals("front")){
                          setIDCardInfo(result);
                      }else if(idCardSide.equals("back")){

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
    private void setIDCardInfo(IDCardResult result){
        etXingming.setText(result.getName().toString());
        etXingbie.setText(result.getGender().toString());
        etMinzu.setText(result.getEthnic().toString());
        etChushengnianyue.setText(result.getBirthday().toString());
        etShenfenzhenghao.setText(result.getIdNumber().toString());
        etDizhi.setText(result.getAddress().toString());
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
                recIDCard("front",idcardpath_z);
            } else if (requestCode == 12) {
                localMedias_f = PictureSelector.obtainMultipleResult(data);
                idcardpath_f = localMedias_f.get(0).getCompressPath();
                x.image().bind(ivIdcardF, idcardpath_f);
                recIDCard("back",idcardpath_f);
            }
        }
    }
}
