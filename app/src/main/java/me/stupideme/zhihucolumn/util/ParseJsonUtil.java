package me.stupideme.zhihucolumn.util;

import org.json.JSONException;
import org.json.JSONObject;

import me.stupideme.zhihucolumn.model.Article;
import me.stupideme.zhihucolumn.model.Author;
import me.stupideme.zhihucolumn.model.Avatar;
import me.stupideme.zhihucolumn.model.Column;
import me.stupideme.zhihucolumn.model.Comment;
import me.stupideme.zhihucolumn.model.Link;
import me.stupideme.zhihucolumn.model.Meta;
import me.stupideme.zhihucolumn.model.Topic;

/**
 * Created by StupidL on 2016/8/22.
 */

public class ParseJsonUtil {

    public static Column parseJsonToColumn(String json) {
        try {
            JSONObject root = new JSONObject(json);
            return parseJsonToColumn(root);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Column parseJsonToColumn(JSONObject root) throws JSONException {
        Column column = new Column();

        //set name of column
        column.setName(root.getString("name"));

        //set author name of column
        JSONObject jsonCreator = root.getJSONObject("creator");
        Author creator = parseJsonToAuthor(jsonCreator);
        column.setAuthorName(creator.getName());

        //set avatar url
        JSONObject jsonAvatar = root.getJSONObject("avatar");
        Avatar avatar = parseJsonToAvatar(jsonAvatar);
        String id = avatar.getId();
        String avatarUrl = "https://pic1.zhimg.com/" + id + "_m.jpg";
        column.setAvatarUrl(avatarUrl);

        //set follower count
        column.setFollowerCount(root.getInt("followersCount"));
        //set post count
        column.setPostCount(root.getInt("postsCount"));
        //set description
        column.setDescription(root.getString("description"));
        //set slug
        column.setSlug(root.getString("slug"));

        return column;
    }


    public static Author parseJsonToAuthor(String json) {
        try {
            JSONObject root = new JSONObject(json);
            return parseJsonToAuthor(root);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Author parseJsonToAuthor(JSONObject root) throws JSONException {
        Author author = new Author();
        author.setName(root.getString("name"));
        author.setBio(root.getString("bio"));
        author.setProfileUrl(root.getString("profileUrl"));
        author.setHash(root.getString("hash"));
        author.setSlug(root.getString("slug"));
        author.setOrg(root.getBoolean("isOrg"));
        author.setDescription(root.getString("description"));
        JSONObject jsonAvatar = root.getJSONObject("avatar");
        Avatar avatar = parseJsonToAvatar(jsonAvatar);
        author.setAvatar(avatar);
        return author;
    }

    public static Avatar parseJsonToAvatar(String json) {
        try {
            JSONObject root = new JSONObject(json);
            return parseJsonToAvatar(root);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Avatar parseJsonToAvatar(JSONObject root) {
        Avatar avatar = new Avatar();
        try {
            avatar.setId(root.getString("id"));
            avatar.setTemplate(root.getString("template"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return avatar;
    }

    public static Link parseJsonToLink(String json) {
        JSONObject root = null;
        try {
            root = new JSONObject(json);
            return parseJsonToLink(root);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Link parseJsonToLink(JSONObject root) {
        Link link = new Link();
        try {
            link.setComments(root.getString("comments"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return link;
    }

    public static Article parseJsonToArticle(String json) {
        try {
            JSONObject root = new JSONObject(json);
            return parseJsonToArticle(root);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Article parseJsonToArticle(JSONObject root) {
        Article article = new Article();
        try {
            article.setTitleImageFullScreen(root.getBoolean("isTitleImageFullScreen"));
            article.setRating(root.getString("rating"));
            article.setTitleImage(root.getString("titleImage"));
            JSONObject jsonLink = root.getJSONObject("links");
            Link link = parseJsonToLink(jsonLink);
            article.setLinks(link);
//            List<Topic> topics = new ArrayList<>();
//            JSONArray jsonTopics = root.getJSONArray("topics");
//            for (int i = 0; i < jsonTopics.length(); i++) {
//                JSONObject object = jsonTopics.getJSONObject(i);
//                Topic topic = parseJsonToTopic(object);
//                topics.add(topic);
//            }
//            article.setTopics(topics);
            article.setHref(root.getString("href"));
            JSONObject jsonAuthor = root.getJSONObject("author");
            article.setAuthor(parseJsonToAuthor(jsonAuthor));
            article.setContent(root.getString("content"));
            article.setState(root.getString("state"));
            article.setCanComment(root.getBoolean("canComment"));
            article.setSnapshotUrl(root.getString("snapshotUrl"));
            article.setSlug(root.getString("slug"));
            article.setPublishedTime(root.getString("publishedTime"));
            article.setTitle(root.getString("title"));
            article.setUrl(root.getString("url"));
            article.setSummary(root.getString("summary"));
            JSONObject jsonMeta = root.getJSONObject("meta");
            article.setMeta(parseJsonToMeta(jsonMeta));
            article.setCommentPermission(root.getString("commentPermission"));
            article.setCommentsCount(root.getInt("commentsCount"));
            article.setLikesCount(root.getInt("likesCount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return article;
    }

    public static Comment aprseJsonToComment(String json) {
        Comment comment = new Comment();
        return comment;
    }

    public static Comment parseJsonToComment(JSONObject object) {
        Comment comment = new Comment();
        return comment;
    }

    public static Meta parseJsonToMeta(String json) {
        Meta meta = new Meta();
        return meta;
    }

    public static Meta parseJsonToMeta(JSONObject object) {
        Meta meta = new Meta();
        return meta;
    }

    public static Article parseJsonToNext(String json) {
        return parseJsonToArticle(json);
    }

    public static Article parseJsonToNext(JSONObject object) {
        return parseJsonToArticle(object);
    }

    public static Article parseJsonToPrevious(String json) {
        return parseJsonToArticle(json);
    }

    public static Article parseJsonPrevious(JSONObject object) {
        return parseJsonToArticle(object);
    }

    public static Topic parseJsonToTopic(String json) {
        try {
            JSONObject root = new JSONObject(json);
            return parseJsonToTopic(root);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Topic parseJsonToTopic(JSONObject root) {
        Topic topic = new Topic();
        try {
            topic.setId(root.getString("id"));
            topic.setUrl(root.getString("url"));
            topic.setName(root.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topic;
    }

}
