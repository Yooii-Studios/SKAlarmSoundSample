package com.yooiistudios.stevenkim.alarmsound.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yooiistudios.stevenkim.alarmsound.OnAlarmSoundClickListener;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSound;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundDialog;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundFactory;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundManager;
import com.yooiistudios.stevenkim.alarmsound.SKAlarmSoundPlayer;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity implements OnAlarmSoundClickListener {

    private static final String TAG = "MainActivity";

    @InjectView(R.id.alarmSoundTypeTextView) TextView soundTypeTextView;
    @InjectView(R.id.alarmSoundTitleTextView) TextView soundTitleTextView;
    @InjectView(R.id.playStopButton) Button playAndStopButton;

    SKAlarmSound currentAlarmSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        currentAlarmSound = SKAlarmSoundManager.loadLatestAlarmSound(this);
        refreshAlarmSoundTextViews();
    }

    @OnClick(R.id.mp3Button)
    void mp3ButtonClicked() {
        AlertDialog musicDialog = SKAlarmSoundDialog.makeMusicDialog(this, currentAlarmSound, this);
        if (musicDialog != null) {
            musicDialog.show();
        }
    }

    @OnClick(R.id.ringtonButton)
    void ringtonesButtonClicked() {
        SKAlarmSoundDialog.makeRingtoneDialog(this, currentAlarmSound, this).show();
    }

    @OnClick(R.id.mnButton)
    void mnButtonClicked() {
        SKAlarmSoundDialog.makeAppMusicDialog(this, currentAlarmSound, this).show();
    }

    @OnClick(R.id.none)
    void noneButtonClicked() {
        currentAlarmSound = SKAlarmSoundFactory.makeMuteAlarmSound(this);
        refreshAlarmSoundTextViews();
    }

    @OnClick(R.id.alarmSoundDialogButton)
    void soundDialogButtonClicked() {
        SKAlarmSoundDialog.makeSoundDialog(this, currentAlarmSound, this).show();
    }

    @OnClick(R.id.clearSoundButton)
    void clearSoundButtonClicked() {
        currentAlarmSound = null;
        SKAlarmSoundManager.clearLatestAlarmSound(this);
        refreshAlarmSoundTextViews();
    }

    @OnClick(R.id.playStopButton)
    void playAndStopSound() {
        if (playAndStopButton.getText().toString().equals("Play")) {
            playAndStopButton.setText("Stop");
            try {
                SKAlarmSoundPlayer.playAlarmSound(currentAlarmSound, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            playAndStopButton.setText("Play");
            SKAlarmSoundPlayer.stop();
        }
    }

    void refreshAlarmSoundTextViews() {
        if (currentAlarmSound != null) {
            soundTypeTextView.setText(currentAlarmSound.getAlarmSoundType().toString());
            soundTitleTextView.setText(currentAlarmSound.getSoundTitle());
        } else {
            soundTypeTextView.setText("Sound Type");
            soundTitleTextView.setText("Sound Title");
        }
    }

    /**
     * SKAlarmSoundDialog Listner
     */
    @Override
    public void onAlarmSoundSelected(SKAlarmSound alarmSound) {
        currentAlarmSound = alarmSound;
        refreshAlarmSoundTextViews();
    }

    @Override
    public void onAlarmSoundSelectCanceled() {
        Toast.makeText(this, "Alarm selection canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlarmSoundSelectFailedDueToUsbConnection() {
        Toast.makeText(this, "Can't access the file system because of USB Connection", Toast.LENGTH_SHORT).show();
    }
}
