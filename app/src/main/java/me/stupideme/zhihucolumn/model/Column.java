package me.stupideme.zhihucolumn.model;


/**
 * Created by StupidL on 2016/8/20.
 */
public class Column {
    /**
     * name of column
     */
    private String name;

    /**
     * name of author
     */
    private String authorName;

    /**
     * url of avatar
     */
    private String avatarUrl;

    /**
     * follower count
     */
    private int followerCount;

    /**
     * description of column
     */
    private String description;

    /**
     * post count of column
     */
    private int postCount;

    /**
     * slug of column
     */
    private String slug;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
