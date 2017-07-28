package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import cz.pikadorama.roumingclient.*
import cz.pikadorama.roumingclient.activity.ImageFullscreenActivity
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.data.Topic
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_top.*
import kotlinx.android.synthetic.main.fragment_top.view.*


class TopFragment : Fragment() {

    var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_top, container, false)

        root.search.setOnClickListener {
            fetchTopicsFromWeb(root.limit.selectedItemPosition + 1, root.interval.selectedItemPosition + 1)
        }

        root.limit.adapter = ArrayAdapter.createFromResource(activity, R.array.limit, R.layout.spinner_item)
        (root.limit.adapter as ArrayAdapter<String>).setDropDownViewResource(R.layout.spinner_dropdown)
        root.interval.adapter = ArrayAdapter.createFromResource(activity, R.array.interval, R.layout.spinner_item)
        (root.interval.adapter as ArrayAdapter<String>).setDropDownViewResource(R.layout.spinner_dropdown)

        val lv = root.findViewById(android.R.id.list) as ListView
        lv.setOnItemClickListener { _, _, position, id ->
            startActivity(ImageFullscreenActivity::class.java,
                          (lv.adapter as TopicListAdapter).getItem(position).toBundle())
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        activity.toolbar.setTitle(R.string.title_top)
        showTopTopics()
        listState?.let { list.onRestoreInstanceState(listState) }
    }

    override fun onPause() {
        super.onPause()
        listState = list.onSaveInstanceState()
    }

    private fun fetchTopicsFromWeb(limit: Int, interval: Int) {
        val url = "http://www.rouming.cz/roumingListTop.php"
        val request = object : StringRequest(Request.Method.POST, url,
                                             Response.Listener<String> { processWebResponse(it) },
                                             Response.ErrorListener { toast(R.string.error_load_topics) }) {
            override fun getParams(): Map<String, String> = mapOf(Pair("count", limit.toString()),
                                                                  Pair("operation", "1"),
                                                                  Pair("interval", interval.toString()))
        }
        sendHttpRequest(request)
    }

    private fun showTopTopics() {
        val cachedTopics = dao().findAll().filter { it.type == Topic.Type.TOP }
        if (!cachedTopics.isEmpty()) {
            updateList(cachedTopics)
        }
    }

    private fun processWebResponse(response: String) {
        val topics = Topic.fromResponse(response)
        updateList(topics)
        updateTopicsInDatabase(topics)
        // FIXME: save last query configuration to set spinners on load
        // FIXME: spinners black color -> white
    }

    private fun updateList(topics: List<Topic>) {
        list.adapter = TopicListAdapter(activity, topics)
    }

}
