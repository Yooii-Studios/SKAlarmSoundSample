package com.yooiistudios.stevenkim.alarmsound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;

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
                                                   final OnAlarmSoundClickListener alarmSoundClickListener) {
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
                        alarmSoundClickListener.onAlarmSoundSelected(SKAlarmSoundFactory.makeMuteAlarmSound(context));
                        break;
                    case 1:
                        Log.i(TAG, "sound_ringtones");
                        AlertDialog ringtoneDialog = makeRingtoneDialog(context, alarmSound, alarmSoundClickListener);
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
                alarmSoundClickListener.onAlarmSoundSelectCanceled();
            }
        }).create();

        alertDialog.setTitle(R.string.alarm_pref_sound_type);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    public static AlertDialog makeRingtoneDialog(final Context context, final SKAlarmSound alarmSound,
                                                 final OnAlarmSoundClickListener alarmSoundClickListener) {

        RingtoneManager ringtoneManager = new RingtoneManager(context);
        final Cursor ringtones = ringtoneManager.getCursor();
        final MediaPlayer mediaPlayer = new MediaPlayer();

        // default is not selected
        int selected = -1;

        if (alarmSound != null && alarmSound.getSoundPath() != null) {
            Log.i(TAG, "selected ringtone: " +  alarmSound.getSoundPath());

            // search the previously selected ringtone if the alarm sound is validate
            if (alarmSound.getAlarmSoundType() == SKAlarmSoundType.RINGTONE
                    && SKAlarmSoundManager.validateAlarmSound(alarmSound.getSoundPath(), context)) {

                for (ringtones.moveToFirst(); !ringtones.isAfterLast(); ringtones.moveToNext()) {
                    selected++;
                    String path = ringtones.getString(RingtoneManager.URI_COLUMN_INDEX) + "/"
                            + ringtones.getInt(RingtoneManager.ID_COLUMN_INDEX);

                    if (path.equals(alarmSound.getSoundPath())) {
                        break;
                    }
                }
            }
        }

        // Builder for each version
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        // Build AlertDialog
        AlertDialog alertDialog = builder.setSingleChoiceItems(ringtones, selected,
                ringtones.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // play ringtone when selected
                ringtones.moveToPosition(which);
                String path = ringtones.getString(RingtoneManager.URI_COLUMN_INDEX)
                        + "/"
                        + ringtones.getInt(RingtoneManager.ID_COLUMN_INDEX);
                mediaPlayer.reset();
                try{
                    Uri uri = Uri.parse(path);
                    mediaPlayer.setDataSource(context, uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ListView ringtonListView = ((AlertDialog)dialog).getListView();
                int selectedIndex = ringtonListView.getCheckedItemPosition();
                Log.i(TAG, "selectedIndex: " + ringtonListView.getCheckedItemPosition());

                // if which is -1, it's the same as canceled
                if (selectedIndex != -1) {
                    // 벨소리 path, 벨소리 이름 적용하기
                    ringtones.moveToPosition(selectedIndex);
                    String soundSourcePath = ringtones.getString(RingtoneManager.URI_COLUMN_INDEX)
                            + "/" + ringtones.getInt(RingtoneManager.ID_COLUMN_INDEX);

                    Uri ringtoneSource;
                    Ringtone ringtone;
                    SKAlarmSound newAlarmSound;

                    // 1. set and save to latestAlarmSound if ringtone source is valid
                    if (SKAlarmSoundManager.validateAlarmSound(soundSourcePath, context)) {
                        ringtoneSource = Uri.parse(soundSourcePath);
                        ringtone = RingtoneManager.getRingtone(context, ringtoneSource);
                        newAlarmSound = SKAlarmSound.newInstance(SKAlarmSoundType.RINGTONE, ringtone.getTitle(context), soundSourcePath);

                        // Save
                        SKAlarmSoundManager.saveLatestAlarmSound(newAlarmSound, context);
                    }
                    // 2. If not, make default alarm sound
                    else {
                        newAlarmSound = SKAlarmSoundFactory.makeDefaultAlarmSound(context);
                    }

                    // callback
                    alarmSoundClickListener.onAlarmSoundSelected(newAlarmSound);
                }

                // stop ringtone
                mediaPlayer.reset();
                mediaPlayer.release();
                ringtones.close();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // stop ringtone
                mediaPlayer.reset();
                mediaPlayer.release();
                ringtones.close();
                alarmSoundClickListener.onAlarmSoundSelectCanceled();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "onCancel");
                // stop ringtone
                mediaPlayer.reset();
                mediaPlayer.release();
                ringtones.close();
                alarmSoundClickListener.onAlarmSoundSelectCanceled();
            }
        }).create();

        alertDialog.setTitle(R.string.alarm_sound_string_ringtones);
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }

    public static AlertDialog makeMusicDialog(final Context context, final SKAlarmSound alarmSound) {
        return null;
    }

    public static AlertDialog makeAppMusicDialog(final Context context, final SKAlarmSound alarmSound) {
        return null;
    }
}
