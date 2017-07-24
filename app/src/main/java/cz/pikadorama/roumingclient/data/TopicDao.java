package cz.pikadorama.roumingclient.data;

import android.provider.BaseColumns;

import cz.pikadorama.roumingclient.TopicQueryHelper;
import cz.pikadorama.simpleorm.DbDataType;
import cz.pikadorama.simpleorm.annotation.DbColumn;
import cz.pikadorama.simpleorm.annotation.DbTable;

@DbTable(name = "Topic", mappingClass = TopicQueryHelper.class)
public class TopicDao {

    public static final String COL_POSTED = "posted";
    public static final String COL_COMMENTS = "comments";
    public static final String COL_UPVOTES = "upvotes";
    public static final String COL_DOWNVOTES = "downvotes";
    public static final String COL_LINK = "link";
    public static final String COL_TITLE = "title";

    @DbColumn(name = BaseColumns._ID, type = DbDataType.INTEGER, properties = "primary key autoincrement")
    Integer id;
    @DbColumn(name = COL_POSTED, type = DbDataType.TEXT)
    String posted;
    @DbColumn(name = COL_COMMENTS, type = DbDataType.INTEGER)
    int comments;
    @DbColumn(name = COL_UPVOTES, type = DbDataType.INTEGER)
    int upvotes;
    @DbColumn(name = COL_DOWNVOTES, type = DbDataType.INTEGER)
    int downvotes;
    @DbColumn(name = COL_LINK, type = DbDataType.TEXT)
    String link;
    @DbColumn(name = COL_TITLE, type = DbDataType.TEXT)
    String title;

    public TopicDao(Integer id, String posted, int comments, int upvotes, int downvotes, String link, String title) {
        this.id = id;
        this.posted = posted;
        this.comments = comments;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.link = link;
        this.title = title;
    }

    public TopicDao(String posted, int comments, int upvotes, int downvotes, String link, String title) {
        this(null, posted, comments, upvotes, downvotes, link, title);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Topic toTopic() {
        return new Topic(posted, comments, upvotes, downvotes, link, title);
    }
}
