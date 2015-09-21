package com.kyf.zhangdan;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by keyf on 2015/9/16.
 */
public class Utils {

    public static List<Map<String, String>> getSMS(Context context) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Uri SMS_INBOX = Uri.parse("content://sms/");

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"body", "_id", "address", "person", "date"};
        String where = " type = 1 and address = '95555' ";
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");

        if (null == cur) return result;

        while (cur.moveToNext()) {
            long date = cur.getLong(cur.getColumnIndex("date"));
            String body = cur.getString(cur.getColumnIndex("body"));

            Map<String, String> item = new HashMap<String, String>();

            Pattern pattern = Pattern.compile("^您账户[0-9]{4}于[0-9]{2}月[0-9]{2}日[0-9]{2}:[0-9]{2}.*人民币([0-9\\.]+)");
            Matcher matcher = pattern.matcher(body);

            if (matcher.find()) {
                String paynumber = matcher.group(1);
                item.put("date", date + "");
                item.put("number", paynumber);
                item.put("body", body);
                result.add(item);
            }
        }



        return result;
    }
}
