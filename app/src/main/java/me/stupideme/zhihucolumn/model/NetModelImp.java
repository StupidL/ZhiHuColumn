package me.stupideme.zhihucolumn.model;

import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import me.stupideme.zhihucolumn.Constants;
import me.stupideme.zhihucolumn.db.DBManager;
import me.stupideme.zhihucolumn.util.ColumnName;
import me.stupideme.zhihucolumn.util.ParseJsonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by StupidL on 2016/11/12.
 */

public class NetModelImp implements INetModel {

    private static final String TAG = NetModelImp.class.getSimpleName();
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final long KEEP_ALIVE = 10L;

    private static ThreadFactory sThreadFactory;
    private static Executor mExecutor;

    private static NetModelImp INSTANCE;

    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private DBManager mManager;


    /**
     * private constructor
     */
    private NetModelImp(Handler handler) {

        //create thread factory
        sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                return new Thread(runnable, "NetModelImpThread#" + mCount.getAndIncrement());
            }
        };

        //create thread pool executor
        mExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),
                sThreadFactory
        );

        mOkHttpClient = new OkHttpClient();
        mHandler = handler;
        mManager = DBManager.getInstance();
    }

    /**
     * singleton pattern
     * get instance of NetModelImp
     *
     * @return instance
     */
    public static NetModelImp getInstance(Handler handler) {
        if (INSTANCE == null) {
            synchronized (NetModelImp.class) {
                if (INSTANCE == null)
                    INSTANCE = new NetModelImp(handler);
            }
        }
        return INSTANCE;
    }

    @Override
    public void requestColumn(final String columnName) {

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Cursor cursor = mManager.queryColumn(columnName);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String columnName = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_NAME));
                    String authorName = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_AUTHOR_NAME));
                    String avatarUrl = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_AVATAR_URL));
                    String followerCount = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_FOLLOWER_COUNT));
                    String postCount = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_POST_COUNT));
                    String description = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_DESCRIPTION));

                    Column column = new Column();
                    column.setName(columnName);
                    column.setAuthorName(authorName);
                    column.setAvatarUrl(avatarUrl);
                    column.setDescription(description);
                    column.setFollowerCount(Integer.parseInt(followerCount));
                    column.setPostCount(Integer.parseInt(postCount));
                    mHandler.obtainMessage(Constants.MESSAGE_COLUMN_RESULT, column);

                } else {
                    String url = ColumnName.BASE_URL + columnName;
                    Request request = new Request.Builder().url(url).build();
                    Response response;
                    try {
                        response = mOkHttpClient.newCall(request).execute();
                        final String result = response.body().string();
                        Log.v(TAG, "response body: " + result);

                        Column column = ParseJsonUtil.parseJsonToColumn(result);
                        mHandler.obtainMessage(Constants.MESSAGE_COLUMN_RESULT, column);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
        });
    }


    @Override
    public void requestArticles(String columnName, int limit, int offset) {
        final String url = ColumnName.BASE_URL + columnName + "/posts?limit=" + limit + "&offset=" + offset;
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).build();
                Response response;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        JSONArray articles = new JSONArray(body);
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject object = articles.getJSONObject(i);
                            Article article = ParseJsonUtil.parseJsonToArticle(object);
                            mHandler.obtainMessage(Constants.MESSAGE_ARTICLE_RESULT, article);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
