package me.stupideme.zhihucolumn.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.stupideme.zhihucolumn.Constants;
import me.stupideme.zhihucolumn.model.Article;
import me.stupideme.zhihucolumn.model.Column;


/**
 * Created by StupidL on 2016/11/12.
 */

public class DBManager {
    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;
    private static Context mContext;
    private static DBManager INSTANCE;

    private DBManager() {
        mHelper = new DBHelper(mContext);
        mDatabase = mHelper.getReadableDatabase();
    }

    public static void init(Context context) {
        mContext = context;
    }

    public static DBManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DBManager.class) {
                if (INSTANCE == null)
                    INSTANCE = new DBManager();
            }
        }
        return INSTANCE;
    }

    public void insertColumn(ContentValues values) {
        mDatabase.insert(Constants.TABLE_NAME_OF_COLUMN, null, values);
    }

    public void insertColumn(Column column) {
        String columnName = column.getName();
        String authorName = column.getAuthorName();
        String avatarUrl = column.getAvatarUrl();
        String followerCount = String.valueOf(column.getFollowerCount());
        String postCount = String.valueOf(column.getPostCount());
        String description = column.getDescription();
        String slug = column.getSlug();
        mDatabase.execSQL("INSERT OR IGNORE INTO "
                + Constants.TABLE_NAME_OF_COLUMN + " ("
                + Constants.TABLE_COLUMN_NAME + ", "
                + Constants.TABLE_COLUMN_AUTHOR_NAME + ", "
                + Constants.TABLE_COLUMN_AVATAR_URL + ", "
                + Constants.TABLE_COLUMN_FOLLOWER_COUNT + ", "
                + Constants.TABLE_COLUMN_POST_COUNT + ", "
                + Constants.TABLE_COLUMN_DESCRIPTION + ", "
                + Constants.TABLE_COLUMN_SLUG + ") "
                + "VALUES ("
                + "'" + columnName + "', "
                + "'" + authorName + "', "
                + "'" + avatarUrl + "', "
                + "'" + followerCount + "', "
                + "'" + postCount + "',"
                + "'" + description + "',"
                + "'" + slug + "'"
                + ")");
    }

    public void insertColumn(List<Column> list) {
        mDatabase.beginTransaction();
        try {
            for (Column column : list) {
                insertColumn(column);
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

    }

    public Cursor queryColumn(String columnName) {
        return mDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_NAME_OF_COLUMN +
                " WHERE " + Constants.TABLE_COLUMN_NAME + " = ?", new String[]{columnName});
    }

    public Cursor queryAllColumns() {
        return mDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_NAME_OF_COLUMN + " WHERE _id >= ?", new String[]{"0"});
    }

    public void insertArticle(ContentValues values) {
        mDatabase.insert(Constants.TABLE_NAME_OF_ARTICLE, null, values);
    }

    public void insertArticle(Article article) {
        String columnName = article.getColumnName();
        String authorName = article.getAuthor().getName();
        String title = article.getTitle();
        String url = article.getUrl();
        String titleImageUrl = article.getTitleImageUrl();
        String publishedTime = article.getPublishedTime();
        String commentsCount = String.valueOf(article.getCommentsCount());
        String likesCount = String.valueOf(article.getLikesCount());
        String content = article.getContent();

        mDatabase.execSQL("INSERT OR IGNORE INTO " + Constants.TABLE_NAME_OF_ARTICLE + " ("
                + Constants.TABLE_ARTICLE_COLUMN_NAME + ", "
                + Constants.TABLE_ARTICLE_AUTHOR_NAME + ", "
                + Constants.TABLE_ARTICLE_TITLE + ", "
                + Constants.TABLE_ARTICLE_URL + ", "
                + Constants.TABLE_ARTICLE_TITLE_IMAGE_URL + ", "
                + Constants.TABLE_ARTICLE_PUBLISHED_TIME + ", "
                + Constants.TABLE_ARTICLE_COMMENTS_COUNT + ", "
                + Constants.TABLE_ARTICLE_LIKES_COUNT + ", "
                + Constants.TABLE_ARTICLE_CONTENT + ") "
                + "VALUES ("
                + "'" + columnName + "', "
                + "'" + authorName + "', "
                + "'" + title + "', "
                + "'" + url + "', "
                + "'" + titleImageUrl + "', "
                + "'" + publishedTime + "', "
                + "'" + commentsCount + "', "
                + "'" + likesCount + "', "
                + "'" + content + "'"
                + ")");
    }

    public void insertArticle(List<Article> list) {
        mDatabase.beginTransaction();
        try {
            for (Article article : list) {
                insertArticle(article);
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    public Cursor queryAllArticlesInColumn(String name) {
        return mDatabase.rawQuery("SELECT * FROM " + Constants.TABLE_NAME_OF_ARTICLE + " WHERE " +
                Constants.TABLE_ARTICLE_COLUMN_NAME + " = ?", new String[]{name});
    }
}
