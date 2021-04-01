package com.sigma.asynctask_p;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private ProgressBar progressBar;
    private SwitchCompat aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView_Id);
        button = findViewById(R.id.button_Id);
        progressBar = findViewById(R.id.progressBar_Id);
        aSwitch = findViewById(R.id.switch_Id);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyTask myTask = new MyTask();
                myTask.execute("Download");
            }
        });

    }

    private class MyTask extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setText("Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {

            for(int i =0; i<=10; i++){

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                onProgressUpdate(i);
            }

            return strings[0];
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);
            progressBar.setProgress(integers[0]);
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            textView.setText(strings+" complete!");
            showNotification();
        }
    }

    private void showNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "channel_Id");
        builder.setContentTitle("Notification");
        builder.setContentText("Download finished");
        builder.setSmallIcon(R.drawable.ic_done_24);
        builder.setAutoCancel(true);

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        int channelId = (int) System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("channel_Id", "Async Task", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(channelId, builder.build());
        } else {
            notificationManager.notify(channelId, builder.build());
        }

    }
}