package com.example.samsung.wuyafeng;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    SoundPlayer soundPlayer;
    Mp3Player mp3Player;
    FileOpener fileOpener;
    AssetOpener assetOpener;
    MySurfaceView gameView;//游戏界面

    View.OnClickListener returnLisener;

    MyTDView mview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        returnLisener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivity();
            }
        };

//        initActivity();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mview=new MyTDView(this);
        mview.requestFocus();
        mview.setFocusableInTouchMode(true);
        setContentView(mview);


    }



    void initActivity()
    {

        setContentView(R.layout.activity_main);

        Button buttonAsset = (Button) this.findViewById(R.id.button_Asset);
        Button buttonFolder = (Button) this.findViewById(R.id.button_Folder);
        Button button2D = (Button) this.findViewById(R.id.button_2D);

        fileOpener = new FileOpener();
        fileOpener.initFileOpener(this, buttonFolder, returnLisener);

        assetOpener = new AssetOpener();
        assetOpener.initAssetOpener(this, buttonAsset, returnLisener);


        gameView = new MySurfaceView(this, button2D, returnLisener);//创建游戏界面对象



        Button buttonPlay = (Button) this.findViewById(R.id.button_Play);
        Button buttonStop = (Button) this.findViewById(R.id.button_Stop);
        Button buttonPause = (Button) this.findViewById(R.id.button_Pause);

        System.out.print("LWC: init soundPlayer ... ");

        soundPlayer = new SoundPlayer();
        soundPlayer.initSoundPlayer(this, buttonPlay, buttonStop);

        mp3Player = new Mp3Player();
        mp3Player.initMp3Player(this, buttonPlay, buttonPause, buttonStop);


        System.out.println("LWC: done");

        Log.d("LWC", "the first log");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }


    @Override
    public void onResume()
    {
        super.onResume();
        mview.onResume();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mview.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
