package com.yooiistudios.stevenkim.alarmsound.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yooiistudios.stevenkim.alarmsound.OnAlarmSoundClickListener;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSound;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundDialog;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundFactory;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity implements OnAlarmSoundClickListener {

    private static final String TAG = "MainActivity";

    @InjectView(R.id.alarmSoundTypeTextView) TextView soundTypeTextView;
    @InjectView(R.id.alarmSoundTitleTextView) TextView soundTitleTextView;

    SKAlarmSound currentAlarmSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        currentAlarmSound = SKAlarmSoundManager.loadLatestAlarmSound(this);
    }

    @OnClick(R.id.mp3Button)
    void mp3ButtonClicked() {
        Log.i(TAG, "mp3ButtonClicked");
    }

    @OnClick(R.id.ringtonButton)
    void ringtonesButtonClicked() {
        Log.i(TAG, "ringtonesButtonClicked");
    }

    @OnClick(R.id.mnButton)
    void mnButtonClicked() {
        Log.i(TAG, "mnButtonClicked");
    }

    @OnClick(R.id.none)
    void noneButtonClicked() {
        Log.i(TAG, "noneButtonClicked");
        currentAlarmSound = SKAlarmSoundFactory.makeMuteAlarmSound(this);
        refreshAlarmSoundTextViews();
    }

    @OnClick(R.id.alarmSoundDialogButton)
    void soundDialogButtonClicked() {
        Log.i(TAG, "soundDialogButtonClicked");
        SKAlarmSoundDialog.makeSoundAlertDialog(this, currentAlarmSound, this).show();
    }

    @OnClick(R.id.clearSoundButton)
    void clearSoundButtonClicked() {
        Log.i(TAG, "clearSoundButtonClicked");
        currentAlarmSound = null;
    }

    void refreshAlarmSoundTextViews() {
        soundTypeTextView.setText(currentAlarmSound.getAlarmSoundType().toString());
        soundTitleTextView.setText(currentAlarmSound.getSoundTitle());
    }

    /**
     * SKAlarmSoundDialog Listner
     */
    @Override
    public void onAlarmSoundSelected(SKAlarmSound alarmSound) {
        currentAlarmSound = alarmSound;
        refreshAlarmSoundTextViews();
    }
}
