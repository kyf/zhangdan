package com.kyf.zhangdan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MySMSReceiver extends BroadcastReceiver {

    private static MessageListener mMessageListener;

    public MySMSReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object [] pdus = (Object[]) intent.getExtras().get("pdus");
        for(Object pdu : pdus){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte [])pdu);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String content = smsMessage.getMessageBody();
            long date = smsMessage.getTimestampMillis();
            Date timeDate = new Date(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(timeDate);

            mMessageListener.OnReceived(content, sender, time, date);
        }
    }

    public interface MessageListener {
        public void OnReceived(String message, String sender, String time, long unix_time);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }
}
