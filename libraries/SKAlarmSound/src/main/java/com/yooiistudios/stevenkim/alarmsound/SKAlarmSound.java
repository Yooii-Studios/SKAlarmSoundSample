package com.yooiistudios.stevenkim.alarmsound;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * AlarmSound
 *   Model
 */
public class SKAlarmSound {

    private SKAlarmSoundType alarmSoundType;
    private String soundTitle;
    private String soundPathString;

    private SKAlarmSound() {}
    private SKAlarmSound(SKAlarmSoundType alarmSoundType, String soundTitle, String soundPathString) {
        setAlarmSoundType(alarmSoundType);
        setSoundTitle(soundTitle);
        setSoundPathString(soundPathString);
    }

    // Factory
    public static SKAlarmSound newInstance(SKAlarmSoundType alarmSoundType,
                                           String soundTitle, String soundPathString) {
        return new SKAlarmSound(alarmSoundType, soundTitle, soundPathString);
    }

    /**
     * Getter & Setter
     */
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
}
