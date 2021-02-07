package com.seeingvoice.case36audiotrack_puretone;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private PlayThread mPlayThread;

    Button btnPlay, btnLeft, btnRight, btnStop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay =  findViewById(R.id.btn_play);
        btnLeft =  findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        btnStop =  findViewById(R.id.btn_stop);

        btnPlay.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            if (null != mPlayThread) {
                mPlayThread.stopPlay();
                mPlayThread = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        int vid= view.getId();
        if (vid == R.id.btn_play) {
            playSound(true, true);
        }else if (vid == R.id.btn_left){
                playSound(true,false);
        }else if (vid == R.id.btn_right){
                playSound(false,true);
        }else if (vid == R.id.btn_stop){
                if (null != mPlayThread) {
                    mPlayThread.stopPlay();
                    mPlayThread = null;
                }
        }
    }

    private void playSound(boolean left, boolean right) {
        if (null != mPlayThread) {
            mPlayThread.stopPlay();
            mPlayThread = null;
        }
        mPlayThread = new PlayThread(100);
        mPlayThread.setChannel(left,right);
        mPlayThread.start();
    }
}