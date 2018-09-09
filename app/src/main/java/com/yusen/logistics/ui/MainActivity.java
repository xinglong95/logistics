package com.yusen.logistics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yusen.logistics.R;
import com.yusen.logistics.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    TextView tv;
    @Bind(R.id.tv_shangchuanshangpin)
    TextView tvShangchuanshangpin;
    @Bind(R.id.tv_chuangjindingdan)
    TextView tvChuangjindingdan;
    @Bind(R.id.tv_gongsijianjie)
    TextView tvGongsijianjie;
    @Bind(R.id.tv_gerenzhongxin)
    TextView tvGerenzhongxin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_shangchuanshangpin, R.id.tv_chuangjindingdan, R.id.tv_gongsijianjie, R.id.tv_gerenzhongxin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shangchuanshangpin:
                startActivity(new Intent(me,AddShangPinActivity.class));
                break;
            case R.id.tv_chuangjindingdan:
                startActivity(new Intent(me,DingDanActivity.class));
                break;
            case R.id.tv_gongsijianjie:
                break;
            case R.id.tv_gerenzhongxin:
                break;
        }
    }
}
