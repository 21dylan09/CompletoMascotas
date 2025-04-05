package com.example.loginsignup.actividadesDueño;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.loginsignup.R;

import java.text.SimpleDateFormat;
import java.util.*;

public class RecordatoriosAlimentacion extends AppCompatActivity {

    private int selectedHour = 8;
    private int selectedMinute = 0;
    private int alarmIdCounter = 1;

    private ArrayList<AlarmData> alarmList = new ArrayList<>();
    private AlarmAdapter alarmAdapter;
    private ListView alarmListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios_alimentacion);

        Button setTimeButton = findViewById(R.id.setTimeButton);
        Button setAlarmButton = findViewById(R.id.setAlarmButton);
        TextView timeTextView = findViewById(R.id.timeTextView);
        alarmListView = findViewById(R.id.alarmListView);

        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!isExactAlarmPermissionGranted()) {
                requestExactAlarmPermission();
            }
        }

        alarmAdapter = new AlarmAdapter();
        alarmListView.setAdapter(alarmAdapter);

        setTimeButton.setOnClickListener(v -> showTimePickerDialog(timeTextView));

        setAlarmButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
            calendar.set(Calendar.SECOND, 0);

            int alarmId = alarmIdCounter++;
            String formattedTime = formatTime(selectedHour, selectedMinute);

            setAlarm(calendar, alarmId, formattedTime);

            alarmList.add(new AlarmData(formattedTime, alarmId, calendar));
            alarmAdapter.notifyDataSetChanged();

            timeTextView.setText(formattedTime);
        });
    }

    private void showTimePickerDialog(TextView timeTextView) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedHour = hourOfDay;
            selectedMinute = minute;
            timeTextView.setText(formatTime(selectedHour, selectedMinute));
        }, selectedHour, selectedMinute, false);
        timePickerDialog.show();
    }

    private String formatTime(int hour, int minute) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return format.format(calendar.getTime());
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(Calendar calendar, int alarmId, String timeLabel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isExactAlarmPermissionGranted()) return;

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm_time", timeLabel);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    private void cancelAlarm(int alarmId) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private boolean isExactAlarmPermissionGranted() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                ContextCompat.checkSelfPermission(this, "android.permission.SCHEDULE_EXACT_ALARM") == PackageManager.PERMISSION_GRANTED;
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "alarma_channel",
                    "Alarma Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Canal de notificación para alarmas");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String alarmTime = intent.getStringExtra("alarm_time");

            Notification notification = new NotificationCompat.Builder(context, "alarma_channel")
                    .setContentTitle("¡Hora de Alimentar a tu Mascota!")
                    .setContentText("Ya son las: " + alarmTime +" alimentalo")
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(new Random().nextInt(10000), notification);
            }
        }
    }

    // 🔧 Clase para representar una alarma
    class AlarmData {
        String time;
        int id;
        Calendar calendar;

        AlarmData(String time, int id, Calendar calendar) {
            this.time = time;
            this.id = id;
            this.calendar = calendar;
        }
    }

    // 📋 Adaptador para mostrar cada alarma con botones
    class AlarmAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return alarmList.size();
        }

        @Override
        public Object getItem(int i) {
            return alarmList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return alarmList.get(i).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(RecordatoriosAlimentacion.this).inflate(R.layout.item_alarm, parent, false);

            TextView timeView = view.findViewById(R.id.timeTextViewItem);
            Button editButton = view.findViewById(R.id.editButton);
            Button deleteButton = view.findViewById(R.id.deleteButton);

            AlarmData alarm = alarmList.get(position);
            timeView.setText(alarm.time);

            editButton.setOnClickListener(v -> {
                showTimePickerDialog(newTime -> {
                    alarm.calendar.set(Calendar.HOUR_OF_DAY, newTime.get(Calendar.HOUR_OF_DAY));
                    alarm.calendar.set(Calendar.MINUTE, newTime.get(Calendar.MINUTE));
                    alarm.time = formatTime(newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE));
                    setAlarm(alarm.calendar, alarm.id, alarm.time);
                    alarmAdapter.notifyDataSetChanged();
                });
            });

            deleteButton.setOnClickListener(v -> {
                cancelAlarm(alarm.id);
                alarmList.remove(position);
                notifyDataSetChanged();
            });

            return view;
        }

        private void showTimePickerDialog(OnTimeSelectedListener listener) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(RecordatoriosAlimentacion.this, (view, hourOfDay, minute) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                listener.onTimeSelected(calendar);
            }, selectedHour, selectedMinute, false);
            timePickerDialog.show();
        }
    }
    interface OnTimeSelectedListener {
        void onTimeSelected(Calendar calendar);
    }
}

