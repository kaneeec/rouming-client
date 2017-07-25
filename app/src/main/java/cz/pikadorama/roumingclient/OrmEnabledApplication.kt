package cz.pikadorama.roumingclient

import android.app.Application
import cz.pikadorama.roumingclient.data.DbHelper
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.simpleorm.DbManager


class OrmEnabledApplication : Application() {
    override fun onCreate() {
        try {
            DbManager.registerHelper(DbHelper(this), Topic::class.java)
        } catch(e: Exception) {
            // catching exception that occurs when trying to recreate existing table
        }
    }
}
