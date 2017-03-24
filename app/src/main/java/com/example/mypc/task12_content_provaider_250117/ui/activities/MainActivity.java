package com.example.mypc.task12_content_provaider_250117.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mypc.task12_content_provaider_250117.R;
import com.example.mypc.task12_content_provaider_250117.database.PersonContract;
import com.example.mypc.task12_content_provaider_250117.utils.DatabaseTask;
import com.example.mypc.task12_content_provaider_250117.utils.Utility;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName;
    EditText etSurname;
    EditText etPhone;
    EditText etMail;
    EditText etSkype;
    ImageView ivPerson;
    Button btnSave, btnCamera;
    final int REQUEST_CODE_PHOTO = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private final String TAG = "Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.edit_text_name);
        etSurname = (EditText) findViewById(R.id.edit_text_surname);
        etPhone = (EditText) findViewById(R.id.edit_text_phone_number);
        etMail = (EditText) findViewById(R.id.edit_text_mail);
        etSkype = (EditText) findViewById(R.id.edit_text_skype);
        btnSave = (Button) findViewById(R.id.btn_main_save);
        btnCamera = (Button) findViewById(R.id.btn_main_camera);
        ivPerson = (ImageView) findViewById(R.id.image_view_main_profile);
        btnCamera.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        Utility utility = new Utility();
        ivPerson.setImageBitmap(utility.loadPhoto(this));
        Log.d(TAG, " onCreate");
    }

    @Override
    //Создание тул бара
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//элемент тул бара
        int id = item.getItemId();
        if (id == R.id.action_list_activity) {
            folowToListActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void folowToListActivity() {
        Intent intent = new Intent(MainActivity.this, ListPersonActivity.class);
        clearText();
        startActivity(intent);
    }

    public void clearText() {
        etName.setText("");
        etSurname.setText("");
        etPhone.setText("");
        etMail.setText("");
        etSkype.setText("");
        ivPerson.setImageDrawable(null);//задает пустую картинку
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case (R.id.btn_main_save):
                ContentValues contentValues = new ContentValues();//Используется для добавления новых строк в таблицу
                contentValues.put(PersonContract.KEY_NAME, etName.getText().toString());
                contentValues.put(PersonContract.KEY_SURNAME, etSurname.getText().toString());
                contentValues.put(PersonContract.KEY_PHONE, etPhone.getText().toString());
                contentValues.put(PersonContract.KEY_MAIL, etMail.getText().toString());
                contentValues.put(PersonContract.KEY_SKYPE, etSkype.getText().toString());
                if (ivPerson.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) ivPerson.getDrawable()).getBitmap();//получаем Bitmap из изображения
                    String imageEncodeString = Utility.encodeToBase64(bitmap);//кодируем bitmap в строку
                    contentValues.put(PersonContract.KEY_PROFILE, imageEncodeString);
                }
//                getContentResolver().insert(DBContentProvider.PERSONS_CONTENT_URI, contentValues);
                DatabaseTask databaseTask = new DatabaseTask(this);
                databaseTask.execute(DatabaseTask.INSERT, contentValues);
                folowToListActivity();
                break;
            case (R.id.btn_main_camera):
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//вызов камеры
                    startActivityForResult(intent, REQUEST_CODE_PHOTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Bundle bnd1 = intent.getExtras();
        if (bnd1 != null) {
            Object obj = intent.getExtras().get("data");//забираем со второй активности значение по ключу
            if (obj instanceof Bitmap) {//instanceof - проверка, является ли данный обьект Bitmap
                Bitmap bitmap = (Bitmap) obj;//явное привидение типов
                ivPerson.setImageBitmap(bitmap);//задаем image view картинку
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, " onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, " onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility utility = new Utility();
        Bitmap bitmap = ((BitmapDrawable) ivPerson.getDrawable()).getBitmap();//получаем Bitmap из изображения
        String strPhoto = Utility.encodeToBase64(bitmap);//передаем Bitmap и получаем из нее строку
        utility.savePhoto(this, strPhoto);
        Log.d(TAG, " onDestroy");
    }
}

