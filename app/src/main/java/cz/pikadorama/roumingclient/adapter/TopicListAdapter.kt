package cz.pikadorama.roumingclient.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import cz.pikadorama.roumingclient.*
import cz.pikadorama.roumingclient.data.Topic
import kotlinx.android.synthetic.main.topic_list_item.view.*
import org.jsoup.Jsoup
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


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
        val request = UTF8StringRequest(Request.Method.GET, topic.link,
                                        Response.Listener<String> { parseAndShowResponse(it) },
                                        Response.ErrorListener { context.toast(R.string.error_load_topics) })
        sendHttpRequest(request)
    }

    private fun parseAndShowResponse(response: String) {
        val document = Jsoup.parse(response)
        val messages = document.getElementsByClass("roumingForumMessage").map { it.text().trim() }
        val people = document.getElementsByClass("roumingForumTitle").map {
            it.text().replaceBefore("(", "").replaceAfter(")", "").replace("(", "").replace(")", "").trim()
        }

        val content by lazy {
            val string = people.zip(messages.drop(1)).joinToString(
                    separator = "<br/><br/>") { "<strong>${it.first}</strong><br/>${it.second}" }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return@lazy Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return@lazy Html.fromHtml(string);
            }
        }
        MaterialDialog.Builder(context).title(R.string.comments).content(content).negativeText(R.string.close).show();
    }

    class UTF8StringRequest : StringRequest {
        constructor(method: Int, url: String, onSuccess: Response.Listener<String>,
                    onError: Response.ErrorListener) : super(method, url, onSuccess, onError)

        override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
            try {
                val utf8String = response.data.toString(Charset.forName("UTF-8"))
                return Response.success<String>(utf8String, HttpHeaderParser.parseCacheHeaders(response))

            } catch (e: UnsupportedEncodingException) {
                return Response.error(ParseError(e))
            }
        }
    }
}