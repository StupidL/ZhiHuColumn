package me.stupideme.zhihucolumn.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.net.GetData;

public class SplashActivity extends AppCompatActivity {
    private MyReceiver mReceiver;
    private static final String IMAGE_URL = "http://news-at.zhihu.com/api/4/start-image/1080*1776";
    private static String startImageUrl;
    private static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        final ImageView imageView = (ImageView) findViewById(R.id.splash_image);

        final Animation animation = new ScaleAnimation(1.0F,1.2F,1.0F,1.2F, ScaleAnimation.RELATIVE_TO_SELF,0.5F, ScaleAnimation.RELATIVE_TO_SELF,0.5F);
        animation.setDuration(3000);
        if (imageView != null) {
            imageView.setAnimation(animation);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == 0x100){
                   Bundle bundle= message.getData();
                    startImageUrl = bundle.getString("url");
                    System.out.println("======"+startImageUrl);
                    try {
                        JSONObject object = new JSONObject(startImageUrl);
                        String imageUrl = object.getString("img");
                        System.out.println("++++"+imageUrl);
                        Glide.with(SplashActivity.this).load(imageUrl).into(imageView);
                        animation.startNow();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = GetData.getStartImageUrl(IMAGE_URL);
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("url",url);
                    message.setData(bundle);
                    message.what = 0x100;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ColumnsFragment.COLUMN_ACTION);
        registerReceiver(mReceiver, filter);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ColumnsActivity.class));
                SplashActivity.this.finish();
            }
        }, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            startActivity(new Intent(SplashActivity.this, ColumnsActivity.class));
            SplashActivity.this.finish();
        }
    }
}
