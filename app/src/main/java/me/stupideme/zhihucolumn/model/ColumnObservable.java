package me.stupideme.zhihucolumn.model;

/**
 * Created by StupidL on 2016/11/13.
 */

public interface ColumnObservable {
    void attach(ColumnObserver observer);

    void detach(ColumnObserver observer);

    void notifyColumnObservers(Column column);

}
