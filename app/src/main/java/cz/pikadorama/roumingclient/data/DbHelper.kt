package cz.pikadorama.roumingclient.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, "rouming.db", null, 1) {
    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {}
    override fun onCreate(p0: SQLiteDatabase) {}
}