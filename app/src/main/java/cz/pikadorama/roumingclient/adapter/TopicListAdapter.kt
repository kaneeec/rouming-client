package cz.pikadorama.roumingclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.android.volley.Response
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.dao
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import cz.pikadorama.roumingclient.loadFrom
import cz.pikadorama.roumingclient.toast
import kotlinx.android.synthetic.main.topic_list_item.view.*
import org.jsoup.Jsoup

class TopicListAdapter : ArrayAdapter<Topic> {

    constructor(context: Context) : super(context, 0, emptyList())
    constructor(context: Context, items: List<Topic>) : super(context, 0, items)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.topic_list_item, parent, false)
        val topic = getItem(position)

        with(view) {
            thumbnail.loadFrom(topic)
            title.text = topic.title
            upvotes.text = topic.upvotes.toString()
            downvotes.text = topic.downvotes.toString()
            comments.text = topic.comments.toString()

            showComments.setOnClickListener { showDialogWithComments(context, topic) }

            faveit.setImageResource(if (topic.faved) R.drawable.topic_faved_full else R.drawable.topic_faved_empty)
            faveit.tag = topic.faved
            faveit.setOnClickListener {
                val faved = !(faveit.tag as Boolean)

                faveit.tag = faved
                topic.faved = faved
                dao().update(topic)

                faveit.setImageResource(if (faved) R.drawable.topic_faved_full else R.drawable.topic_faved_empty)
            }
        }

        return view
    }

    private fun showDialogWithComments(context: Context, topic: Topic) {
        RoumingHttpClient(context).fetchDetails(topic, Response.Listener<String> { parseAndShowResponse(it) },
                                                Response.ErrorListener { context.toast(R.string.error_load_topics) })
    }

    private fun parseAndShowResponse(response: String) {
        val messages = Jsoup.parse(response).getElementsByClass("roumingForumMessage")
        MaterialDialog.Builder(context)
                .title(R.string.comments)
                .content(messages.drop(1).map { it.text().trim() }.joinToString(separator = "\n\n"))
                .negativeText(R.string.close)
                .show();
    }

}