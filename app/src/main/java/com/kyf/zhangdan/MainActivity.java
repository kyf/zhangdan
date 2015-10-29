package com.kyf.zhangdan;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RemoteViews;

import com.kyf.zhangdan.view.MyLoading;

public class MainActivity extends Activity {

    private Context myContext;

    private WebView mainView;

    public MyLoading myLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myContext = this;
        myLoading = new MyLoading(this);
        myLoading.setContent("正在加载数据...");
        myLoading.setCanceledOnTouchOutside(false);
        myLoading.show();
        initWeb();
        init();
    }

    private void init(){
        MySMSReceiver mySMSReceiver = new MySMSReceiver();

        mySMSReceiver.setOnReceivedMessageListener(new MySMSReceiver.MessageListener() {
            @Override
            public void OnReceived(String message, String sender, String time, long unix_time) {
                String number = Utils.getPayNumber(message);
                if(number.equals(""))return;
                DBHelper.execute("insert into `paylist`(`number`, `date`, `note`) values('" + number + "', " + unix_time + ", '" + message + "')");
                NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                PendingIntent pendingIntent3 = PendingIntent.getActivity(myContext, 0,
                        new Intent(myContext, MainActivity.class), 0);
                String title = "新的账单信息";
                Notification notify3 = new Notification.Builder(myContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker(title)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent3).setNumber(1).build();

                notify3.flags |= Notification.FLAG_AUTO_CANCEL;
                nm.notify(1, notify3);
            }
        });
    }

    private void initWeb(){
        mainView = (WebView) findViewById(R.id.MainView);

        WebSettings settings = mainView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        JavaScriptMethods jsm = new JavaScriptMethods(myContext);
        mainView.addJavascriptInterface(jsm, "JavaScriptMethods");

        mainView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    myLoading.dismiss();
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mainView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mainView.loadUrl("file:///android_asset/www/index.html");
    }

    public boolean onKeyDown(int keyCode, KeyEvent e){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mainView.canGoBack()){
                mainView.goBack();
            }else{
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
            return true;
        }

        return super.onKeyDown(keyCode, e);
    }

}
