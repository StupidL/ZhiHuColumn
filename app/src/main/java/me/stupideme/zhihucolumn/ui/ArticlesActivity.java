package me.stupideme.zhihucolumn.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.stupideme.zhihucolumn.R;

public class ArticlesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ArticlesFragment())
                .commit();
    }
}
