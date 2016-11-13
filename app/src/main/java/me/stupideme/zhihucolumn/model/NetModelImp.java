package me.stupideme.zhihucolumn.model;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
    private DBManager mManager;
    private List<ColumnObserver> mColumnObservers;
    private List<ArticleObserver> mArticleObservers;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == Constants.MESSAGE_COLUMN_RESULT) {
                Column column = (Column) message.obj;
                notifyColumnObservers(column);
                Log.v(TAG, "handler notify column");
            } else if (message.what == Constants.MESSAGE_ARTICLE_RESULT) {
                Article article = (Article) message.obj;
                notifyArticleObservers(article);
                Log.v(TAG, "handler notify article");
            }
        }
    };


    /**
     * private constructor
     */
    private NetModelImp() {

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
        mManager = DBManager.getInstance();
        mColumnObservers = new ArrayList<>();
        mArticleObservers = new ArrayList<>();

    }

    /**
     * singleton pattern
     * get instance of NetModelImp
     *
     * @return instance
     */
    public static NetModelImp getInstance() {
        if (INSTANCE == null) {
            synchronized (NetModelImp.class) {
                if (INSTANCE == null)
                    INSTANCE = new NetModelImp();
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

                    Message message = Message.obtain();
                    message.what = Constants.MESSAGE_COLUMN_RESULT;
                    message.obj = column;
                    mHandler.sendMessage(message);
                    Log.w(TAG, "send column message from database by handler");

                } else {
                    String url = ColumnName.BASE_URL + columnName;
                    Request request = new Request.Builder().url(url).build();
                    Response response;
                    try {
                        response = mOkHttpClient.newCall(request).execute();

                        final String result = response.body().string();
                        Log.v(TAG, "response body: " + result);

                        Column column = ParseJsonUtil.parseJsonToColumn(result);

                        mManager.insertColumn(column);
                        Log.w(TAG, "insert column into database");

                        Message message = Message.obtain();
                        message.what = Constants.MESSAGE_COLUMN_RESULT;
                        message.obj = column;
                        mHandler.sendMessage(message);
                        Log.w(TAG, "send column message from net by handler");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
        });
    }

    @Override
    public void requestArticles(final String columnName, final String url) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = mManager.queryAllArticlesInColumn(columnName);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String columnName2 = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_COLUMN_NAME));
                        String title = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_TITLE));
                        String url = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_URL));
                        String imageUrl = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_TITLE_IMAGE_URL));
                        String authorName = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_AUTHOR_NAME));
                        String publishedTime = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_PUBLISHED_TIME));
                        String content = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_CONTENT));
                        String commentsCount = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_COMMENTS_COUNT));
                        String likesCount = cursor.getString(cursor.getColumnIndex(Constants.TABLE_ARTICLE_LIKES_COUNT));

                        Article article = new Article();
                        article.setColumnName(columnName2);
                        article.setTitle(title);
                        article.setUrl(url);
                        article.setTitleImageUrl(imageUrl);
                        article.setAuthorName(authorName);
                        article.setPublishedTime(publishedTime);
                        article.setContent(content);
                        article.setCommentsCount(Integer.parseInt(commentsCount));
                        article.setLikesCount(Integer.parseInt(likesCount));

                        Message message = Message.obtain();
                        message.what = Constants.MESSAGE_ARTICLE_RESULT;
                        message.obj = article;
                        mHandler.sendMessage(message);
                        Log.w(TAG, "send article message from database by handler");
                        cursor.moveToNext();
                    }

                } else {
                    Request request = new Request.Builder().url(url).build();
                    Response response;
                    try {
                        response = mOkHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String body2 = response.body().string();

                            byte[] bytes = body2.getBytes("UTF-16");
                            String body = new String(bytes, "UTF-16");

                            JSONArray articles = new JSONArray(body);
                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject object = articles.getJSONObject(i);
                                Article article = ParseJsonUtil.parseJsonToArticle(object);
                                article.setColumnName(columnName);

                                mManager.insertArticle(article);
                                Log.w(TAG, "insert article into database");

                                Message message = new Message();
                                message.what = Constants.MESSAGE_ARTICLE_RESULT;
                                message.obj = article;
                                mHandler.sendMessage(message);
                                Log.w(TAG, "send article message from net by handler");
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void attach(ColumnObserver observer) {
        mColumnObservers.add(observer);
    }

    @Override
    public void detach(ColumnObserver observer) {
        mColumnObservers.remove(observer);
    }

    @Override
    public void notifyColumnObservers(Column column) {
        for (ColumnObserver o : mColumnObservers) {
            o.onReceiveColumn(column);
            Log.v(TAG, "notify column observers");
        }
    }

    @Override
    public void attach(ArticleObserver observer) {
        mArticleObservers.add(observer);
    }

    @Override
    public void detach(ArticleObserver observer) {
        mArticleObservers.remove(observer);
    }

    @Override
    public void notifyArticleObservers(Article article) {
        for (ArticleObserver o : mArticleObservers) {
            o.onReceiveArticle(article);
            Log.v(TAG, "notify article observers");
        }
    }
}
