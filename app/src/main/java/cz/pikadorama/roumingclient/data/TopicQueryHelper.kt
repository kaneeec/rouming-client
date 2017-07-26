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
        with(cursor) {
            val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
            val posted = getString(getColumnIndexOrThrow(Topic.COL_POSTED))
            val comments = getInt(getColumnIndexOrThrow(Topic.COL_COMMENTS))
            val upvotes = getInt(getColumnIndexOrThrow(Topic.COL_UPVOTES))
            val downvotes = getInt(getColumnIndexOrThrow(Topic.COL_DOWNVOTES))
            val link = getString(getColumnIndexOrThrow(Topic.COL_LINK))
            val title = getString(getColumnIndexOrThrow(Topic.COL_TITLE))
            val type = getString(getColumnIndexOrThrow(Topic.COL_TYPE))
            return Topic(id, posted, comments, upvotes, downvotes, link, title, Topic.Type.valueOf(type))
        }
    }

    override fun objectToContentValues(obj: Topic): ContentValues {
        val cv = ContentValues()
        with(cv) {
            put(BaseColumns._ID, obj.id)
            put(Topic.COL_POSTED, obj.posted)
            put(Topic.COL_COMMENTS, obj.comments)
            put(Topic.COL_UPVOTES, obj.upvotes)
            put(Topic.COL_DOWNVOTES, obj.downvotes)
            put(Topic.COL_LINK, obj.link)
            put(Topic.COL_TITLE, obj.title)
            put(Topic.COL_TYPE, obj.type.name)
        }
        return cv
    }

}