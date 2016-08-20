package me.stupideme.zhihucolumn;

import android.app.Application;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import me.stupideme.zhihucolumn.bean.Article;
import me.stupideme.zhihucolumn.bean.Column;
import me.stupideme.zhihucolumn.net.LoadColumnsService;

/**
 * Created by StupidL on 2016/8/20.
 */

public class App extends Application {
    public static List<Column> columnsList = new ArrayList<>();
    public static List<Article> articlesList= new ArrayList<>();
    @Override
    public void onCreate(){
        startService(new Intent(this, LoadColumnsService.class));
    }


}
