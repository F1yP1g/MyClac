package com.lusai.mycalc;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import com.lusai.mycalc.tools.CustomProgressDialog;


public class StartActivity extends AppCompatActivity {
    private int count = 5;
    private MediaPlayer musicPlayer;
    private CustomProgressDialog customProgressDialog;

    private SoundPool sp;
    private float volume;
    private int currentID;
    private HashMap<Integer, Integer> hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView logo = (ImageView)findViewById(R.id.logo);
        Button getIn = (Button)findViewById(R.id.in);

        initSoundPool();

        musicPlayer = MediaPlayer.create(this,R.raw.bgmusic);
        musicPlayer.start();
        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                musicPlayer.start();
                musicPlayer.setLooping(true);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartActivity.this,"别点我，人家只是图标，好好做题哦~",Toast.LENGTH_SHORT).show();
            }
        });
        getIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(1,0);
                myDialog();
                handler.postDelayed(runnable, 1000);

            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count--;
            if(count == 0){
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                musicPlayer.stop();
                finish();
            }
            handler.postDelayed(this, 1000);
        }
    };

    public void myDialog(){
        customProgressDialog = new CustomProgressDialog(StartActivity.this, "正在进入中...",R.style.Dialog_Fullscreen, R.drawable.startload);
         customProgressDialog.setCancelable(false); // 设置不响应返回按钮点击事件
        customProgressDialog.show();
        // 动态设置自定义Dialog的显示内容的宽和高
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = customProgressDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = d.getWidth();    //宽度设置为全屏
        customProgressDialog.getWindow().setAttributes(p);
    }

    private void initSoundPool() {
        sp=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        hm =new HashMap<Integer, Integer>();
        hm.put(1, sp.load(getApplicationContext(), R.raw.attack, 1));

    }
    /** 播放即时音效*/
    private void playSound(int num,int loop){
        AudioManager am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        float currentSound=am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxSound=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume=currentSound/maxSound;
        currentID=sp.play(hm.get(num), volume, volume, 1, loop, 1.0f);
    }
}
