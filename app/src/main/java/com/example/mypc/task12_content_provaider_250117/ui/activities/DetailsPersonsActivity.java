package com.example.mypc.task12_content_provaider_250117.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mypc.task12_content_provaider_250117.R;
import com.example.mypc.task12_content_provaider_250117.model.Person;

import java.util.List;

public class DetailsPersonsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvPersonId;
    TextView tvPersonName;
    TextView tvPersonSurname;
    TextView tvPersonPhone;
    TextView tvPersonMail;
    TextView tvPersonSkype;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        person = Person.selectPerson;

        tvPersonId = (TextView) findViewById(R.id.text_view_person_id);
        tvPersonName = (TextView) findViewById(R.id.text_view_person_name);
        tvPersonSurname = (TextView) findViewById(R.id.text_view_person_surname);
        tvPersonPhone = (TextView) findViewById(R.id.text_view_person_phone_number);
        tvPersonMail = (TextView) findViewById(R.id.text_view_person_mail);
        tvPersonSkype = (TextView) findViewById(R.id.text_view_person_skype);

        tvPersonId.setText(String.valueOf(person.getMid()));
        tvPersonName.setText(String.valueOf(person.getmName()));
        tvPersonSurname.setText(String.valueOf(person.getmSurname()));
        tvPersonPhone.setText(String.valueOf(person.getmPhoneNumber()));
        tvPersonMail.setText(String.valueOf(person.getmMail()));
        tvPersonSkype.setText(String.valueOf(person.getmSkype()));
        tvPersonPhone.setOnClickListener(this);
        tvPersonMail.setOnClickListener(this);
        tvPersonSkype.setOnClickListener(this);
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

                } else if (isIntentAvailable( view.getContext(), intentTvSkype.setClassName(SKYPE_PATH_GENERAL, SKYPE_PATH_OLD))) {//установлена старая версия скайпа
                            view.getContext().startActivity(intentTvSkype);
                } else {//скайп не установлен
                    Toast.makeText(view.getContext(), "Приложение Skype не установлено.", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    //Этот метод проверяет в системе наличие приложения, способного ответить на конкретный интент.
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}