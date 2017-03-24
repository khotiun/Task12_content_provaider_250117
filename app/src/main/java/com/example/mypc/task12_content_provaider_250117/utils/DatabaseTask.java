package com.example.mypc.task12_content_provaider_250117.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.mypc.task12_content_provaider_250117.database.DBContentProvider;
import com.example.mypc.task12_content_provaider_250117.database.PersonContract;

/**
 * Created by MyPc on 12.02.2017.
 */

public class DatabaseTask extends AsyncTask<Object, Void, Void> {

    private final Context mContext;
    public static final int INSERT = 0;
    public static final int DELETE_BY_ID = 1;
    public static final int UPDATE = 2;
    public static final int QUERY_BY_ID = 3;
    public static final int DELETE = 4;

    public DatabaseTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Object... params) {

        ContentResolver contentResolver = mContext.getContentResolver();
        ContentValues values;
        Integer id;

        switch ((Integer) params[0]){
            case INSERT:
                values = (ContentValues) params[1];
                contentResolver.insert(DBContentProvider.PERSONS_CONTENT_URI, values);
                break;
            case DELETE_BY_ID:
                values = (ContentValues) params[1];
                contentResolver.delete(DBContentProvider.PERSONS_CONTENT_URI, PersonContract.KEY_ID + " = ?",
                        new String[] {values.getAsString(PersonContract.KEY_ID)});
                break;
            case UPDATE:
                id = (Integer) params[0];
                values = (ContentValues) params[1];
                contentResolver.update(DBContentProvider.PERSONS_CONTENT_URI.buildUpon().
                        appendPath(String.valueOf(id)).build(), values, null, null);
                contentResolver.notifyChange(DBContentProvider.PERSONS_CONTENT_URI, null);
                break;
            case QUERY_BY_ID:
                id = (Integer) params[0];
                contentResolver.query(DBContentProvider.PERSONS_CONTENT_URI.buildUpon().
                        appendPath(String.valueOf(id)).build(), null, null, null, null);
                break;
            case DELETE:
                contentResolver.delete(DBContentProvider.PERSONS_CONTENT_URI, null, null);
                break;
            default:
                break;
        }

        return null;
    }
}
