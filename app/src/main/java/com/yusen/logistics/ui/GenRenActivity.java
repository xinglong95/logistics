package com.yusen.logistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusen.logistics.R;
import com.yusen.logistics.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenRenActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_righttext)
    TextView tvRighttext;
    @Bind(R.id.tv_xiugaimima)
    TextView tvXiugaimima;
    @Bind(R.id.iv_touxiang)
    ImageView ivTouxiang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_ren);
        ButterKnife.bind(this);
        tvTitle.setText("个人中心");
        tvTitle.setText("修改密码");
    }


    @OnClick({R.id.iv_back, R.id.iv_touxiang, R.id.tv_xiugaimima})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                me.finish();
                break;
            case R.id.iv_touxiang:
                break;
            case R.id.tv_xiugaimima:
                startActivity(new Intent(me,ChangePassActivity.class));
                break;
        }
    }
}
