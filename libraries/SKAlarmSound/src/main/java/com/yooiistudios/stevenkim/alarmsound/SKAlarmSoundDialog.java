package com.yooiistudios.stevenkim.alarmsound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 2.
 *
 * SKAlarmSoundDialog
 *  Class that makes dialogs setting alarm sound in preference activity
 */
public class SKAlarmSoundDialog {
    private static final String TAG = "SKAlarmSoundDialog";
    private SKAlarmSoundDialog() { throw new AssertionError("You MUST not create this class!"); }

    public static AlertDialog makeSoundAlertDialog(final Context context, final SKAlarmSound alarmSound,
                                                   final OnAlarmSoundClickListener alarmSoundDialogInterface) {
        // SingleChoiceItems
        final String[] soundTypes = new String[] {
                context.getString(R.string.alarm_sound_string_none),
                context.getString(R.string.alarm_sound_string_ringtones),
                context.getString(R.string.alarm_sound_string_music),
                context.getString(R.string.alarm_sound_string_app_music),
        };

        // Builder for each version
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        // Build AlertDialog
        AlertDialog alertDialog = builder.setSingleChoiceItems(soundTypes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Log.i(TAG, "sound_none");
                        alarmSoundDialogInterface.onAlarmSoundSelected(SKAlarmSoundFactory.makeMuteAlarmSound(context));
                        break;
                    case 1:
                        Log.i(TAG, "sound_ringtones");
                        AlertDialog ringtoneDialog = makeRingtoneDialog(context, alarmSound);
                        ringtoneDialog.show();
                        break;
                    case 2:
                        Log.i(TAG, "sound_music");
                        AlertDialog musicDialog = makeMusicDialog(context, alarmSound);
                        musicDialog.show();
                        break;
                    case 3:
                        Log.i(TAG, "sound_app_music");
                        AlertDialog appMusicDialog = makeAppMusicDialog(context, alarmSound);
                        appMusicDialog.show();
                        break;
                }
                dialog.dismiss();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "onCancel");
            }
        }).create();

        alertDialog.setTitle(R.string.alarm_pref_sound_type);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    public static AlertDialog makeRingtoneDialog(final Context context, final SKAlarmSound alarmSound) {
        return null;
    }

    public static AlertDialog makeMusicDialog(final Context context, final SKAlarmSound alarmSound) {
        return null;
    }

    public static AlertDialog makeAppMusicDialog(final Context context, final SKAlarmSound alarmSound) {
        return null;
    }
}
