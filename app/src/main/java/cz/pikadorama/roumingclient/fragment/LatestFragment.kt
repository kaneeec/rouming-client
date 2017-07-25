package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import cz.pikadorama.roumingclient.toast
import cz.pikadorama.simpleorm.DaoManager
import kotlinx.android.synthetic.main.latest.*
import kotlinx.android.synthetic.main.latest.view.*

class LatestFragment : Fragment() {

    val dao = DaoManager.getDao(Topic::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.latest, container, false)
        root.refreshLayout.setOnRefreshListener({ fetchTopicsFromWeb() })
        root.refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
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
        val cachedTopics = dao.findAll().filter { it.type == Topic.Type.LATEST }
        if (!cachedTopics.isEmpty()) {
            updateTopics(cachedTopics)
        } else {
            fetchTopicsFromWeb()
        }
    }

    private fun processWebResponse(response: String) {
        val topics = Topic.fromResponse(response)
        updateTopics(topics)
        refreshLayout.isRefreshing = false
        saveFetchedDataToDatabase(topics)
    }

    private fun saveFetchedDataToDatabase(topics: List<Topic>) {
        AsyncTask.execute {
            dao.deleteAll()
            topics.forEach { dao.create(it) }
        }
    }

    private fun updateTopics(cachedTopics: List<Topic>) {
        list.adapter = TopicListAdapter(activity, cachedTopics)
    }
}
