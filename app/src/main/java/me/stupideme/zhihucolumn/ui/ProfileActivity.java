package me.stupideme.zhihucolumn.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupideme.zhihucolumn.App;
import me.stupideme.zhihucolumn.R;
import me.stupideme.zhihucolumn.bean.Article;
import me.stupideme.zhihucolumn.bean.Author;
import me.stupideme.zhihucolumn.bean.Avatar;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        Article article = App.articlesList.get(position);
        Author author = article.getAuthor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(author.getName());
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.super.onBackPressed();
            }
        });

        CircleImageView imageView = (CircleImageView) findViewById(R.id.user_head);
        TextView bio = (TextView) findViewById(R.id.user_bio);
        TextView profileLink = (TextView) findViewById(R.id.user_profile_link);
        TextView des = (TextView) findViewById(R.id.user_des);

        if (imageView != null) {
            String id = author.getAvatar().getId();
            String url = "https://pic1.zhimg.com/" + id + "_m.jpg";
            Glide.with(this).load(url).into(imageView);
        }
        if (bio != null) {
            bio.setText(author.getBio());
        }
        if (profileLink != null) {
            profileLink.setText(author.getProfileUrl());
        }
        if (des != null) {
            des.setText(author.getDescription());
        }

    }
}
