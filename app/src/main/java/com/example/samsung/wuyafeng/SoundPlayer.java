package com.example.samsung.wuyafeng;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by matlab_user on 2016/7/20.
 */
public class SoundPlayer {

    SoundPool soundPool;
    HashMap<Integer, Integer> hashMap;
    int currStreamId;

    public void initSoundPlayer(final AppCompatActivity appCompatActivity, Button bp, Button bs){

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        hashMap = new HashMap<Integer, Integer>();
        hashMap.put(1, soundPool.load(appCompatActivity, R.raw.aaaa, 1));

        bp.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSound(1, 0, appCompatActivity);
                        Toast.makeText(appCompatActivity.getBaseContext(), "Playing Sound", Toast.LENGTH_SHORT).show();
                    }
                });

        bs.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        soundPool.stop(currStreamId);
                        Toast.makeText(appCompatActivity.getBaseContext(), "Stop sound", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void playSound(int sound, int loop, AppCompatActivity appCompatActivity)
    {

        System.out.print("LWC: playing sound... ");
        AudioManager audioManager = (AudioManager) appCompatActivity.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent/streamVolumeMax;
        currStreamId = soundPool.play(hashMap.get(sound), volume, volume, 1, loop, 1.0f);
    }

}
