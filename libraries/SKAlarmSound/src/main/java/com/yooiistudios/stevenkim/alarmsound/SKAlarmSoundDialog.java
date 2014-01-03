package com.yooiistudios.stevenkim.alarmsound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;

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
                        alarmSoundClickListener.onAlarmSoundSelected(SKAlarmSoundFactory.makeMuteAlarmSound(context));
                        break;
                    case 1:
                        AlertDialog ringtoneDialog = makeRingtoneDialog(context, alarmSound, alarmSoundClickListener);
                        ringtoneDialog.show();
                        break;
                    case 2:
                        AlertDialog musicDialog = makeMusicDialog(context, alarmSound, alarmSoundClickListener);
                        if (musicDialog != null) {
                            musicDialog.show();
                        }
                        break;
                    case 3:
                        AlertDialog appMusicDialog = makeAppMusicDialog(context, alarmSound, alarmSoundClickListener);
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
        int selectedIndex = -1;

        if (alarmSound != null && alarmSound.getSoundPath() != null) {
            // search the previously selected ringtone if the alarm sound is validate
            if (alarmSound.getAlarmSoundType() == SKAlarmSoundType.RINGTONE
                    && SKAlarmSoundManager.validateAlarmSound(alarmSound.getSoundPath(), context)) {

                for (ringtones.moveToFirst(); !ringtones.isAfterLast(); ringtones.moveToNext()) {
                    selectedIndex++;
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
        AlertDialog alertDialog = builder.setSingleChoiceItems(ringtones, selectedIndex, ringtones.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX), new DialogInterface.OnClickListener() {
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

                // if which is -1, it's the same as canceled
                if (selectedIndex != -1) {
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

                        // set
                        newAlarmSound = SKAlarmSound.newInstance(SKAlarmSoundType.RINGTONE,
                                ringtone.getTitle(context), soundSourcePath);

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

    public static AlertDialog makeMusicDialog(final Context context, final SKAlarmSound alarmSound,
                                              final OnAlarmSoundClickListener alarmSoundClickListener) {

        String[] cursorColumns = new String[] {
                "audio._id AS _id",  // index must match IDCOLIDX below
                MediaStore.Audio.Media.TITLE };
        final Cursor musicCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursorColumns, null, null, null);

        if (musicCursor != null) {
            final MediaPlayer mediaPlayer = new MediaPlayer();

            int selectedIndex = -1;

            // search for the previously selected music index
            if (alarmSound.getAlarmSoundType() == SKAlarmSoundType.MUSIC
                    && SKAlarmSoundManager.validateAlarmSound(alarmSound.getSoundPath(), context)) {
                for (musicCursor.moveToFirst(); !musicCursor.isAfterLast(); musicCursor.moveToNext()) {
                    selectedIndex++;
                    String path = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + String.valueOf(musicCursor.getInt(0));
                    if (path.equals(alarmSound.getSoundPath())) {
                        break;
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

            AlertDialog alertDialog = builder.setSingleChoiceItems(musicCursor, selectedIndex,
                    musicCursor.getColumnName(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView mp3ListView = ((AlertDialog)dialog).getListView();
                    int selectedIndex = mp3ListView.getCheckedItemPosition();

                    // play music when clicked
                    musicCursor.moveToPosition(selectedIndex);
                    String soundPath = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/"
                            + musicCursor.getInt(0);

                    mediaPlayer.reset();
                    try {
                        Uri uri = Uri.parse(soundPath);
                        mediaPlayer.setDataSource(context, uri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView mp3ListView = ((AlertDialog)dialog).getListView();
                    int selectedIndex = mp3ListView.getCheckedItemPosition();

                    // play music when clicked
                    musicCursor.moveToPosition(selectedIndex);
                    String soundPath = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/"
                            + musicCursor.getInt(0);

                    SKAlarmSound newAlarmSound;
                    // 1. set and save the alarmSound if valid
                    if (SKAlarmSoundManager.validateAlarmSound(soundPath, context)) {
                        String title = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        newAlarmSound = SKAlarmSound.newInstance(SKAlarmSoundType.MUSIC, title, soundPath);
                    }
                    // 2. if not, set the default alarmSound
                    else {
                        newAlarmSound = SKAlarmSoundFactory.makeDefaultAlarmSound(context);
                    }
                    musicCursor.close();

                    alarmSoundClickListener.onAlarmSoundSelected(newAlarmSound);
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alarmSoundClickListener.onAlarmSoundSelectCanceled();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    musicCursor.close();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    alarmSoundClickListener.onAlarmSoundSelectCanceled();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    musicCursor.close();
                }
            }).create();

            alertDialog.setTitle(R.string.alarm_sound_string_music);
            alertDialog.setCanceledOnTouchOutside(false);
            return alertDialog;
        } else {
            // can't access the file system because of the USB Connection
            alarmSoundClickListener.onAlarmSoundSelectFailedDueToUsbConnection();
            return null;
        }
    }

    public static AlertDialog makeAppMusicDialog(final Context context, final SKAlarmSound alarmSound,
                                                 final OnAlarmSoundClickListener alarmSoundClickListener) {
        final String[] appMusics = new String[] {
                context.getString(R.string.alarm_sound_string_app_music_dream)
        };

        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);

        int selectedMusicIndex = -1;
        int i;

        // search the index if I selected music in this alarmSound
        if (alarmSound.getAlarmSoundType() == SKAlarmSoundType.APP_MUSIC) {
            for (i = 0; selectedMusicIndex < appMusics.length; i++) {
                if (alarmSound.getSoundTitle().equals(appMusics[i])) {
                    selectedMusicIndex = i;
                    break;
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

        AlertDialog alertDialog = builder.setSingleChoiceItems(appMusics, selectedMusicIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // play music when clicked
                mediaPlayer.reset();

                int appSoundRawInt = MNAlarmAppMusicManager.getRawIntFromIndex(which);
                if (appSoundRawInt != -1) {
                    AssetFileDescriptor afd = context.getResources().openRawResourceFd(appSoundRawInt);
                    if (afd != null) {
                        try {
                            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            afd.close();
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView appMusicListView = ((AlertDialog)dialog).getListView();
                int selectedIndex = appMusicListView.getCheckedItemPosition();

                // if which is -1, it's the same as canceled
                if (selectedIndex != -1) {

                    SKAlarmSound newAlarmSound = SKAlarmSound.newInstance(SKAlarmSoundType.APP_MUSIC, appMusics[selectedIndex], null);
                    alarmSoundClickListener.onAlarmSoundSelected(newAlarmSound);

                    SKAlarmSoundManager.saveLatestAlarmSound(newAlarmSound, context);
                }
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarmSoundClickListener.onAlarmSoundSelectCanceled();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alarmSoundClickListener.onAlarmSoundSelectCanceled();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        }).create();

        alertDialog.setTitle(R.string.alarm_sound_string_app_music);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
