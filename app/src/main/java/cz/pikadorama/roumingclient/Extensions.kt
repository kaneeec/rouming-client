package cz.pikadorama.roumingclient

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.simpleorm.DaoManager

const val BUNDLE_KEY = "data"

fun Context.toast(messageResId: Int) = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
fun Fragment.toast(messageResId: Int) = Toast.makeText(this.activity, messageResId, Toast.LENGTH_SHORT).show()

fun Any.dao() = DaoManager.getDao(Topic::class.java)
fun Any.updateTopicsInDatabase(topics: List<Topic>, type: Topic.Type) {
    AsyncTask.execute {
        dao().findAll().filter { it.type == type }.forEach { dao().delete(it) }
        topics.forEach { dao().create(it) }
    }
}

fun Topic.toBundle(): Bundle {
    val bundle = Bundle()
    val serialized = listOf(id.toString(), posted, comments.toString(), upvotes.toString(), downvotes.toString(), link,
                            title, type.name).joinToString(separator = "###")
    bundle.putString(BUNDLE_KEY, serialized)
    return bundle
}
fun Bundle.toTopic(): Topic {
    val parts = getString(BUNDLE_KEY).split("###")
    return Topic(parts[0].toInt(), parts[1], parts[2].toInt(), parts[3].toInt(), parts[4].toInt(), parts[5],
                 parts[6], Topic.Type.valueOf(parts[7]))
}

fun ImageView.loadImage(topic: Topic) {
    Picasso.with(context)
            .load(Uri.parse(topic.imageDirectLink()))
            .fit().centerCrop()
            .placeholder(R.drawable.image_loading)
            .error(R.drawable.error_image_loading)
            .into(this)
}