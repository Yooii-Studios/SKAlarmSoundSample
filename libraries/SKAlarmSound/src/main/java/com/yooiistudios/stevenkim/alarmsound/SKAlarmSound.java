package com.yooiistudios.stevenkim.alarmsound;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * AlarmSound
 *   Model
 */
public class SKAlarmSound {
    public SKAlarmSoundType getAlarmSoundType() {
        return alarmSoundType;
    }

    public void setAlarmSoundType(SKAlarmSoundType alarmSoundType) {
        this.alarmSoundType = alarmSoundType;
    }

    public String getSoundTitle() {
        return soundTitle;
    }

    public void setSoundTitle(String soundTitle) {
        this.soundTitle = soundTitle;
    }

    public String getSoundPathString() {
        return soundPathString;
    }

    public void setSoundPathString(String soundPathString) {
        this.soundPathString = soundPathString;
    }

    private SKAlarmSoundType alarmSoundType;
    private String soundTitle;
    private String soundPathString;
}
