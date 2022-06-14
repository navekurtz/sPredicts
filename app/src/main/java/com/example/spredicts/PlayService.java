package com.example.spredicts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.net.URI;

public class PlayService extends Service
{
    private static final String TAG = null;
    MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(this.mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        else
        {
            mediaPlayer.start();
        }
        return START_STICKY;//play the music till service ended
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCompletion(MediaPlayer mp) {
        if(mediaPlayer.isPlaying()==true)
        {
            mediaPlayer.stop();
        }
    }
    public void onStart(Intent intent, int startId) {
        mediaPlayer.start();
    }

    @Override
    public void onLowMemory()
    {

    }
}
