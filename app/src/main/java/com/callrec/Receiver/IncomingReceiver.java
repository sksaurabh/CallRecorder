package com.callrec.Receiver;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.media.MediaRecorder;

import android.os.Bundle;

import android.os.Environment;

import android.telephony.PhoneStateListener;

import android.telephony.TelephonyManager;

import android.util.Log;
import android.widget.Toast;


public class IncomingReceiver extends BroadcastReceiver {


    MediaRecorder recorder;
    TelephonyManager telManager;
    boolean recordStarted;
    private Context context;
    String phoneNumber;
    String selected_song_name;
    String currentTimeMilli;
    String completeFilePath;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        currentTimeMilli = Long.toString(System.currentTimeMillis());

        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                incomingCallRecord(action, context);
            }
        }
    }

    private void incomingCallRecord(String action, Context context) {

        // TODO Auto-generated method stub
        if (action.equals("android.intent.action.PHONE_STATE")) {
            telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void unregisterTaleManager() {
        telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
    }

    private final PhoneStateListener phoneListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
                        Log.d("Ringing", "");
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {


                        try {
                            recorder = new MediaRecorder();
                            recorder.reset();
                            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                            File file = createDirIfNotExists("IN" + currentTimeMilli + "\t=> " + phoneNumber);

                            recorder.setOutputFile(file.getAbsolutePath());
                            recorder.prepare();
                            recorder.start();
                            recordStarted = true;
                            // Toast.makeText(context,"Recording started",Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //Toast.makeText(context,"Recording start excecption",Toast.LENGTH_LONG).show();
                        }

                        break;
                    }

                    case TelephonyManager.CALL_STATE_IDLE: {
                        if (recordStarted) {
                            recorder.stop();
                            recorder.reset();
                            recorder.release();
                            recorder = null;
                            recordStarted = false;
                            unregisterTaleManager();
                            if (completeFilePath != null && !completeFilePath.isEmpty())
                                Toast.makeText(context, "Record Saved", Toast.LENGTH_SHORT).show();
                            // Toast.makeText(context,"Recording stop Complete",Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    default: {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private byte[] getAudioFileFromSdcard() throws FileNotFoundException {

            byte[] inarry = null;
            try {
                File sdcard = new File(Environment.getExternalStorageDirectory() + "/MyRecordME");
                File file = new File(sdcard, selected_song_name + ".mp3");
                FileInputStream fileInputStream = null;
                byte[] bFile = new byte[(int) file.length()];
                // convert file into array of bytes
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();
                inarry = bFile;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return inarry;
        }
    };

    public File createDirIfNotExists(String path) {
        selected_song_name = path;
        File folder = new File(Environment.getExternalStorageDirectory() + "/MyRecordME");
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Log.e("TravellerLog :: ", "folder is created");
            }
        }

        File file = new File(folder, path + ".mp3");
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Log.e("TravellerLog :: ", "file is created");
                    completeFilePath = file.getAbsolutePath();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }
}
