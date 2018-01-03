package com.teamtreehouse.musicmachine;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.teamtreehouse.musicmachine.models.Song;

public class DownloadIntentService extends IntentService {
    private static final String TAG = DownloadIntentService.class.getSimpleName();
    private static final int REQUEST_OPEN_FROM_NOTIFICATION = 55;

    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 22;

    public DownloadIntentService() {
        super("DownloadIntentService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Song song = intent.getParcelableExtra(MainActivity.EXTRA_SONG);

        Intent mainIntent = new Intent(this, DetailActivity.class);
        mainIntent.putExtra(MainActivity.EXTRA_SONG, song);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                        this,
                        REQUEST_OPEN_FROM_NOTIFICATION,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_queue_music_white)
                .setContentTitle("Downloading")
                .setContentText(song.getTitle())
                .setContentIntent(pendingIntent)
                .setProgress(0, 0, true);

        notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

        downloadSong(song.getTitle());
    }

    private void downloadSong(String song) {
        long endTime = System.currentTimeMillis() + 2*1000;
        while (System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, song + " downloaded!");

        notificationManager.cancel(NOTIFICATION_ID);
    }
}
