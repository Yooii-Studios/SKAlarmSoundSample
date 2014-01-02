package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundManager
 *   사운드 로직에 관련된 작업을 진행, Singleton
 */
public class SKAlarmSoundManager {

    /**
     * Singleton
     */
    private volatile static SKAlarmSoundManager instance;
    private SKAlarmSoundManager() {}
    public static SKAlarmSoundManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SKAlarmSoundManager.class) {
                if (instance == null) {
                    instance = new SKAlarmSoundManager();
                }
            }
        }
        return instance;
    }

    /**
     * Validate SKAlarmSound instance whether playable or not.
     *
     * @param alarmSound SKAlarmSound instance to be validated
     * @return true if the sound is validate, false if not.
     */
    public boolean validateAlarmSound(SKAlarmSound alarmSound) {
        return false;
    }

    /**
     * Return the lastest SKAlarmSound instance from the device.
     * If the one doesn't exist, return the one with the system default ringtone.
     *
     * @return SKAlarmSound which is selected recently.
     */
    public SKAlarmSound loadLatestAlarmSound() {
        SKAlarmSound alarmSound = null;

        return alarmSound;
    }
}
