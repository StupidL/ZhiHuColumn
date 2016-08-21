package me.stupideme.zhihucolumn;

import android.app.Application;
import java.util.ArrayList;
import java.util.List;

import me.stupideme.zhihucolumn.bean.Article;
import me.stupideme.zhihucolumn.bean.Column;

/**
 * Created by StupidL on 2016/8/20.
 */

public class App extends Application {
    public static List<Column> columnsList = new ArrayList<>();
    public static List<Article> articlesList = new ArrayList<>();

    @Override
    public void onCreate() {
    }


}
