package cz.pikadorama.roumingclient

import android.app.Fragment
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.simpleorm.DaoManager

fun Context.toast(messageResId: Int) = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
fun Fragment.toast(messageResId: Int) = Toast.makeText(this.activity, messageResId, Toast.LENGTH_SHORT).show()

fun Any.dao() = DaoManager.getDao(Topic::class.java)
fun Any.updateTopicsInDatabase(topics: List<Topic>, type: Topic.Type) {
    AsyncTask.execute {
        dao().findAll().filter { it.type == type }.forEach { dao().delete(it) }
        topics.forEach { dao().create(it) }
    }
}