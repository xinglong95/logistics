package com.yusen.logistics.ui;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citzx.cslibrary.net.NetBean;
import com.citzx.cslibrary.net.RequestCallBack;
import com.citzx.cslibrary.net.XutilHttpHelp;
import com.citzx.cslibrary.utils.GsonUtils;
import com.citzx.cslibrary.utils.ToastUtil;
import com.citzx.cslibrary.view.AbsBaseAdapter;
import com.code19.library.L;
import com.google.gson.reflect.TypeToken;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.yusen.logistics.R;
import com.yusen.logistics.base.APIConfig;
import com.yusen.logistics.base.BaseActivity;
import com.yusen.logistics.bean.BluetoothBean;
import com.yusen.logistics.bean.SubmitDingDanBean;
import com.yusen.logistics.bean.YunDanListBean;

import org.xutils.http.RequestParams;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YunDanListActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_righttext)
    TextView tvRighttext;
    AbsBaseAdapter adapter;
    @Bind(R.id.lv_dingdanliebiao)
    ListView lvDingdanliebiao;
    private PrinterServiceConnection conn = null;
    private GpService mGpService = null;
    private BluetoothSocket socket;//蓝牙socket
    private ConnectThread mThread;//连接的蓝牙线程
    private BluetoothAdapter adapter_blue;//蓝牙适配器
    private List<BluetoothBean> mBluetoothList;//搜索的蓝牙设备
    boolean isprinconnect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yun_dan_list);
        ButterKnife.bind(this);
        tvTitle.setText("订单列表");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.finish();
            }
        });
        getDingDanList();
        startService();//开启服务
        connection();//创建连接
//        printOrder();//打印
//        closePrin();
    }

    @OnClick({R.id.iv_back, R.id.tv_righttext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_righttext:
                searchBlueToothDevice();//搜索蓝牙并连接
                break;
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
                dismissLoadingDialog();
                NetBean<SubmitDingDanBean, ?> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<SubmitDingDanBean, ?>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getData() != null) {
                        SubmitDingDanBean bean=responseT.getData();
                        printOrder(bean);
                    }

                } else {
                    ToastUtil.showShort(responseT.getInfo());
                }
            }
        });
    }

    List<YunDanListBean> list = new ArrayList<>();

    /**
     * 获取运单列表
     */
    private void getDingDanList() {
        showLoadingDialog();
        RequestParams params = new RequestParams(APIConfig.ShangPin.getShangPin);
        params.addBodyParameter("DataType", "Waybill_List");
        XutilHttpHelp.getInstance().BaseInfoHttp(params, me, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                dismissLoadingDialog();
                NetBean<?, YunDanListBean> responseT = GsonUtils
                        .parseJson(
                                result,
                                new TypeToken<NetBean<?, YunDanListBean>>() {
                                }.getType());
                if (responseT.isOk()) {
                    if (responseT.getDatas() != null) {
                        list = responseT.getDatas();
                        setData();
                    }
                }else{
                    ToastUtil.showShort(responseT.getInfo());
                }
            }
        });
    }

    private void setData() {
        adapter = new AbsBaseAdapter<YunDanListBean>(this, R.layout.item_dingdanlist) {
            @Override
            protected void bindDatas(ViewHolder viewHolder,final  YunDanListBean data, int position) {
                viewHolder.bindTextView(R.id.tv_danhao, "单号："+data.getW_OrderNo());
                viewHolder.bindTextView(R.id.tv_shouhuoren, "收货人:"+data.getW_S_Name());
                viewHolder.bindTextView(R.id.tv_shouhuodizhi, "收货地址："+data.getW_S_Province()+data.getW_S_City()+data.getW_S_District()+data.getW_S_Address());
                SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy/MM/dd");//定义
                try {
                    Date d1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(data.getW_DeliveryDate() + "");//定义起始日期
                    viewHolder.bindTextView(R.id.tv_fahuoshijian, sdf0.format(d1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Button btn_dayin= (Button) viewHolder.getView(R.id.btn_dayin);
                btn_dayin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isprinconnect){
                            getYundanInfo(data.getW_ID());
                        }else{
                            ToastUtil.showShort("请先连接打印机");
                        }
                    }
                });

            }
        };
        adapter.setDatas(list);
        lvDingdanliebiao.setAdapter(adapter);
        lvDingdanliebiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(me,DingDanActivity.class);
                intent.putExtra("WID",list.get(position).getW_ID());
                startActivity(intent);
            }
        });
    }
    private void startService() {
        Intent i = new Intent(this, GpPrintService.class);
        startService(i);
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        final Intent intent = new Intent();
        intent.setAction("com.gprinter.aidl.GpPrintService");
        intent.setPackage(this.getPackageName());
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }
    private class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }
    BluetoothDevice mBluetoothDevice_peidui;
    String deviceAddress_peidui;
    public void searchBlueToothDevice() {

        L.d("searchBlueToothDevice(MainActivity.java:112)--->> " + "searchBlueToothDevice");

//        pdSearch = ProgressDialog.show(MainActivity.this, "", "连接中", true, true);
//        pdSearch.setCanceledOnTouchOutside(false);
//        pdSearch.show();

        mBluetoothList = new ArrayList<>();
        // 检查设备是否支持蓝牙
        adapter_blue = BluetoothAdapter.getDefaultAdapter();
        if (adapter_blue == null) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        // 如果蓝牙已经关闭就打开蓝牙
        if (!adapter_blue.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
            return;
        }
        // 获取已配对的蓝牙设备
        Set<BluetoothDevice> devices = adapter_blue.getBondedDevices();
        // 遍历
        int count = 0;
        for (BluetoothDevice pairedDevice : devices) {
            L.d("searchBlueToothDevice(MainActivity.java:137)--->> " + pairedDevice.getName());
            if (pairedDevice.getName() == null) {
                return;
            } else if (pairedDevice.getName().startsWith("QR380A")) {
                count++;
                deviceAddress_peidui = pairedDevice.getAddress();
                mBluetoothDevice_peidui = adapter_blue.getRemoteDevice(deviceAddress_peidui);
                connect(deviceAddress_peidui, mBluetoothDevice_peidui);
                break;
            }else{
                ToastUtil.showShort("暂无已配对的打印机");
            }
        }

//        if (adapter.isEnabled()) {
//            //开始搜索
//            adapter.startDiscovery();
//
//            // 设置广播信息过滤
//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//            // 注册广播接收器，接收并处理搜索结果
//            receiver = new MyBroadcastReceiver();
//            registerReceiver(receiver, intentFilter);
//        }
    }
    /**
     * 启动连接蓝牙的线程方法
     */
    public synchronized void connect(String macAddress, BluetoothDevice device) {
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
        if (socket != null) {
            try {
                mGpService.closePort(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            socket = null;
        }
        mThread = new ConnectThread(macAddress, device);
        mThread.start();
    }
    private class ConnectThread extends Thread {
        private BluetoothDevice mmDevice;
        private OutputStream mmOutStream;

        public ConnectThread(String mac, BluetoothDevice device) {
            mmDevice = device;
            String SPP_UUID = "00001101-0000-1000-8000-00805f9b34fb";
            try {
                if (socket == null) {
                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            adapter_blue.cancelDiscovery();
            try {
                L.d("run(MainActivity.java:367)--->> " + "连接socket");
                if (socket.isConnected()) {
                    L.d("run(MainActivity.java:369)--->> " + "已经连接过了");
                } else {
                    if (socket != null) {
                        try {
                            if (mGpService != null) {
                                int state = mGpService.getPrinterConnectStatus(0);
                                switch (state) {
                                    case GpDevice.STATE_CONNECTED:
                                        break;
                                    case GpDevice.STATE_LISTEN:
                                        L.d( "run(MainActivity.java:379)--->> " + "state:STATE_LISTEN");
                                        break;
                                    case GpDevice.STATE_CONNECTING:
                                        L.d("run(MainActivity.java:382)--->> " + "state:STATE_CONNECTING");
                                        break;
                                    case GpDevice.STATE_NONE:
                                        L.d( "run(MainActivity.java:385)--->> " + "state:STATE_NONE");
                                        registerBroadcast();
                                        mGpService.openPort(0, 4, mmDevice.getAddress(), 0);
                                        break;
                                    default:
                                        L.d( "run(MainActivity.java:390)--->> " + "state:default");
                                        break;
                                }
                            } else {
                                L.d( "run(MainActivity.java:394)--->> " + "mGpService IS NULL");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception connectException) {
                L.d( "run(MainActivity.java:402)--->> " + "连接失败");
                try {
                    if (socket != null) {
                        mGpService.closePort(0);
                        socket = null;
                    }
                } catch (Exception closeException) {

                }
            }
        }
    }
    public static final String ACTION_CONNECT_STATUS = "action.connect.status";

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECT_STATUS);
        registerReceiver(printerStatusBroadcastReceiver, filter);
    }

    private BroadcastReceiver printerStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                if (type == GpDevice.STATE_CONNECTING) {
                    L.d( "onReceive(MainActivity.java:430)--->> " + "STATE_CONNECTING");
                } else if (type == GpDevice.STATE_NONE) {
                    L.d( "onReceive(MainActivity.java:432)--->> " + "STATE_NONE");
//                    showErrorDialog();
                    ToastUtil.showShort("打印机连接失败，请重新连接");
                    tvRighttext.setText("重新连接");
                    isprinconnect=false;
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    //打印机-有效的打印机
                    L.d( "onReceive(MainActivity.java:436)--->> " + "STATE_VALID_PRINTER");
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    L.d( "onReceive(MainActivity.java:438)--->> " + "STATE_INVALID_PRINTER");
                } else if (type == GpDevice.STATE_CONNECTED) {
                    //表示已连接可以打印
                    L.d( "onReceive(MainActivity.java:441)--->> " + "STATE_CONNECTED");
                    unregisterReceiver(printerStatusBroadcastReceiver);
//                    showSuccessDialog();
                    ToastUtil.showShort("打印机连接成功，请打印");
                    tvRighttext.setText("已连接");
                    isprinconnect=true;
                } else if (type == GpDevice.STATE_LISTEN) {
                    L.d( "onReceive(MainActivity.java:445)--->> " + "STATE_LISTEN");
                }
            }
        }
    };

    /**
     * 开始打印
     */
    private void printOrder(SubmitDingDanBean bean) {
        L.d("printOrder(MainActivity.java:500)--->> " + "printOrder");
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(1); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        L.d( "sendLabel(MainActivity.java:507)--->> " + EscCommand.ENABLE.ON.getValue());
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文
        tsc.addText(300, 15, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                bean.getW_S_Name()+" "+bean.getW_S_Tel());
        tsc.addText(260, 15, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                bean.getW_S_Province()+bean.getW_S_City()+bean.getW_S_District());
        tsc.addText(230, 15, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                bean.getW_S_Address());
        tsc.addText(190, 15, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "重量:"+bean.getW_Actual_Weight()+"kg 运费:"+bean.getW_Fee()+"元 数量:"+bean.getW_Firstcount()+"件");
        tsc.addText(160, 15, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "快递单号:"+bean.getW_Courier_Number());
        tsc.addText(130, 15, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "发货日期:"+bean.getW_DeliveryDate());
        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        tsc.addBitmap(20, 50, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);

//        //二维码
//        tsc.addQRCode(200, 70, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, " www.gprinter.com.cn");

        // 绘制一维条码
        tsc.add1DBarcode(100, 15, LabelCommand.BARCODETYPE.CODE128, 50, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_90, bean.getW_OrderNo());

        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
//        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);

        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(0, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭打印
     */
    private void closePrin(){
        if (conn != null) {
            unbindService(conn);
        }
    }
}
