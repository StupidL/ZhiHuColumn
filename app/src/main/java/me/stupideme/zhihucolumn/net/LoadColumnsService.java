package me.stupideme.zhihucolumn.net;

import android.app.IntentService;
import android.content.Intent;

import me.stupideme.zhihucolumn.ui.ColumnsFragment;

public class LoadColumnsService extends IntentService {

    public LoadColumnsService() {
        super("LoadColumnsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get data from internet

//
//        Intent i = new Intent();
//        i.setAction(ColumnsFragment.COLUMN_ACTION);
//        sendBroadcast(i);
    }

}
