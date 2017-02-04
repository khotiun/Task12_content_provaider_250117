package com.example.mypc.task12_content_provaider_250117.ui.adapters;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.mypc.task12_content_provaider_250117.R;
import com.example.mypc.task12_content_provaider_250117.model.Person;
import com.example.mypc.task12_content_provaider_250117.ui.activities.DetailsPersonsActivity;
import com.example.mypc.task12_content_provaider_250117.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPc on 16.01.2017.
 */

public class FilterAdapter extends ArrayAdapter<Person> implements Filterable {

    private List<Person> persons;
    private List<Person> clonePersonList;
    private Filter filter;
    private Context mContext;



    public FilterAdapter(Context context, ArrayList<Person> persons) {
        super(context, R.layout.item_person, persons);
        this.persons = persons;
        clonePersonList = persons;
        mContext = context;

    }

    @Override
    public int getCount() {//возвращает размер листа
        return persons.size();
    }


    @Override
    public Person getItem(int position) {//возвращает обьект по позиции
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {//возвращает хеш код обьекта по позиции
        return persons.get(position).hashCode();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//возвращает вью
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//берем из контекста сервисный лойаут
        PersonViewHolder holder = new PersonViewHolder();//создаем обьект PersonViewHolder
        View view = convertView; //может взять готовый вью, из списка который не используется
        if (view == null) {//если вью не пришло тогда нужно создать новое
            view = layoutInflater.inflate(R.layout.item_person, parent, false);//создает вью из лойаута
            holder.tvPersonName = (TextView) view.findViewById(R.id.text_view_name);//находим эллементы по id
            holder.tvPersonPhoneNumber = (TextView) view.findViewById(R.id.text_view_phone_number);
            holder.ibEditPerson = (ImageButton) view.findViewById(R.id.image_button_phone_number);
            view.setTag(holder);//закрепляем за вью холдер
        } else
            holder = (PersonViewHolder) view.getTag();
        final Person person = persons.get(position);//присваиваем персону по позиции
        holder.tvPersonName.setText(person.getmName());
        holder.tvPersonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person.selectPerson = person;
                Intent intent = new Intent(mContext, DetailsPersonsActivity.class);
                mContext.startActivity(intent);

            }
        });
        holder.tvPersonPhoneNumber.setText(person.getmPhoneNumber());
        holder.tvPersonPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEtCall = new Intent(Intent.ACTION_DIAL);
                intentEtCall.setData(Uri.parse("tel:" + person.getmPhoneNumber()));
                view.getContext().startActivity(intentEtCall);
            }
        });

        holder.ibEditPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditDialog(person);
            }
        });

        return view;
    }

    private static class PersonViewHolder {//класс нужен для нахождения полей разметки
        public TextView tvPersonName;
        public TextView tvPersonPhoneNumber;
        public ImageButton ibEditPerson;
    }

    public void resetData() {
        persons = clonePersonList;//присваиваем нашему листу клонированый лист
        notifyDataSetChanged();

    }

    public void resetData(ArrayList<Person> list) {
        persons = list;
        notifyDataSetChanged();

    }

    private void openEditDialog(final Person person) {
        final LayoutInflater dialogInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = dialogInflater.inflate(R.layout.dialog_edit, null);

        final TextView tvDialogId = (TextView) root.findViewById(R.id.edit_text_dialog_id);
        final EditText etDialogName = (EditText) root.findViewById(R.id.edit_text_dialog_name);
        final EditText etDialogSurname = (EditText) root.findViewById(R.id.edit_text_dialog_surname);
        final EditText etDialogPhone = (EditText) root.findViewById(R.id.edit_text_dialog_phone_number);
        final EditText etDialogMail = (EditText) root.findViewById(R.id.edit_text_dialog_mail);
        final EditText etDialogSkype = (EditText) root.findViewById(R.id.edit_text_dialog_skype);

        tvDialogId.setText(String.valueOf(person.getMid()));
        etDialogName.setText(String.valueOf(person.getmName()));
        etDialogSurname.setText(String.valueOf(person.getmSurname()));
        etDialogPhone.setText(String.valueOf(person.getmPhoneNumber()));
        etDialogMail.setText(String.valueOf(person.getmMail()));
        etDialogSkype.setText(String.valueOf(person.getmSkype()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);//запуст диалогового окна, параметр контекст откуда будет запущено диалоговое окно
        builder.setView(root);//задаютсе вью как контент для использования
        builder.setMessage("Dialog");//Задает под заглавие диалогового окна

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                person.setMid(Integer.parseInt(etDialogId.getText().toString()));
                person.setmName(etDialogName.getText().toString());
                person.setmSurname(etDialogSurname.getText().toString());
                person.setmPhoneNumber(etDialogPhone.getText().toString());
                person.setmMail(etDialogMail.getText().toString());
                person.setmSkype(etDialogSkype.getText().toString());
                notifyDataSetChanged();//обновляетListView
            }
        });

        builder.setNegativeButton("Deleete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!persons.isEmpty()) {
                            persons.remove(person);

                            sendNotyfication(person);
                            notifyDataSetInvalidated();//метод останавливает адаптер от доступа к данным
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.setTitle("Do you want to delete person?");
                dialog.show();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setCancelable(false);//если пользователь нажмет мимо диалогового окна то ничего не произойдет
        builder.setTitle("Editing person");//задает заглавие диалогового окна
        //запуск диалогового окна
        builder.create();
        builder.show();
    }

    void sendNotyfication(final Person person) {
        int NOTIFI_ID = 101;// указывает уникальность нотификэйшена для данного приложения
        Context context = mContext.getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);//PendingIntent тот интент на который мы будет переходить при нажатии на уведомление
        //PendingIntent.FLAG_CANCEL_CURRENT - если наш PendingIntent ранее уже был создан, то он должен отмениться чтобы сгенерировать новый
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);//получаем обьект NotificationManager
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)//с помощью builder можно построить наше уведомление указать картинку и какието данные в уведомлении
                .setSmallIcon(R.mipmap.ic_launcher)//иконка уведомления
                .setWhen(System.currentTimeMillis())//время прихода уведомления
                .setContentTitle("Deleted contact " + person.getmSurname())//текст в уведомлении
                .setContentText("Phone number " + person.getmPhoneNumber())
                .setAutoCancel(false);//Notyfication - остается висеть после его нажатия
        Notification notification = builder.build();
        nm.notify(NOTIFI_ID, notification);

    }

    @NonNull
    @Override
    public Filter getFilter() {//должен возвращать экземпляр класса Filter
        if (filter == null)
            filter = new PersonFilter();
        return filter;
    }

    private class PersonFilter extends Filter {//выполнять фильтрацию

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {//выполняет фильтрацию
            FilterResults results = new FilterResults();//CharSequence это данные, которые мы фильтруем, последовательность символов
            if (constraint == null || constraint.length() == 0) {
                results.values = clonePersonList;//Какой обьект отрисовать
                results.count = clonePersonList.size();//Сколько таких обьектов отрисовать
            } else {
                List<Person> newPersonList = new ArrayList<>();//создаем новый лист
                for (Person p : persons) {//проходим по нашему листу персон
                    if (p.getmName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {//startsWith -  проверяет, начинается ли строка с указанного префикса
                        newPersonList.add(p);
                    }
                    results.values = newPersonList;
                    results.count = newPersonList.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {//publishResults же вызывается в UI-потоке, чтобы опубликовать результаты на экране
//            if (filterResults.count == 0) {
//                notifyDataSetInvalidated();
//            } else {
//                persons = (List<Person>) filterResults.values;
//                notifyDataSetChanged();
//            }
            if (filterResults.count != 0) {
                persons = (List<Person>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
    public void dropAllPesons(){//удаление всех персон из бд и с адаптера
        persons.clear();
        notifyDataSetChanged();
    }
}
