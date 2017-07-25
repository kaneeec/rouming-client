package cz.pikadorama.roumingclient.data

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import cz.pikadorama.simpleorm.dao.DaoQueryHelper

class TopicQueryHelper : DaoQueryHelper<Topic> {

    override fun getId(obj: Topic): Int {
        return obj.id!!
    }

    override fun setId(obj: Topic, id: Int) {
        obj.id = id
    }

    override fun cursorToObject(cursor: Cursor): Topic {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        val posted = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COL_POSTED))
        val comments = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COL_COMMENTS))
        val upvotes = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COL_UPVOTES))
        val downvotes = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COL_DOWNVOTES))
        val link = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COL_LINK))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COL_TITLE))
        val type = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COL_TYPE))
        return Topic(id, posted, comments, upvotes, downvotes, link, title, Topic.Type.valueOf(type))
    }

    override fun objectToContentValues(obj: Topic): ContentValues {
        val cv = ContentValues()
        cv.put(BaseColumns._ID, obj.id)
        cv.put(Topic.COL_POSTED, obj.posted)
        cv.put(Topic.COL_COMMENTS, obj.comments)
        cv.put(Topic.COL_UPVOTES, obj.upvotes)
        cv.put(Topic.COL_DOWNVOTES, obj.downvotes)
        cv.put(Topic.COL_LINK, obj.link)
        cv.put(Topic.COL_TITLE, obj.title)
        cv.put(Topic.COL_TYPE, obj.type.name)
        return cv
    }

}