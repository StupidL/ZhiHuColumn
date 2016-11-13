package me.stupideme.zhihucolumn.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.stupideme.zhihucolumn.R;

public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("文章详情");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView titleImage = (ImageView) findViewById(R.id.backdrop);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.article_fab);
        TextView title = (TextView) findViewById(R.id.article_title);
        TextView timeAndAuthor = (TextView) findViewById(R.id.article_time_and_author);
        WebView content = (WebView) findViewById(R.id.article_content);

        Intent intent = getIntent();
        String article_title = intent.getStringExtra("title");
        String article_imageUrl = intent.getStringExtra("imageUrl");
        String article_author = intent.getStringExtra("author");
        String article_published_time = intent.getStringExtra("published_time");
        String article_content = intent.getStringExtra("content");

        title.setText(article_title);
        Glide.with(this).load(article_imageUrl).into(titleImage);
        timeAndAuthor.setText(article_author + "  于  " + article_published_time.substring(0, 10));
        content.loadDataWithBaseURL(null, article_content, "text/html", "UTF-8", null);
    }
}