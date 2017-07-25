package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.android.volley.Response
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.activity.TopicDetailActivity
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.dao
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import cz.pikadorama.roumingclient.toast
import cz.pikadorama.roumingclient.updateTopicsInDatabase
import kotlinx.android.synthetic.main.latest.*
import kotlinx.android.synthetic.main.latest.view.*

class LatestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.latest, container, false)
        root.refreshLayout.setOnRefreshListener({ fetchTopicsFromWeb() })
        root.refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)

        val lv = root.findViewById(android.R.id.list) as ListView
        lv.setOnItemClickListener { _, _, position, id ->
            startActivity(Intent(activity, TopicDetailActivity::class.java))
        }
        
        return root
    }

    override fun onResume() {
        super.onResume()
        showLatestTopics()
    }

    private fun fetchTopicsFromWeb() {
        RoumingHttpClient(activity).fetchLatest(Response.Listener { processWebResponse(it) },
                                                Response.ErrorListener { toast(R.string.error_load_topics) })
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
        updateList(topics)
        refreshLayout.isRefreshing = false
        updateTopicsInDatabase(topics, Topic.Type.LATEST)
    }

    private fun updateList(topics: List<Topic>) {
        list.adapter = TopicListAdapter(activity, topics)
    }

}
