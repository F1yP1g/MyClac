package com.lusai.mycalc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lusai.mycalc.ui.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public int minPlus = 1,maxPlus = 100;
    public int minMut = 2,maxMut = 9;
    public int result = 0, userResult = 0;
    int flag = 0;
    TextView hinttext,calcstr;
    Button Go;
    EditText re,min1 ,min2,max1,max2;
    Random random = new Random();

    private MediaPlayer musicPlayer;

    Date curDate,endDate;
    private  long cur = 0,firstTime = 0;
    private SoundPool sp;
    private float volume;
    private int currentID;
    private HashMap<Integer, Integer> hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hinttext = (TextView)findViewById(R.id.hint);
        Go = (Button)findViewById(R.id.go_button);
        calcstr = (TextView)findViewById(R.id.calc_text);
        re = (EditText)findViewById(R.id.re_number);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initSoundPool();
        /**背景音乐*/
        musicPlayer = MediaPlayer.create(this,R.raw.mainbgmusic);
        musicPlayer.start();
        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                musicPlayer.start();
                musicPlayer.setLooping(true);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CalcContrl();
        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound(1,0);
                try {
                    userResult = Integer.valueOf(re.getText().toString()).intValue();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (result == userResult) {
                    if(flag >=25){
                        Dialog();
                        CalcContrl();
                    }
                    else
                        CalcContrl();
                } else
                    Toast.makeText(MainActivity.this, "计算错误！", Toast.LENGTH_SHORT).show();
                re.setText("");

            }
        });
    }

    public void CalcContrl(){
        if(cur == 0){
            curDate = new Date(System.currentTimeMillis());
            cur = curDate.getTime();
        }
        if(flag < 5){
            hinttext.setText("第 "+(++flag)+" 道加减法");
            AddOrSub();
        }
        else if(flag < 10){
            hinttext.setText("第 "+(++flag-5)+" 道余数");
            mod();
        }
        else if(flag < 15){
            hinttext.setText("第 "+(++flag-10)+" 道乘除法");
            MultiplyOrDiv();
        }
        else{
            hinttext.setText("第 "+(++flag-15)+" 道混合式");
            mix();
        }

    }
    public void AddOrSub(){
        int a, b;
        String str = null;
        a = random.nextInt(maxPlus-minPlus+1)+minPlus;
        b = random.nextInt(maxPlus-minPlus+1)+minPlus;
        if(random.nextInt(2) == 1){
            str = a+" + "+b+" =";
            calcstr.setText(str);
            result = a+b;
        }
        else{
            if(a < b){
                int tem = a;
                a = b;
                b = tem;
            }
            str = a+" - "+b+" =";
            calcstr.setText(str);
            result = abs(a-b);
        }

    }

    public void mod(){
        int a, b,c;
        String str = null;
        a = random.nextInt(maxMut-minMut+1)+minMut;
        b = random.nextInt(maxMut-minMut+1)+minMut;
        c = random.nextInt(a-1)+1;
        str = a*b+c+"/"+a+" 余数=";
        calcstr.setText(str);
        result = c;
    }
    public void MultiplyOrDiv(){
        int a, b;
        String str = null;
        a = random.nextInt(maxMut-minMut+1)+minMut;
        b = random.nextInt(maxMut-minMut+1)+minMut;
        if(random.nextInt(2) == 1){
            str = a+" * "+b+" =";
            calcstr.setText(str);
            result = a*b;
        }
        else{
            str = (a*b)+" / "+a+" =";
            calcstr.setText(str);
            result = b;
        }
    }

    public void mix(){
        int a, b, c, d,tem;
        String str = null;
        a = random.nextInt(maxMut-minMut+1)+minMut;
        b = random.nextInt(maxMut-minMut+1)+minMut;
        c = random.nextInt(maxPlus-minPlus+1)+minPlus;
        d = random.nextInt(maxPlus-minPlus+1)+minPlus;
        tem = random.nextInt(3);
        if(tem == 0){
            str = a+" * "+b+" + "+c+" =";
            calcstr.setText(str);
            result = a*b+c;
        }
        else if(tem == 1){
            str =d+" + "+ (a*b)+" / "+b+" =";
            calcstr.setText(str);
            result =d+a;
        }
        else if(tem == 2){
            if(d < (a*b+c)){
                str = c+" + "+a+" * "+b+" - "+d;
                calcstr.setText(str);
                result =c+a*b-d;
            }
            else if(d>(a*b)){
                str =d+" - "+ a+" * "+b+" + "+c+" =";
                calcstr.setText(str);
                result =d-a*b+c;
            }
        }
    }

    public void fixNum1(){
        if (minPlus > maxPlus) {
            int t = minPlus;
            minPlus = maxPlus;
            maxPlus = t;
        }
    }
    public void fixNum2(){
        if(minMut > maxMut){
            int t = minMut;
            minMut = maxMut;
            maxMut = t;
        }
    }
    void Dialog(){
        endDate = new Date(System.currentTimeMillis());
        long diff = endDate.getTime() - cur;
        long second = diff/1000;
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        isExit.setTitle("练习完成");
        // 设置对话框消息
        isExit.setMessage("本次用时"+second+"秒");

        if(second < 60){
            isExit.setMessage("本次用时"+second+"秒");
        }
        else if(second % 60 == 0){
            isExit.setMessage("本次用时"+second/60+"分钟");
        }
        else
            isExit.setMessage("本次用时"+second/60+"分钟"+second%60+"秒");
        // 添加选择按钮并注册监听
        isExit.setButton("重新开始", listener);
        isExit.setButton2("退出", listener);
        isExit.show();
    }
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    flag = 0;
                    cur = 0;
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    musicPlayer.stop();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void initSoundPool() {
        sp=new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        hm =new HashMap<Integer, Integer>();
        hm.put(1, sp.load(getApplicationContext(), R.raw.click, 1));

    }
    /** 播放即时音效*/
    private void playSound(int num,int loop){
        AudioManager am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        float currentSound=am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxSound=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume=currentSound/maxSound;
        currentID=sp.play(hm.get(num), volume, volume, 1, loop, 1.0f);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void change_dialog1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("输入新的数值：");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.change_dialog, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        min1 = (EditText)view.findViewById(R.id.min_change);
        max1 = (EditText)view.findViewById(R.id.max_change);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    minPlus = Integer.valueOf(min1.getText().toString()).intValue();
                    maxPlus =  Integer.valueOf(max1.getText().toString()).intValue();
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "要输入数字", Toast.LENGTH_SHORT).show();
                }

                    fixNum1();
                    if(minPlus <1){
                        minPlus = 1;
                        Toast.makeText(MainActivity.this, "最小值是1，不能再小啦", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, "修改成功！\n加减法从"+minPlus+"到"+maxPlus, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }
    public void change_dialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("输入新的数值：");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.change_dialog, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        min2 = (EditText)view.findViewById(R.id.min_change);
        max2 = (EditText)view.findViewById(R.id.max_change);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    minMut = Integer.valueOf(min2.getText().toString()).intValue();
                    maxMut =  Integer.valueOf(max2.getText().toString()).intValue();
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "要输入数字", Toast.LENGTH_SHORT).show();
                }

                    fixNum2();
                    if(minMut < 2){
                        minMut = 2;
                        Toast.makeText(MainActivity.this, "最小值为2，不能再小啦", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, "修改成功！\n乘除法从"+minMut+"到"+maxMut, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }

    public void about_dialog(){
        AlertDialog about = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.about_dialog, null);
        about.setView(view);
        about.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Add_change) {
            change_dialog1();

        } else if (id == R.id.Mut_change) {
            change_dialog2();

        }else if (id == R.id.about) {
            about_dialog();
        } else if (id == R.id.chat_robot) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
            musicPlayer.stop();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                musicPlayer.stop();
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
