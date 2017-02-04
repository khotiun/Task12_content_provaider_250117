package com.example.mypc.task12_content_provaider_250117;

import android.net.Uri;

import com.example.mypc.task12_content_provaider_250117.database.PersonContract;

/**
 * Created by MyPc on 20.01.2017.
 */

public class Config {

    public static final String DB_NAME = "Person Data Base2";

    public static final String TABLE_PERSON = "person";

    public static final String COMMAND_CREATE = "create table "
            + TABLE_PERSON + " ("
            + PersonContract.KEY_ID + " INTEGER PRIMARY KEY, "
            + PersonContract.KEY_NAME +" TEXT, "
            + PersonContract.KEY_SURNAME + " TEXT, "
            + PersonContract.KEY_PHONE + " TEXT, "
            + PersonContract.KEY_MAIL + " TEXT, "
            + PersonContract.KEY_SKYPE + " TEXT"
            + ");";

    public static final String COMMAND_DELETE = "DROP TABLE IF EXISTS " + TABLE_PERSON;
    public static final int DB_VERSION = 2201017;
}
