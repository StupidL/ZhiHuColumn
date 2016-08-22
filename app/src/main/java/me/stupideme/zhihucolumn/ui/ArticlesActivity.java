package me.stupideme.zhihucolumn.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;

public class ArticlesActivity extends AppCompatActivity {

    private ArticlesFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("专栏文章");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticlesActivity.super.onBackPressed();
            }
        });

        mFragment = new ArticlesFragment();
        Intent intent = getIntent();
        String columnName = intent.getStringExtra("columnName");
        Bundle bundle = new Bundle();
        bundle.putString("columnName", columnName);
        mFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragment)
                .commit();
    }

}
