package com.hypech.case32audiotrack_sine_pcm_static;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by LeoReny@hypech.com on 2021/2/2.
 */

public class PlayThread extends Thread {
    //人耳能够感觉到的最高频率为20kaudioFrequency，因此要满足人耳的听觉要求，则需要至少每秒进行40k次采样，
    // 这个40kHz就是采样率。我们常见的CD音质，采样率为44.1kaudioFrequency。
    public static final int SAMPLE_RATE = 44100;
    public static final int AMPLITUDE = 65535; //2^16 -1;
    public static final double PI2 = 2 * 3.14159265;
    double increment;
    short[] wave;

    AudioTrack mAudioTrack;
    public static boolean ISPLAYSOUND;

    int length,  numOfSamplesPerWave,  audioFrequency;
//    byte[] wave;

    /**
     * 初始化
     * @param rate 频率
     */
    public PlayThread(int rate) {
        if (rate > 0) {
            //sound actual frequency 100hz
            audioFrequency = rate;
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
//            wave = SinWave.sin(wave, numOfSamplesPerWave, SAMPLE_RATE);
            wave = sine(wave, increment);
        }
    }

    public static short[] sine(short[] wave, double increment) {
        for (int i = 0; i < SAMPLE_RATE; i++) {

            wave[i] = (short) (AMPLITUDE * Math.sin(increment * i));

//            wave[i] = (short) (HEIGHT * (1 - Math.sin(TWOPI * ((i % waveLen) * 1.00 / waveLen))));
//            wave[i] = (short) (HEIGHT * (1 - Math.sin(TWOPI * ((i % waveLen) * 1.00 / waveLen))));
        }
        return wave;
    }


    @Override
    public void run() {
        super.run();
        if (null != mAudioTrack)
            mAudioTrack.play();
        //一直播放
        while (ISPLAYSOUND) {
            mAudioTrack.write(wave, 0, length);
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
