package com.hypech.case32audiotrack_sine_pcm_static;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.media.AudioAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LeoReny@hypech.com on 2021/2/2.
 */
public class PlayThreadPCM extends Thread {
    public static boolean IS_PLAY_SOUND;
    private Activity mActivity;
    private AudioTrack mAudioTrack;
    private byte[] audioData;
    private String mFileName;
    private int mRid;

    public PlayThreadPCM(Activity activity, String fileName, int rID) {
        mActivity = activity;
        mFileName = fileName;
        mRid = rID;
    }

    @Override
    public void run() {
        super.run();
        try {
            try (InputStream in = mActivity.getResources().openRawResource(mRid)) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                for (int b; (b = in.read()) != -1; ) {
                    out.write(b);
                }
                audioData = out.toByteArray();
            }
        } catch (IOException e) {
            Log.wtf("DD", "Failed to read", e);
        }

        // R.raw.ding铃声文件的相关属性为 22050Hz, 8-bit, Mono
        mAudioTrack = new AudioTrack(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                new AudioFormat.Builder().setSampleRate(22050)
                        .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build(),
                audioData.length,
                AudioTrack.MODE_STATIC,
                AudioManager.AUDIO_SESSION_ID_GENERATE);
        mAudioTrack.write(audioData, 0, audioData.length);
        mAudioTrack.play();
        Log.d("ff", "Playing");
    }


    /**
     * 设置左右声道，左声道时设置右声道音量为0，右声道设置左声道音量为0
     *
     * @param left  左声道
     * @param right 右声道
     */
    public void setChannel(boolean left, boolean right) {
        if (null != mAudioTrack) {
            mAudioTrack.setStereoVolume(left ? 1 : 0, right ? 1 : 0);
        }
    }

    //设置音量
    public void setVolume(float left, float right) {
        if (null != mAudioTrack) {
            mAudioTrack.setStereoVolume(left,right);
        }
    }

    public void stopPlay() {
        IS_PLAY_SOUND = false;
        releaseAudioTrack();
    }

    private void releaseAudioTrack() {
        if (null != mAudioTrack) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }
}
