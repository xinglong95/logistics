package com.yusen.logistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import com.yusen.logistics.bean.LoginInfoBean;

import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_zhanghao)
    EditText etZhanghao;
    @Bind(R.id.et_mima)
    EditText etMima;
    @Bind(R.id.btn_denglu)
    Button btnDenglu;
    @Bind(R.id.tv_gongsijianjie)
    TextView tvGongsijianjie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        etZhanghao.setText(LOGApplication.getUsername());
        etMima.setText(LOGApplication.getPassword());
    }

    private boolean isNull() {
        boolean isNull = true;
        if (MTextUtils.isEmpty(etZhanghao.getText().toString())) {
            isNull = false;
            ToastUtil.showShort("请输入账号");
        } else if (MTextUtils.isEmpty(etMima.getText().toString())) {
            isNull = false;
            ToastUtil.showShort("请输入密码");
        }
        return isNull;
    }

    @OnClick(R.id.btn_denglu)
    public void onViewClicked() {
        if (isNull()) {
            showLoadingDialog();
            RequestParams params = new RequestParams(APIConfig.User.Login);
            params.addBodyParameter("DataType", "Login");
            params.addBodyParameter("account", etZhanghao.getText().toString());
            params.addBodyParameter("password", etMima.getText().toString());
            XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    dismissLoadingDialog();
                    NetBean<LoginInfoBean, ?> responseT = GsonUtils
                            .parseJson(
                                    result,
                                    new TypeToken<NetBean<LoginInfoBean, ?>>() {
                                    }.getType());
                    if (responseT.isOk()) {
                        LOGApplication.setToken(responseT.getData().getToken());
                        LOGApplication.setUserinfo(responseT.getData());
                        LOGApplication.setUsername(etZhanghao.getText().toString());
                        LOGApplication.setPassword(etMima.getText().toString());
                        ToastUtil.showShort("登录成功");
                        startActivity(new Intent(me, MainActivity.class));
                        me.finish();
                    }else{
                        ToastUtil.showShort(responseT.getInfo());
                    }
                }
            });
        }
    }

    @OnClick(R.id.tv_gongsijianjie)
    public void onViewClickedGongSi() {
        startActivity(new Intent(me,JianJieActivity.class));
    }
}
