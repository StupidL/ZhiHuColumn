package me.stupideme.zhihucolumn.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.stupideme.zhihucolumn.Constants;
import me.stupideme.zhihucolumn.R;

public class StupidArticlesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stupid_articles);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("专栏文章");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String slug = intent.getStringExtra(Constants.EXTRA_COLUMN_SLUG);
        StupidArticlesFragment mFragment = StupidArticlesFragment.newInstance(slug);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();



    }
}
