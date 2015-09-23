package com.kyf.zhangdan;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by keyf on 2015/9/16.
 */
public class JavaScriptMethods {

    private Context mContext;

    private static List<Map<String, String>> smslist = null;

    private static int CurrentYear = 0;

    private static int CurrentMonth = 0;

    private static int CurrentDay = 0;

    public JavaScriptMethods(Context context) {
        mContext = context;
        Calendar cal = Calendar.getInstance();
        CurrentYear = cal.get(Calendar.YEAR);
        CurrentMonth = cal.get(Calendar.MONTH) + 1;
        CurrentDay = cal.get(Calendar.DAY_OF_MONTH);
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
}