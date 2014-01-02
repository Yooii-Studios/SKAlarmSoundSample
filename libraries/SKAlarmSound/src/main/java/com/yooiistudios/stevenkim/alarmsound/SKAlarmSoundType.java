package com.yooiistudios.stevenkim.alarmsound;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundType
 */
public enum SKAlarmSoundType {
    MUTE(0, "MUTE"), RINGTONE(1, "RINGTONE"),
    MUSIC(2, "MUSIC"), MORNING_KIT_MUSIC(3, "MORNING_KIT_MUSIC");

    private final int index;
    private final String typeName;
    public int getIndex() { return index; }
    public String toString() { return typeName; }

    SKAlarmSoundType(int index, String typeName) {
        this.index = index;
        this.typeName = typeName;
    }
}
