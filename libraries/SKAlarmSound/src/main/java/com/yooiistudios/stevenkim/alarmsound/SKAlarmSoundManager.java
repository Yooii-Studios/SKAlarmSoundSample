package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundManager
 *   사운드 로직에 관련된 작업을 진행
 */
public class SKAlarmSoundManager {

    private SKAlarmSoundManager() { throw new AssertionError("You MUST not craete class!"); }

    /**
     * Singleton
     */
    /*
    private volatile static SKAlarmSoundManager instance;
    private SKAlarmSoundManager() {}
    public static SKAlarmSoundManager getInstance() {
        if (instance == null) {
            synchronized (SKAlarmSoundManager.class) {
                if (instance == null) {
                    instance = new SKAlarmSoundManager();
                }
            }
        }
        return instance;
    }
    */

    /**
     * Validate SKAlarmSound instance whether playable or not.
     *
     * @param alarmSound SKAlarmSound instance to be validated
     * @param context Context to access the Android
     * @return true if the sound is validate, false if not.
     */
    public static boolean validateAlarmSound(SKAlarmSound alarmSound, Context context) {
        return false;
    }

    /**
     * Return the lastest SKAlarmSound instance from the device.
     * If the one doesn't exist, return the one with the system default ringtone.
     *
     * @param context Context to access the Android
     * @return SKAlarmSound which is selected recently.
     */
    public static SKAlarmSound loadLatestAlarmSound(Context context) {
        SKAlarmSound alarmSound = null;

        return alarmSound;
    }

    /**
     * load the default alarm sound(system default ringtone) from device.
     * @param context Context to access the Android
     * @return default SKAlarmSound
     */
    public static SKAlarmSound loadDefaultAlarmSound(Context context) {
        SKAlarmSound alarmSound = null;

        return alarmSound;
    }
}
