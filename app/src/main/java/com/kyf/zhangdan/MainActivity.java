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

public class MainActivity extends Activity {

    private Context myContext;

    private WebView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myContext = this;
        initWeb();
        init();
    }

    private void init(){
        MySMSReceiver mySMSReceiver = new MySMSReceiver();
        Log.e("zhangdan", "onInit");
        mySMSReceiver.setOnReceivedMessageListener(new MySMSReceiver.MessageListener() {
            @Override
            public void OnReceived(String message, String sender, String time, long unix_time) {
                Log.e("zhangdan", "onreceive");
                //DBHelper.execute("insert into `paylist`(`number`, `date`, `note`) values('" + number + "', " + date + ", '" + body + "')");
                NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification n = new Notification();
                n.icon = R.mipmap.ic_launcher;
                n.tickerText = message;
                n.when = System.currentTimeMillis();
                n.flags = Notification.FLAG_AUTO_CANCEL;
                RemoteViews rv = new RemoteViews(getPackageName(), R.layout.notify);
                rv.setTextViewText(R.id.text_content, message);
                n.contentView = rv;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                PendingIntent contentIntent = PendingIntent.getActivity(myContext, R.string.app_name,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                n.contentIntent = contentIntent;
                nm.notify(R.string.app_name, n);
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
