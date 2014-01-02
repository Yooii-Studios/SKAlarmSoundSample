package com.yooiistudios.stevenkim.alarmsound;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarm
 *  알람 자료구조
 */
public class MNAlarm implements Serializable, Cloneable {
    private static final String TAG = "MNAlarm";

    // 로직의 변경이 있을 때 같은 클래스임을 명시적으로 알려 주는 코드
    private static final long serialVersionUID = 1L;

    /**
     * Variables
     */
    private boolean             isAlarmOn;
    private boolean             isSnoozeOn;
    private boolean             isRepeatOn;

    private ArrayList<Boolean>  alarmRepeatList;

    private String              alarmLabel;

    // 한 알람당 8개 할당. n+0번 ~ n+6번: 미반복/월(0번이 월요일이거나 미반복) ~ 일, n+7번: 스누즈
    private int                 alarmId;

    private Calendar            alarmCalendar;

    // 사운드

    /**
     * Methods
     */
    // 이 메서드 호출을 방지, 일반적으로 MNAlarmMaker에서 생성해 사용할 것
    private MNAlarm() {}

    public static MNAlarm newInstance() {
        MNAlarm alarm = new MNAlarm();
        alarm.alarmRepeatList = new ArrayList<Boolean>(7);
        return alarm;
    }

    // 혹시나 깊은 복사를 사용할 경우를 대비해서 가져옴
    // 아래 주석은 Eclipse에서 그대로 가져옴
    @SuppressWarnings("unchecked")
    public MNAlarm clone() throws CloneNotSupportedException {
        MNAlarm obj = (MNAlarm)super.clone();
        obj.alarmCalendar = (Calendar) alarmCalendar.clone();
        obj.alarmRepeatList = (ArrayList<Boolean>) alarmRepeatList.clone();

        return obj;
    }
    
    @Override
    public String toString() {
        return String.format("alarmId: %d / alarmLabel: %s / on: %s, repeat: %s / ",
                alarmId,
                alarmLabel,
                isAlarmOn ? "Yes" : "No",
                isRepeatOn ? "Yes" : "No");
    }

    /**
     * Getter & Setter
     */
    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean isAlarmOn) {
        this.isAlarmOn = isAlarmOn;
    }

    public boolean isSnoozeOn() {
        return isSnoozeOn;
    }

    public void setSnoozeOn(boolean isSnoozeOn) {
        this.isSnoozeOn = isSnoozeOn;
    }

    public boolean isRepeatOn() {
        return isRepeatOn;
    }

    public void setRepeatOn(boolean isRepeatOn) {
        this.isRepeatOn = isRepeatOn;
    }

    public ArrayList<Boolean> getAlarmRepeatList() {
        return alarmRepeatList;
    }

    public void setAlarmRepeatList(ArrayList<Boolean> alarmRepeatList) {
        this.alarmRepeatList = alarmRepeatList;
    }

    public String getAlarmLabel() {
        return alarmLabel;
    }

    public void setAlarmLabel(String alarmLabel) {
        this.alarmLabel = alarmLabel;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public Calendar getAlarmCalendar() {
        return alarmCalendar;
    }

    public void setAlarmCalendar(Calendar alarmCalendar) {
        this.alarmCalendar = alarmCalendar;
    }
}
