package com.example.mypc.task12_content_provaider_250117.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by MyPc on 06.02.2017.
 */

public class Utility {
    public static final String PREFS_NAME = "SAVE_LOAD";
    public static final String SAVE = "SAVE";
    public static final String LOAD = "LOAD";
    SharedPreferences sharedPreferences;

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//создаем байтовый массив поток вывода
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//сжимаем bitmap и преобраем в байтовый поток вывода
        byte[] b = baos.toByteArray();//преобразовываем поток в массив байтов
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);//преобразовываем массив в строку
        //Base64 - число символов алфавитного кадирования
        Log.d("Image was encode:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }

    public void savePhoto(Context context, String mProfile) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();//Позволяет работать с данными, считывать и записывать их
        ed.putString(SAVE, mProfile);
        ed.commit();
    }

    public Bitmap loadPhoto (Context context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savePhoto = sharedPreferences.getString(SAVE, "");
        Bitmap bitmap = decodeBase64(savePhoto);
        return bitmap;
    }


}
