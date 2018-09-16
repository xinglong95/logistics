package com.yusen.logistics.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yusen.logistics.R;
import com.yusen.logistics.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
    TextView tv;
    @Bind(R.id.tv_shangchuanshangpin)
    TextView tvShangchuanshangpin;
    @Bind(R.id.tv_chuangjindingdan)
    TextView tvChuangjindingdan;
    @Bind(R.id.tv_yundanliebiao)
    TextView tvYunDanLieBiao;
    @Bind(R.id.tv_gerenzhongxin)
    TextView tvGerenzhongxin;
    MainActivity me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        me=this;
    }

    @OnClick({R.id.tv_shangchuanshangpin, R.id.tv_chuangjindingdan, R.id.tv_yundanliebiao, R.id.tv_gerenzhongxin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shangchuanshangpin:
                startActivity(new Intent(me,AddShangPinActivity.class));
                break;
            case R.id.tv_chuangjindingdan:
                startActivity(new Intent(me,DingDanActivity.class));
                break;
            case R.id.tv_yundanliebiao:
                startActivity(new Intent(me,YunDanListActivity.class));
                break;
            case R.id.tv_gerenzhongxin:
                startActivity(new Intent(me,GenRenActivity.class));
                break;
        }
    }
}
