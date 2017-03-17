package com.example.mypc.task12_content_provaider_250117.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.task12_content_provaider_250117.R;
import com.example.mypc.task12_content_provaider_250117.database.DBContentProvider;
import com.example.mypc.task12_content_provaider_250117.database.PersonContract;
import com.example.mypc.task12_content_provaider_250117.model.Person;
import com.example.mypc.task12_content_provaider_250117.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DetailsPersonsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    TextView tvPersonId;
    TextView tvPersonName;
    TextView tvPersonSurname;
    TextView tvPersonPhone;
    TextView tvPersonMail;
    TextView tvPersonSkype;
    ImageView ivProfile;
    ImageButton ibTakePhoto;
    Button btnPersonSavePhoto;
    Person person;
    public int idPerson;

    private final int REQUEST_CAMERA = 0, SELECT_FILE = 1;//задаем REQUEST CODE для того что бы распознать какой интент нам вернется
    private String userChooserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getLoaderManager().initLoader(0, null, this);//регистрация loader
        //LoaderManager управляет жизненным циклом загрузчика автоматически
        tvPersonId = (TextView) findViewById(R.id.text_view_person_id);
        tvPersonName = (TextView) findViewById(R.id.text_view_person_name);
        tvPersonSurname = (TextView) findViewById(R.id.text_view_person_surname);
        tvPersonPhone = (TextView) findViewById(R.id.text_view_person_phone_number);
        tvPersonMail = (TextView) findViewById(R.id.text_view_person_mail);
        tvPersonSkype = (TextView) findViewById(R.id.text_view_person_skype);
        ivProfile = (ImageView) findViewById(R.id.image_view_detail_person);
        ibTakePhoto = (ImageButton) findViewById(R.id.image_btn_edit_person_photo);
        ibTakePhoto.setOnClickListener(this);
        btnPersonSavePhoto = (Button) findViewById(R.id.btn_person_save_photo);
        btnPersonSavePhoto.setOnClickListener(this);


//
//                person = Person.selectPerson;
//
//        tvPersonId.setText(String.valueOf(person.getMid()));
//        tvPersonName.setText(String.valueOf(person.getmName()));
//        tvPersonSurname.setText(String.valueOf(person.getmSurname()));
//        tvPersonPhone.setText(String.valueOf(person.getmPhoneNumber()));
//        tvPersonMail.setText(String.valueOf(person.getmMail()));
//        tvPersonSkype.setText(String.valueOf(person.getmSkype()));
//        ivProfile.setImageBitmap(Utility.decodeBase64(person.getmProfile()));

        tvPersonPhone.setOnClickListener(this);
        tvPersonMail.setOnClickListener(this);
        tvPersonSkype.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);//перезагрузка Loader
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.text_view_person_phone_number:
                Intent intentEtCall = new Intent(Intent.ACTION_DIAL);
                intentEtCall.setData(Uri.parse("tel:" + person.getmPhoneNumber()));
                view.getContext().startActivity(intentEtCall);
                break;
            case R.id.text_view_person_mail:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + person.getmMail()));
                view.getContext().startActivity(emailIntent);
                break;
            case R.id.text_view_person_skype:
                final String SKYPE_PATH_GENERAL = "com.skype.raider";//установлен ли скайп на устройство
                final String SKYPE_PATH_OLD = "com.skype.raider.contactsync.ContactSkypeOutCallStartActivity";//старая версия скайпа
                final String SKYPE_PATH_NEW = "com.skype.raider.Main";//новая версия скайпа
                final Intent intentTvSkype = new Intent("android.intent.action.VIEW");//не явный вызов интента для звонка в скайп
                intentTvSkype.setData(Uri.parse("skype:" + person.getmSkype()));

                if (isIntentAvailable(view.getContext(), intentTvSkype.setClassName(SKYPE_PATH_GENERAL, SKYPE_PATH_NEW))) {//установлена новая версия скайпа
                    view.getContext().startActivity(intentTvSkype);

                } else if (isIntentAvailable(view.getContext(), intentTvSkype.setClassName(SKYPE_PATH_GENERAL, SKYPE_PATH_OLD))) {//установлена старая версия скайпа
                    view.getContext().startActivity(intentTvSkype);
                } else {//скайп не установлен
                    Toast.makeText(view.getContext(), "Приложение Skype не установлено.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.image_btn_edit_person_photo:
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(PersonContract.KEY_PROFILE, (byte[]) null);
//                getContentResolver().update(Uri.parse(DBContentProvider.PERSONS_CONTENT_URI + "/" + person.getMid()), contentValues, null, null);
                selectImage();
                break;
            case R.id.btn_person_save_photo:
//                ContentValues content = new ContentValues();
//                Bitmap bitmap = ((BitmapDrawable) ivProfile.getDrawable()).getBitmap();
//                content.put(PersonContract.KEY_PROFILE, Utility.encodeToBase64(bitmap));
//                getContentResolver().update(Uri.parse(DBContentProvider.PERSONS_CONTENT_URI + "/" + person.getMid()), content, null, null);
                break;

        }
    }

    // Этот метод проверяет в системе наличие приложения, способного ответить на конкретный интент.
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {//создает Loader

        String selection = null;
        String sortOrder = null;
        Intent intent = this.getIntent();//получаем интент с бандлом в котором наш id персоны
        Bundle bundleAdapter = intent.getExtras();
        if (bundleAdapter != null) {
            idPerson = bundleAdapter.getInt("IdPersonToDetailActivity");
            selection = "Id = " + idPerson;
        }
        CursorLoader loader = new CursorLoader(this, DBContentProvider.PERSONS_CONTENT_URI, null, selection, null, sortOrder);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {//результат работу лоадер, новый курсор с данными
        cursor.moveToNext();
        String idPerson = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_ID));
        tvPersonId.setText(idPerson);
        String namePerson = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME));
        tvPersonName.setText(namePerson);
        String surnamePerson = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME));
        tvPersonSurname.setText(surnamePerson);
        String phonePerson = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE));
        tvPersonPhone.setText(phonePerson);
        String mailPerson = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL));
        tvPersonMail.setText(mailPerson);
        String skypePerson = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE));
        tvPersonSkype.setText(skypePerson);
        String photoPerson = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PROFILE));
        if (photoPerson != null) {
            ivProfile.setImageBitmap(Utility.decodeBase64(photoPerson));
        }
        person = new Person(Integer.parseInt(idPerson), namePerson, surnamePerson, phonePerson, mailPerson, skypePerson, photoPerson);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};//создаем массив айтемов для выбора необходимого
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");//задаем название диалога
        builder.setItems(items, new DialogInterface.OnClickListener() {//задаем диалогу наши items и вешаем на них слушателя
            @Override
            public void onClick(DialogInterface dialog, int item) {//если нажат ("Take Photo"), тогда нам приходит 0 item
                if (items[item].equals("Take Photo")) {//сравниваем какой item нам пришел
                    userChooserTask = "Take Photo";
                    cameraIntent();//вызываем метод, который обрабатывает интент камеры
                } else if (items[item].equals("Choose from Library")) {
                    userChooserTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//стандартный интент для вызова камеры
        startActivityForResult(intent, REQUEST_CAMERA);//вызываем intent, и передаем REQUEST_CAMERA - что бы
        // могли распознать результат который нам прийдет
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");//задаем тип отображения файлов(все файлы image)
        intent.setAction(Intent.ACTION_GET_CONTENT);//интент для получение файлов выборка по image/*
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("WER", "bitmap");
        if (resultCode == Activity.RESULT_OK) {//если действие с вызваной активности прошло успешно и активнасть возвращает нам результат
            if (requestCode == SELECT_FILE) {//определем чем равен пришелший requestCode
                onSelectFromGalleryResult(data);//если равен 1, тогда запускаем этот метод и передаем ему intent
            } else if (requestCode == REQUEST_CAMERA) {
                onGalleryImageResult(data);
                Bundle bndl = data.getExtras();
                if (bndl != null) {
                    Log.d("WER", "11111111");
                    Object obj = data.getExtras().get("data");
                    if (obj instanceof Bitmap) {
                        Bitmap bitmap = (Bitmap) obj;
                        Log.d("WER", "bitmap" + bitmap.getWidth() + " x " + bitmap.getHeight());
                        String imageIncodedString = Utility.encodeToBase64(bitmap);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(PersonContract.KEY_PROFILE, imageIncodedString);
                        getContentResolver().update(Uri.parse(DBContentProvider.PERSONS_CONTENT_URI + "/" + idPerson), contentValues, null, null);
                    }
                }
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                // получаем системный контент провайдер и передаем ему uri - нашего интента
                Log.d("WER", "" + data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        String imageIncodedString = Utility.encodeToBase64(bm);
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PersonContract.KEY_PROFILE, imageIncodedString);
//        getContentResolver().update(Uri.parse(DBContentProvider.PERSONS_CONTENT_URI + "/" + idPerson), contentValues, null, null);
        ivProfile.setImageBitmap(bm);
    }

    private void onGalleryImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");//метод data.getExtras() - возвращает бандл, и у него мы вызываем
        //get - который по ключу возвращаем тип Object и его мы приводим к bitmap
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();//Инициализируем выходящий поток ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);//пишем в него сжатый Bitmap, используя метод compress()
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");//указывается путь к файлу и имя файла

        FileOutputStream fo;
        try {
            destination.createNewFile();//содание файла
            fo = new FileOutputStream(destination);//передача файла в поток
            fo.write(bytes.toByteArray());//записать в поток масива байтов
            fo.close();//закрыть поток
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivProfile.setImageBitmap(bitmap);
    }
}