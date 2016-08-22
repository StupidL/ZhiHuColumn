package me.stupideme.zhihucolumn.bean;


import java.util.List;

/**
 * Created by StupidL on 2016/8/20.
 */
public class Column {
    private int followerCount;          //关注着数量
    private String description;         //简介
    private Author creator;             //创造者
    private Avatar avatar;              //头像
    private List<Topic> topics;             //话题
    private String activateState;
    private String href;
    private boolean acceptSubmission;
    private String slug;
    private String name;                //专栏名字？
    private String url;
    private String commentPermission;
    private boolean following;
    private int postCount;
    private boolean canPost;
    private String activateAuthorRequested;

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getCreator() {
        return creator;
    }

    public void setCreator(Author creator) {
        this.creator = creator;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isAcceptSubmission() {
        return acceptSubmission;
    }

    public void setAcceptSubmission(boolean acceptSubmission) {
        this.acceptSubmission = acceptSubmission;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getCommentPermission() {
        return commentPermission;
    }

    public void setCommentPermission(String commentPermission) {
        this.commentPermission = commentPermission;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public boolean isCanPost() {
        return canPost;
    }

    public void setCanPost(boolean canPost) {
        this.canPost = canPost;
    }

    public String  isActivateAuthorRequested() {
        return activateAuthorRequested;
    }

    public void setActivateAuthorRequested(String  activateAuthorRequested) {
        this.activateAuthorRequested = activateAuthorRequested;
    }

    public String getActivateState() {
        return activateState;
    }

    public void setActivateState(String activateState) {
        this.activateState = activateState;
    }
}
