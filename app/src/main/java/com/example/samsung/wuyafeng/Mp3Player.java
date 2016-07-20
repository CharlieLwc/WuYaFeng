package com.example.samsung.wuyafeng;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by matlab_user on 2016/7/20.
 */
public class Mp3Player {


    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    private int maxVolume;
    private int currVolume;
    private int stepVolume;


    void initMp3Player(final AppCompatActivity appCompatActivity, Button bStart, Button bPause, Button bStop){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource("/sdcard/glsl.mp3");
            mediaPlayer.prepare();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        audioManager = (AudioManager) appCompatActivity.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        stepVolume = maxVolume/6;

        bStart.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.start();
                        Toast.makeText(appCompatActivity.getBaseContext(), "Playing mp3", Toast.LENGTH_SHORT).show();
                    }
                });

        bPause.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.pause();
                        Toast.makeText(appCompatActivity.getBaseContext(), "Pausing mp3", Toast.LENGTH_SHORT).show();
                    }
                });


        bStop.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.stop();
                        try{
                            mediaPlayer.prepare();
                        }
                        catch(IllegalStateException e){
                            e.printStackTrace();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }


                        Toast.makeText(appCompatActivity.getBaseContext(), "Stop mp3", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
