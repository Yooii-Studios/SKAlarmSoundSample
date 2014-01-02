package com.yooiistudios.stevenkim.alarmsound.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import lombok.Getter;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Getter @InjectView(R.id.alarmSoundTypeTextView) TextView soundTypeTextView;
    @Getter @InjectView(R.id.alarmSoundTitleTextView) TextView soundTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.alarmSoundDialogButton)
    void soundDialogButtonClicked() {
        Log.i(TAG, "soundDialogButtonClicked");
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
    }
}
