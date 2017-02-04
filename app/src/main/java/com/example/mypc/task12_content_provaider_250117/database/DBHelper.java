package com.example.mypc.task12_content_provaider_250117.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mypc.task12_content_provaider_250117.Config;

/**
 * Created by MyPc on 25.01.2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context) {
        super(context, Config.DB_NAME, null, Config.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {//Запускается когда создается база данных
        sqLiteDatabase.execSQL(Config.COMMAND_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {//Сработает при изминении базы данных
        sqLiteDatabase.execSQL(Config.COMMAND_DELETE);//Запрос к базе данных на уничтожение таблице
        this.onCreate(sqLiteDatabase);//Создание новой версии таблици с обнавленной структурой
    }
}
