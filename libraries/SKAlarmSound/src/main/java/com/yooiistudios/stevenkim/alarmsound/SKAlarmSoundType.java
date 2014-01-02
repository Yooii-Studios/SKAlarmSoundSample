package com.yooiistudios.stevenkim.alarmsound;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundType
 */
public enum SKAlarmSoundType {
    MUTE(0), RINGTONE(1), MUSIC(2), MORNING_KIT_MUSIC(3);

    private final int index;
    public int getIndex() { return index; }

    SKAlarmSoundType(int index) {
        this.index = index;
    }
}
