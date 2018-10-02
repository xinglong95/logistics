package com.yusen.logistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.citzx.cslibrary.net.NetBean;
import com.citzx.cslibrary.net.RequestCallBack;
import com.citzx.cslibrary.net.XutilHttpHelp;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.utils.MTextUtils;
import com.citzx.cslibrary.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;
import com.yusen.logistics.R;
import com.yusen.logistics.base.APIConfig;
import com.yusen.logistics.base.BaseActivity;
import com.yusen.logistics.base.LOGApplication;
import com.yusen.logistics.bean.ShangPinInfoBean;

import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_righttext)
    TextView tvRighttext;
    @Bind(R.id.et_yuanmima)
    EditText etYuanmima;
    @Bind(R.id.et_xinmima)
    EditText etXinmima;
    @Bind(R.id.btn_tijiao)
    Button btnTijiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(this);
        tvTitle.setText("修改密码");
    }

    @OnClick({R.id.iv_back, R.id.btn_tijiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                me.finish();
                break;
            case R.id.btn_tijiao:
                if (MTextUtils.isEmpty(etYuanmima.getText().toString())){
                     ToastUtil.showShort("请输入原密码");
                }else if(MTextUtils.isEmpty(etXinmima.getText().toString())){
                     ToastUtil.showShort("请输入新密码");
                }else{
                     postNewPass();
                }
                break;
        }
    }
    /**
     */
    private void postNewPass() {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.User.Login);
        params.addBodyParameter("DataType", "Update_Password");
        params.addBodyParameter("password",etYuanmima.getText().toString());
        params.addBodyParameter("newpassword",etXinmima.getText().toString());
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                dismissLoadingDialog();
                NetBean<ShangPinInfoBean, ?> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<ShangPinInfoBean, ?>>() {
                                }.getType());
                if (responseT.isOk()) {
                    LOGApplication.reMoveLoginPassWord();
                    ToastUtil.showShort("修改成功请重新登录");
                    startActivity(new Intent(me, LoginActivity.class));
                    LOGApplication.removeAllActiviyies();
                }else{
                    ToastUtil.showShort(responseT.getInfo());
                }
            }
        });
    }
}
