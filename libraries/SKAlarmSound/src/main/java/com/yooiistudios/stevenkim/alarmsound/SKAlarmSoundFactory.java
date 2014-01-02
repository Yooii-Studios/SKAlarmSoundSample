package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundFactory
 *  미리 정해진 세팅의 AlarmSound를 만든다
 */
public class SKAlarmSoundFactory {
    private SKAlarmSoundFactory() { throw new AssertionError("You MUST not craete class!"); }

    /**
     * make the default alarm sound(system default ringtone) from device.
     * @param context Context to access the Android
     * @return default SKAlarmSound
     */
    public static SKAlarmSound makeDefaultAlarmSound(Context context) {
        // get default sound stuff from the device(Context)
        String soundTitle = "";
        String soundPath = "";

        // init defaultAlarmSound
        return SKAlarmSound.newInstance(SKAlarmSoundType.RINGTONE, soundTitle, soundPath);
    }

    public static SKAlarmSound makeMuteAlarmSound(Context context) {
        return SKAlarmSound.newInstance(SKAlarmSoundType.MUTE, "", null);
    }
}
