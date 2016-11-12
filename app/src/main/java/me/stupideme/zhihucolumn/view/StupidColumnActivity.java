package me.stupideme.zhihucolumn.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.stupideme.zhihucolumn.R;

public class StupidColumnActivity extends AppCompatActivity {

    private StupidColumnFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stupid_column);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mFragment = StupidColumnFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
    }
}
