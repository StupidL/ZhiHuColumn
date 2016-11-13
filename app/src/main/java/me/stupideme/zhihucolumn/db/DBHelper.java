package me.stupideme.zhihucolumn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.stupideme.zhihucolumn.Constants;

/**
 * Created by StupidL on 2016/11/12.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "zhihu_column.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_COLUMNS = "CREATE TABLE " +
            Constants.TABLE_NAME_OF_COLUMN + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT" + ", " +
            Constants.TABLE_COLUMN_NAME + " UNIQUE , " +
            Constants.TABLE_COLUMN_AUTHOR_NAME + ", " +
            Constants.TABLE_COLUMN_AVATAR_URL + ", " +
            Constants.TABLE_COLUMN_FOLLOWER_COUNT + ", " +
            Constants.TABLE_COLUMN_POST_COUNT + ", " +
            Constants.TABLE_COLUMN_DESCRIPTION + ", " +
            Constants.TABLE_COLUMN_SLUG +
            ")";

    private static final String CREATE_TABLE_ARTICLES = "CREATE TABLE " +
            Constants.TABLE_NAME_OF_ARTICLE + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT" + ", " +
            Constants.TABLE_ARTICLE_COLUMN_NAME + ", " +
            Constants.TABLE_ARTICLE_TITLE + " UNIQUE , " +
            Constants.TABLE_ARTICLE_URL + ", " +
            Constants.TABLE_ARTICLE_TITLE_IMAGE_URL + ", " +
            Constants.TABLE_ARTICLE_AUTHOR_NAME + ", " +
            Constants.TABLE_ARTICLE_PUBLISHED_TIME + ", " +
            Constants.TABLE_ARTICLE_CONTENT + ", " +
            Constants.TABLE_ARTICLE_COMMENTS_COUNT + ", " +
            Constants.TABLE_ARTICLE_LIKES_COUNT + " " +
            ")";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_COLUMNS);
        sqLiteDatabase.execSQL(CREATE_TABLE_ARTICLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
