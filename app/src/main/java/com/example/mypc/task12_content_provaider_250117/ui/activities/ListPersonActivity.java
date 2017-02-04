package com.example.mypc.task12_content_provaider_250117.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mypc.task12_content_provaider_250117.R;
import com.example.mypc.task12_content_provaider_250117.database.DBContentProvider;
import com.example.mypc.task12_content_provaider_250117.database.PersonContract;
import com.example.mypc.task12_content_provaider_250117.model.Person;
import com.example.mypc.task12_content_provaider_250117.ui.adapters.FilterAdapter;

import java.util.ArrayList;

public class ListPersonActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Person> persons;
    EditText edSearch;
    FilterAdapter filterAdapter;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_person);
        lv = (ListView) findViewById(R.id.lv);
        persons = new ArrayList<>();

        String selection = null;
        String sortOrder = null;

        Cursor cursor = getContentResolver().query(DBContentProvider.PERSONS_CONTENT_URI, null, selection, null, sortOrder);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(PersonContract.KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME));
            String surname = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME));
            String phone = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE));
            String mail = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL));
            String skype = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE));
            persons.add(new Person(id, name, surname, phone, mail, skype));
        }

        filterAdapter = new FilterAdapter(this, persons);
        lv.setAdapter(filterAdapter);
        edSearch = (EditText) findViewById(R.id.text_view_list_select);
        edSearch.addTextChangedListener(new TextWatcher() {//addTextChangedListener - отследить изминения текста внутри компонента
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {//beforeTextChanged(CharSequence s, int start, int count, int after) -
                // метод вызывается до изменений, чтобы уведомить нас, что в строке s, начиная с позиции start вот-вот будут заменены count символов, новыми after символами.
                // Изменение текста s в этом методе является ошибкой.
//                if (count < before) {
//                    filterAdapter.resetData();
//                }
//                filterAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//onTextChanged(CharSequence s, int start, int before, int count) - метод вызывается,
                // чтобы уведомить нас, что в строке s, начиная с позиции start, только что заменены after символов, новыми count символами. Изменение текста s в этом методе является ошибкой.
                filterAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {//afterTextChanged(Editable s) - метод вызывается, чтобы уведомить нас, что где-то в строке s, текст был изменен.
                // В этом методе можно вносить изменения в текст s, но будьте осторожны, чтобы не зациклиться, потому что любые изменения в s рекурсивно вызовут этот же метод.

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_get_id_person:
                getPersonId();
                break;
            case R.id.action_delete_all_persons:
                filterAdapter.dropAllPesons();
                break;
            case R.id.action_clouse_list_person:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void getPersonId() {
        final LayoutInflater getPersonDialogInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewPerson = getPersonDialogInflater.inflate(R.layout.dialog_list_get_person, null);
        final EditText etDialogGetPersonId = (EditText) viewPerson.findViewById(R.id.edit_text_dialog_list_id);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);//запуст диалогового окна, параметр контекст откуда будет запущено диалоговое окно
        builder.setView(viewPerson);//задаютсе вью как контент для использования
        builder.setMessage("Enter id person:");//Задает под заглавие диалогового окна
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                filterAdapter.resetData(crudsqLite.getPerson(Integer.parseInt(etDialogGetPersonId.getText().toString())));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
