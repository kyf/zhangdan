package com.kyf.zhangdan;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.kyf.zhangdan.view.MyLoading;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by keyf on 2015/9/16.
 */
public class JavaScriptMethods {

    private Context mContext;

    private static List<Map<String, String>> smslist = null;

    private static int CurrentYear = 0;

    private static int CurrentMonth = 0;

    private static int CurrentDay = 0;

    private MyLoading myLoading;

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1001:
                case 1002:{
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
                case 1003:{
                    //myLoading.hide();
                }
            }
        }
    };


    public JavaScriptMethods(Context context) {
        mContext = context;
        Calendar cal = Calendar.getInstance();
        CurrentYear = cal.get(Calendar.YEAR);
        CurrentMonth = cal.get(Calendar.MONTH) + 1;
        CurrentDay = cal.get(Calendar.DAY_OF_MONTH);
        myLoading = new MyLoading(mContext);
    }

    @JavascriptInterface
    public String getCurrentDate() {
        return CurrentYear + "年" + CurrentMonth + "月";
    }

    @JavascriptInterface
    public int getCurrentYear() {
        return CurrentYear;
    }

    @JavascriptInterface
    public int getCurrentMonth() {
        return CurrentMonth;
    }

    @JavascriptInterface
    public void alert(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void changeDate(int flag) {
        if (flag > 0) {
            CurrentMonth++;
            if (CurrentMonth > 12) {
                CurrentMonth = 1;
                CurrentYear++;
            }
        } else {
            CurrentMonth--;
            if (CurrentMonth < 1) {
                CurrentMonth = 12;
                CurrentYear--;
            }
        }
    }

    @JavascriptInterface
    public String getDataFromNative(long begin, long end) {
        String sql = "select `id`, `number`, `date`, `note`, `title` from `paylist` where `date` >=" + begin + " and `date` < " + end + " order by date desc";

        Cursor data = DBHelper.query(sql);
        data.moveToFirst();
        List<Map<String, String>> ds = new ArrayList<Map<String, String>>();
        int size = data.getCount();
        while (true) {
            if (size == 0) break;
            Map<String, String> it = new HashMap<String, String>();
            it.put("number", data.getString(data.getColumnIndex("number")));
            it.put("id", data.getString(data.getColumnIndex("id")));

            String title = data.getString(data.getColumnIndex("title"));

            if (title == null || title.equals("")) {
                title = "[暂未添加]";
            }

            it.put("title", title);


            long date = data.getLong(data.getColumnIndex("date"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = new Date(date);
            String strDate = dateFormat.format(d);

            it.put("date", strDate);
            it.put("note", data.getString(data.getColumnIndex("note")));
            ds.add(it);

            boolean state = data.moveToNext();
            if (!state) break;
        }

        JSONArray json = new JSONArray(ds);
        return json.toString();
    }

    @JavascriptInterface
    public boolean isFirst() {
        Cursor cursor = DBHelper.query("select `isfirst` from `appglobal` where id = 1");
        cursor.moveToFirst();
        int count = cursor.getCount();

        if (count == 0) return true;
        int isfirst = cursor.getInt(cursor.getColumnIndex("isfirst"));
        if (isfirst == 1) {
            return false;
        }

        return true;
    }


    @JavascriptInterface
    public void deleteDetail(String id){
        String sql = "delete from `paylist` where id = " + id;
        DBHelper.execute(sql);
    }

    @JavascriptInterface
    public String getDetail(String id){
        String sql = "select `title`, `date`, `number`, `note` from `paylist` where id = " + id;
        Cursor cursor = DBHelper.query(sql);
        cursor.moveToFirst();
        if(cursor.getCount() == 0)return "";
        Map<String, String> result = new HashMap<String, String>();
        result.put("title", cursor.getString(cursor.getColumnIndex("title")));

        long date = cursor.getLong(cursor.getColumnIndex("date"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(date);
        String strDate = dateFormat.format(d);
        result.put("date", strDate);

        result.put("number", cursor.getString(cursor.getColumnIndex("number")));
        result.put("note", cursor.getString(cursor.getColumnIndex("note")));

        JSONObject json = new JSONObject(result);
        return json.toString();
    }

    @JavascriptInterface
    public int getSizeSMS() {
        smslist = Utils.getSMS(mContext);
        return smslist.size();
    }


    @JavascriptInterface
    public void importSMS() {
        if (smslist == null) return;
        List<Map<String, String>> data = smslist;

        for (Map<String, String> it : data) {
            String number = it.get("number");
            String date = it.get("date");
            String body = it.get("body");
            DBHelper.execute("insert into `paylist`(`number`, `date`, `note`) values('" + number + "', " + date + ", '" + body + "')");
        }
    }

    @JavascriptInterface
    public void addPay(String title, String date, String number, String note, String id){
        String sql = "insert into `paylist`(`number`, `date`, `note`, `title`) values('" + number + "', " + date + ", '" + note + "', '" + title + "')";
        if(!id.equals("0")){
            sql = "update `paylist` set `number` = '"+number+"', `date` = "+date+", `note` = '"+note+"', `title` = '"+title+"' where id = " + id;
        }

        DBHelper.execute(sql);
    }


    @JavascriptInterface
    public void updateIsFirst() {
        DBHelper.execute("insert into `appglobal`(`isfirst`) values(1)");
    }

    @JavascriptInterface
    public void sync(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                long begin , end ;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                calendar.set(getCurrentYear(), getCurrentMonth() - 1, 1, 0, 0, 0);
                begin = calendar.getTimeInMillis();
                calendar.set(getCurrentYear(), getCurrentMonth(), 1, 0, 0, 0);
                end = calendar.getTimeInMillis();
                String body = getDataFromNative(begin, end);
                String title = "[" + getCurrentYear() + "年" + getCurrentMonth() + "月]对账单";
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://im2.6renyou.com:2225/sync");
                ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("title", title));
                nvp.add(new BasicNameValuePair("body", body));
                Message message = Message.obtain();
                try {
                    post.setEntity(new UrlEncodedFormEntity(nvp));
                    HttpResponse response = client.execute(post);
                    String content = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(content);
                    String msg = jsonObject.getString("msg");
                    message.what = 1001;
                    message.obj = msg;
                }catch(Exception e){
                    message.what = 1002;
                    message.obj = e.getMessage();
                }finally {
                    myHandler.sendMessage(message);
                }
                client.getConnectionManager().shutdown();
            }
        }).start();
    }

    @JavascriptInterface
    public void loading(String msg){
        //myLoading.setContent(msg);
        //myLoading.setCanceledOnTouchOutside(false);
        //myLoading.show();
    }

    @JavascriptInterface
    public void dismiss(){
        if(myLoading != null && myLoading.isShowing()){
            //myHandler.sendEmptyMessage(1003);
        }
    }
}