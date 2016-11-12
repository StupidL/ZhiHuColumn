package me.stupideme.zhihucolumn.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.model.Article;

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
                ArticleDetailActivity.super.onBackPressed();
            }
        });

        ImageView titleImage = (ImageView) findViewById(R.id.backdrop);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.article_fab);
        TextView title = (TextView) findViewById(R.id.article_title);
        TextView timeAndAuthor = (TextView) findViewById(R.id.article_time_and_author);
        WebView content = (WebView) findViewById(R.id.article_content);

        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);
        Article article = App.articlesList.get(position);

        if (titleImage != null) {
            Glide.with(this).load(article.getTitleImage()).into(titleImage);
        }
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ArticleDetailActivity.this, ProfileActivity.class);
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        }

        if (title != null) {
            title.setText(article.getTitle());
        }
        if (timeAndAuthor != null) {
            timeAndAuthor.setText(article.getAuthor().getName() + "  于  " + article.getPublishedTime().substring(0, 10));
        }
        if (content != null) {
            content.loadDataWithBaseURL(null, article.getContent(), "text/html", "UTF-8", null);
        }
    }
}
