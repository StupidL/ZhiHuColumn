package me.stupideme.zhihucolumn.model;

/**
 * Created by StupidL on 2016/11/12.
 */

public interface INetModel {
    void requestColumn(String columnName);
    void requestArticles(String columnName,int limit,int offset);
}
