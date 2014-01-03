package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 3.
 *
 * SKAlarmSoundPlayer
 *  알람 사운드 재생을 담당
 */
public class SKAlarmSoundPlayer {

    /**
     * Singleton
     */
    private volatile static SKAlarmSoundPlayer instance;
    private MediaPlayer mediaPlayer;
    public static MediaPlayer getMediaPlayer() { return getInstance().mediaPlayer; }
    private SKAlarmSoundPlayer() {}
    public static SKAlarmSoundPlayer getInstance() {
        if (instance == null) {
            synchronized (SKAlarmSoundManager.class) {
                if (instance == null) {
                    instance = new SKAlarmSoundPlayer();
                    instance.mediaPlayer = new MediaPlayer();
                }
            }
        }
        return instance;
    }

    public static void play(final Uri uri, final Context context) throws IOException {
        getMediaPlayer().reset();
        getMediaPlayer().setDataSource(context, uri);
        getMediaPlayer().prepare();
        getMediaPlayer().start();
    }

    public static void stop() {
        getMediaPlayer().stop();
    }

    public static void playAlarmSound(final SKAlarmSound alarmSound, final Context context) throws IOException {
        if (alarmSound.getAlarmSoundType() != SKAlarmSoundType.APP_MUSIC) {
            getMediaPlayer().reset();
            Uri uri = Uri.parse(alarmSound.getSoundPath());
            getMediaPlayer().setDataSource(context, uri);
            getMediaPlayer().prepare();
            getMediaPlayer().start();
        } else {

        }
    }
}
