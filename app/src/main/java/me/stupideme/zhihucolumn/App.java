package me.stupideme.zhihucolumn;

import android.app.Application;
import java.util.ArrayList;
import java.util.List;

import me.stupideme.zhihucolumn.db.DBManager;
import me.stupideme.zhihucolumn.model.Article;
import me.stupideme.zhihucolumn.model.Column;

/**
 * Created by StupidL on 2016/8/20.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        DBManager.init(getApplicationContext());
    }


}
