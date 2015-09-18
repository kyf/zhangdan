package com.kyf.zhangdan;

import android.content.Context;
import android.database.Cursor;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keyf on 2015/9/16.
 */
public class JavaScriptMethods {

    private Context mContext;


    public JavaScriptMethods(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void alert(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public String getDataFromNative() {
        String data = "";//Utils.getSMS(mContext);
        return data;
    }

    @JavascriptInterface
    public boolean isFirst() {
        Cursor cursor = DBHelper.query("select `isfirtst` from `appglobal` where id = 1");
        cursor.moveToFirst();
        int count = cursor.getCount();

        if (count == 0) return true;
        int isfirtst = cursor.getInt(cursor.getColumnIndex("isfirtst"));
        if (isfirtst == 1) {
            return false;
        }

        return true;
    }

    @JavascriptInterface
    public void importSMS() {
        List<Map<String, String>> data = Utils.getSMS(mContext);
        //DBHelper.execute("insert into `appglobal`(`isfirst`) values(1)");
    }
}