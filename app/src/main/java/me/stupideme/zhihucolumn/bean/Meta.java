package me.stupideme.zhihucolumn.bean;

/**
 * Created by StupidL on 2016/8/20.
 */

public class Meta {
    private Article previous;
    private Article next;

    public Article getPrevious() {
        return previous;
    }

    public void setPrevious(Article previous) {
        this.previous = previous;
    }

    public Article getNext() {
        return next;
    }

    public void setNext(Article next) {
        this.next = next;
    }

}
