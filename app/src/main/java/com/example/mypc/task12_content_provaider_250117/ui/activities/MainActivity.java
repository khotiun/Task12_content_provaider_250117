package com.example.mypc.task12_content_provaider_250117.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mypc.task12_content_provaider_250117.R;
import com.example.mypc.task12_content_provaider_250117.database.DBContentProvider;
import com.example.mypc.task12_content_provaider_250117.database.PersonContract;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName;
    EditText etSurname;
    EditText etPhone;
    EditText etMail;
    EditText etSkype;
    Button btnSave;

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
        btnSave.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_main_save:
                ContentValues contentValues = new ContentValues();
                contentValues.put(PersonContract.KEY_NAME, etName.getText().toString());
                contentValues.put(PersonContract.KEY_SURNAME, etSurname.getText().toString());
                contentValues.put(PersonContract.KEY_PHONE, etPhone.getText().toString());
                contentValues.put(PersonContract.KEY_MAIL, etMail.getText().toString());
                contentValues.put(PersonContract.KEY_SKYPE, etSkype.getText().toString());
                getContentResolver().insert(DBContentProvider.PERSONS_CONTENT_URI, contentValues);
                folowToListActivity();
                break;
        }

    }

}
