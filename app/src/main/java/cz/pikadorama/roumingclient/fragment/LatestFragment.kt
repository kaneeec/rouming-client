package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import cz.pikadorama.roumingclient.*
import cz.pikadorama.roumingclient.activity.ImageFullscreenActivity
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.data.Topic
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_latest.*
import kotlinx.android.synthetic.main.fragment_latest.view.*

class LatestFragment : Fragment() {

    var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_latest, container, false)

        root.refreshLayout.setOnRefreshListener { fetchTopicsFromWeb() }
        root.refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)

        val lv = root.findViewById(android.R.id.list) as ListView
        lv.setOnItemClickListener { _, _, position, id ->
            startActivity(ImageFullscreenActivity::class.java,
                          (lv.adapter as TopicListAdapter).getItem(position).toBundle())
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        activity.toolbar.setTitle(R.string.title_latest)
        refreshLayout.isRefreshing = true
        showLatestTopics()
        listState?.let { list.onRestoreInstanceState(listState) }
    }

    override fun onPause() {
        super.onPause()
        listState = list.onSaveInstanceState()
    }

    private fun fetchTopicsFromWeb() {
        val url = "http://www.rouming.cz"
        val request = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            processWebResponse(it)
        }, Response.ErrorListener {
        })
        sendHttpRequest(request)
    }

    private fun showLatestTopics() {
        val cachedTopics = dao().findAll().filter { it.type == Topic.Type.LATEST }
        if (!cachedTopics.isEmpty()) {
            updateList(cachedTopics)
        } else {
            fetchTopicsFromWeb()
        }
    }

    private fun processWebResponse(response: String) {
        val topics = Topic.fromResponse(response)
        updateTopicsInDatabase(topics)
        updateList(topics)
    }

    private fun updateList(topics: List<Topic>) {
        list.adapter = TopicListAdapter(activity, topics)
        refreshLayout.isRefreshing = false
    }

}
