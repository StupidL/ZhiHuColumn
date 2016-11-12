package me.stupideme.zhihucolumn.presenter;

import android.os.Handler;
import android.os.Message;

import java.util.HashSet;
import java.util.Set;

import me.stupideme.zhihucolumn.Constants;
import me.stupideme.zhihucolumn.model.Column;
import me.stupideme.zhihucolumn.model.INetModel;
import me.stupideme.zhihucolumn.model.NetModelImp;
import me.stupideme.zhihucolumn.view.IColumnsView;

/**
 * Created by StupidL on 2016/11/12.
 */

public class ColumnsPresenter {

    private INetModel iNetModel;
    private IColumnsView iColumnsView;
    private Set<Column> mDataSet;
    private Handler mHandler;
    private static ColumnsPresenter INSTANCE;

    private ColumnsPresenter(IColumnsView view) {
        iColumnsView = view;
        mDataSet = new HashSet<>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                int what = message.what;
                if (what == Constants.MESSAGE_COLUMN_RESULT) {
                    mDataSet.add((Column) message.obj);
                }
            }
        };
        iNetModel = NetModelImp.getInstance(mHandler);

    }

    public static ColumnsPresenter getInstance(IColumnsView view) {
        if (INSTANCE == null) {
            synchronized (ColumnsPresenter.class) {
                if (INSTANCE == null)
                    INSTANCE = new ColumnsPresenter(view);
            }
        }
        return INSTANCE;
    }

    public void requestColumns(String[] names) {
        for (String name : names)
            iNetModel.requestColumn(name);
    }

    public void clearColumns(){
        mDataSet.clear();
    }

    public Set<Column> getColumns() {
        return mDataSet;
    }
}
