package com.yusen.logistics.ui;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_ren);
        ButterKnife.bind(this);
        tvTitle.setText("个人中心");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        me.finish();
    }
}
