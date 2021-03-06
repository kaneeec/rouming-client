package cz.pikadorama.roumingclient

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.simpleorm.DaoManager


const val BUNDLE_KEY = "data"

fun Context.toast(messageResId: Int) = toast(messageResId, this)
fun Fragment.toast(messageResId: Int) = toast(messageResId, this.activity)
private fun toast(messageResId: Int, context: Context) = Toast.makeText(context, messageResId,
                                                                        Toast.LENGTH_SHORT).show()

fun Fragment.startActivity(activityClass: Class<out Activity>, bundle: Bundle = Bundle()) {
    startActivity(activityClass, bundle, this.activity)
}

private fun startActivity(activityClass: Class<out Activity>, bundle: Bundle, context: Context) {
    val intent = Intent(context, activityClass)
    intent.putExtras(bundle)
    context.startActivity(intent)
}

fun Fragment.sendHttpRequest(request: StringRequest) = sendHttpRequest(activity, request)
fun ArrayAdapter<out Any>.sendHttpRequest(request: StringRequest) = sendHttpRequest(context, request)
private fun sendHttpRequest(context: Context, request: StringRequest) {
    Volley.newRequestQueue(context).add(request)
}

fun Any.dao() = DaoManager.getDao(Topic::class.java)!!
fun Any.updateTopicsInDatabase(topics: List<Topic>) {
    AsyncTask.execute {
        dao().findAll().filter { it.type == topics.first().type }.forEach { dao().delete(it) }
        topics.forEach { dao().create(it) }
    }
}

fun Topic.toBundle(): Bundle {
    val bundle = Bundle()
    val serialized = listOf(id.toString(), posted, comments.toString(), upvotes.toString(), downvotes.toString(), link,
                            title, type.name, faved.toString()).joinToString(separator = "###")
    bundle.putString(BUNDLE_KEY, serialized)
    return bundle
}

fun Bundle.toTopic(): Topic {
    val parts = getString(BUNDLE_KEY).split("###")
    return Topic(parts[0].toInt(), parts[1], parts[2].toInt(), parts[3].toInt(), parts[4].toInt(), parts[5],
                 parts[6], Topic.Type.valueOf(parts[7]), parts[8].toBoolean())
}

fun ImageView.loadFrom(topic: Topic) {
    loadFrom(this, context, topic.imageDirectLinks())
}

fun loadFrom(image: ImageView, context: Context, links: List<String>) {
    Picasso.Builder(context).listener { picasso, _, _ ->
        if (links.size > 1) {
            loadFrom(image, context, links.drop(1))
        } else {
            picasso.cancelRequest(image)
        }
    }.build()
            .load(Uri.parse(links.first()))
            .fit().centerInside()
            .placeholder(R.drawable.image_loading)
            .error(R.drawable.image_loading_error)
            .into(image)
}

