package com.example.mypc.task12_content_provaider_250117.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.mypc.task12_content_provaider_250117.Config;

import java.io.FileNotFoundException;


/**
 * Created by MyPc on 25.01.2017.
 */

public class DBContentProvider extends ContentProvider {

    //    content://<authority>/<path>/<id>.
//    Например, возьмем Uri - content://ru.startandroid.provider.AdressBook/contacts/7 и разложим на части:
//    content:// - это стандартное начало для адреса провайдера.
//    ru.startandroid.provider.AdressBook– это authority. Определяет провайдера (если проводить аналогию с базой данных, то это имя базы).
//    contacts – это path. Какие данные от провайдера нужны (таблица).
//            7 – это ID. Какая конкретно запись нужна (ID записи)
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);//собирает и дробит нашу Uri
    static final String AUTHORITY = "com.example.mypc.testcontentprovider";
    static final String PERSON_PATH = "person";
    //Общий Uri
    public static final Uri PERSONS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PERSON_PATH);

    private static final int URI_PERSON = 1;
    private static final int URI_PERSON_ID = 2;//URI - с указанием id
    //Символ решётки (#) отвечает за число, а символ звёздочки (*) за строку.
    //Типы данных
    //Набор строк
    static final String PERSON_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PERSON_PATH;
    //основной тип MIME для коллекции элементов, возвращаемый командой cursor в Android, всегда должен иметь вид vnd.android.cursor.dir
    //одна строка
    static final String PERSON_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PERSON_PATH;
    // основной тип MIME для одиночного элемента, находимый командой cursor в Android, - вид vnd.android.cursor.item
    private DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        //В контент провайдер будут поступать URI и мы их будем здавать в uriMatcher на проверку, если URI будет подходить под комбинацию
        //AUTHORITY и PATH ранее дабавленные в addURI то uriMatcher вернет константу из того же набора. Главный смысл uriMatcher в том что он определит
        //какой URI к нам пришел общий или с айди
        uriMatcher.addURI(AUTHORITY, PERSON_PATH, URI_PERSON);
        uriMatcher.addURI(AUTHORITY, PERSON_PATH + "/#", URI_PERSON_ID);///# - соответствует кокретной записи (ряд таблицы)

        dbHelper = new DBHelper(getContext());
        Toast.makeText(getContext(), "Call provider", Toast.LENGTH_LONG).show();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName;
        String id;

        switch (uriMatcher.match(uri)) {
            //с помощью switch создаётся ветвление - хотим ли мы получить информацию о всей таблице (код 1) или к конкретному ряду (код 2).
            case URI_PERSON://Общий Uri
                //если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {//если сортировка не указана, то делаем сортировку по имени
                    sortOrder = PersonContract.KEY_NAME + " ASC";
                }
                tableName = Config.TABLE_PERSON;
                break;
            case URI_PERSON_ID://URI c ID
                id = uri.getLastPathSegment();//извлекаем id из URI
                //добавляем ID к условиям выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = PersonContract.KEY_ID + " = " + id;
                } else {
                    selection = selection + " AND" + PersonContract.KEY_ID + " = " + id;
                }
                tableName = Config.TABLE_PERSON;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI:" + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        //просим ContentResolver уведомлять этот курсор
        //об изменениях данных в PERSONS_CONTENT_URI;
        cursor.setNotificationUri(getContext().getContentResolver(), PERSONS_CONTENT_URI);//регестрируем курсор что бы он получал уведомления
        //кокда будут менятся данные соответствующие общему URI
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_PERSON:
                return PERSON_CONTENT_TYPE;
            case URI_PERSON_ID:
                return PERSON_CONTENT_ITEM_TYPE;
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        ContentValues вставляет данные в нужные колонки таблицы
        String tableName;
        Uri resultUri;
        if (uriMatcher.match(uri) == URI_PERSON) {//проверяем что нам пришел наш общий URI
            tableName = Config.TABLE_PERSON;
            resultUri = PERSONS_CONTENT_URI;
        } else
            throw new IllegalArgumentException("Wrong Uri: " + uri);

        db = dbHelper.getWritableDatabase();
        String nullColumn = "";
        long rowID = db.insert(tableName, null, values);//вставляем данные в таблицу получаем ID
        //Log.d("TAGT", rowID + "");
        resultUri = ContentUris.withAppendedId(resultUri, rowID);//withAppendedId - метод сложения строк, rowID - добавляем к общему URI и получае URI с ID
        //уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);//проверит зарегестрирован ли слушатель на этот URI, увидит что мы регестрировали курсор и данные обновились
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id;
        String tableName;
        switch (uriMatcher.match(uri)) {
            case URI_PERSON:
                tableName = Config.TABLE_PERSON;
                break;
            case URI_PERSON_ID:
                tableName = Config.TABLE_PERSON;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PersonContract.KEY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id;
        String tableName;
        switch (uriMatcher.match(uri)) {
            case URI_PERSON:
                tableName = Config.TABLE_PERSON;
                break;
            case URI_PERSON_ID:
                tableName = Config.TABLE_PERSON;
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PersonContract.KEY_ID + " = " + id;
                } else {
                    selection = selection + " AND " + PersonContract.KEY_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Nullable
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return super.openFile(uri, mode);
    }
}
