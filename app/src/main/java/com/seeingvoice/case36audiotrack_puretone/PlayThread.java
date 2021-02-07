package com.seeingvoice.case36audiotrack_puretone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import static com.seeingvoice.case36audiotrack_puretone.GlobalConfig.SAMPLE_RATE;

/**
 * Created by LeoReny@hypech.com on 2021/2/2.
 */

public class PlayThread extends Thread {
    public static final int AMPLITUDE = 65535; //2^16 -1;
    public static final double PI2 = 2 * 3.14159265;
    double increment;
    short[] wave;

    AudioTrack mAudioTrack;
    public static boolean ISPLAYSOUND;

    int numOfSamplesPerWave,  audioFrequency;
//    byte[] wave;

    public PlayThread(int audioFrequency) {
        //number of sample points per full wave 44100/100 = 441次/s
        numOfSamplesPerWave = SAMPLE_RATE / audioFrequency;
        //angle value of each block
        increment = PI2 / numOfSamplesPerWave;
        //f(x) = sin(x) , wave = sin(44100 pieces of data)
        //We just need one period of the sin, and repeat it.
        wave = new short[SAMPLE_RATE];
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, SAMPLE_RATE*2 , AudioTrack.MODE_STREAM);
        ISPLAYSOUND = true;
        wave = PureTone.sine(wave, increment);
    }

    @Override
    public void run() {
        super.run();
        if (null != mAudioTrack)
            mAudioTrack.play();
        //一直播放
        while (ISPLAYSOUND) {
            mAudioTrack.write(wave, 0, SAMPLE_RATE);
        }

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
        ISPLAYSOUND = false;
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
