package cz.pikadorama.roumingclient

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import cz.pikadorama.roumingclient.data.TopicDao
import cz.pikadorama.simpleorm.DbManager
import cz.pikadorama.simpleorm.dao.DaoQueryHelper


class OrmEnabledApplication : Application() {
    override fun onCreate() {
        try {
            DbManager.registerHelper(DbHelper(this), TopicDao::class.java)
        } catch(e: Exception) {
            // catching exception that occurs when trying to recreate existing table
        }
    }
}

class DbHelper(context: Context) : SQLiteOpenHelper(context, "rouming.db", null, 1) {
    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {}
    override fun onCreate(p0: SQLiteDatabase) {}
}

class TopicQueryHelper : DaoQueryHelper<TopicDao> {

    override fun getId(obj: TopicDao): Int {
        return obj.id
    }

    override fun setId(obj: TopicDao, id: Int) {
        obj.id = id
    }

    override fun cursorToObject(cursor: Cursor): TopicDao {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        val posted = cursor.getString(cursor.getColumnIndexOrThrow(TopicDao.COL_POSTED))
        val comments = cursor.getInt(cursor.getColumnIndexOrThrow(TopicDao.COL_COMMENTS))
        val upvotes = cursor.getInt(cursor.getColumnIndexOrThrow(TopicDao.COL_UPVOTES))
        val downvotes = cursor.getInt(cursor.getColumnIndexOrThrow(TopicDao.COL_DOWNVOTES))
        val link = cursor.getString(cursor.getColumnIndexOrThrow(TopicDao.COL_LINK))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(TopicDao.COL_TITLE))
        return TopicDao(id, posted, comments, upvotes, downvotes, link, title)
    }

    override fun objectToContentValues(obj: TopicDao): ContentValues {
        val cv = ContentValues()
        cv.put(BaseColumns._ID, obj.id)
        cv.put(TopicDao.COL_POSTED, obj.posted)
        cv.put(TopicDao.COL_COMMENTS, obj.comments)
        cv.put(TopicDao.COL_UPVOTES, obj.upvotes)
        cv.put(TopicDao.COL_DOWNVOTES, obj.downvotes)
        cv.put(TopicDao.COL_LINK, obj.link)
        cv.put(TopicDao.COL_TITLE, obj.title)
        return cv
    }

}

