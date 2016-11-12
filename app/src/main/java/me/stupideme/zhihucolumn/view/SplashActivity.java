package me.stupideme.zhihucolumn.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.stupideme.zhihucolumn.R;

public class SplashActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "http://news-at.zhihu.com/api/4/start-image/1080*1776";
    private static String startImageUrl;
    private static Handler mHandler;
    private Runnable mRunnable;

    private static final String DEF_CHATSET = "UTF-8";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        final ImageView imageView = (ImageView) findViewById(R.id.splash_image);
        TextView appName = (TextView) findViewById(R.id.app_name);
        TextView author = (TextView) findViewById(R.id.author_name);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String url = getStartImageUrl(IMAGE_URL);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = 0x100;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what == 0x100) {
                    Bundle bundle = message.getData();
                    startImageUrl = bundle.getString("url");
                    try {
                        JSONObject object = new JSONObject(startImageUrl);
                        String imageUrl = object.getString("img");
                        Glide.with(SplashActivity.this).load(imageUrl).into(imageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        mRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ColumnsActivity.class));
                SplashActivity.this.finish();
            }
        };

        if (hasNetwork(this)) {
            new Thread(runnable).start();
            mHandler.postDelayed(mRunnable, 2000);
        } else {
            imageView.setImageResource(R.drawable.splash_network);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            appName.setText("SORRY!");
            author.setText("无网络连接");
        }
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }


    public static String getStartImageUrl(String strUrl) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
}
