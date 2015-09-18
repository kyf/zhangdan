package com.kyf.zhangdan;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;


/**
 * Created by keyf on 2015/9/18.
 */
public class DBHelper extends SQLiteOpenHelper{

    private SQLiteDatabase db = null;

    private static DBHelper helper;

    public DBHelper(Context context, String dbName){
        super(context, dbName, null, 1);
    }

    public void onCreate(SQLiteDatabase db){
        String sql = "create table `paylist`(`id` integer primary key, `number` varchar(250), `title` varchar(250), `date` int(10), `note` text)";
        db.execSQL(sql);
        sql = "create table `appglobal`(`id` integer primary key, `isfirst` tinyint(1))";
        db.execSQL(sql);
        if(this.db == null){
            this.db = db;
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void onOpen(SQLiteDatabase db){
        if(this.db == null){
            this.db = db;
        }
    }

    public Cursor select(String sql){
        return db.rawQuery(sql, null);
    }

    public void exec(String sql){
        db.execSQL(sql);
    }

    private static DBHelper getHelper(){
        if(DBHelper.helper == null){
            Context context = MyApplication.getContext();
            DBHelper.helper = new DBHelper(context, "zhangdan");
            DBHelper.helper.getWritableDatabase();
        }
        return DBHelper.helper;
    }

    public static Cursor query(String sql){
        return DBHelper.getHelper().select(sql);
    }

    public static void execute(String sql){
        DBHelper.getHelper().exec(sql);
    }
}
