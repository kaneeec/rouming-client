package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Response
import cz.pikadorama.roumingclient.*
import cz.pikadorama.roumingclient.activity.ImageFullscreenActivity
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.top.*
import kotlinx.android.synthetic.main.top.view.*


class TopFragment : Fragment() {

    lateinit var adapter: TopicListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.top, container, false)

        root.search.setOnClickListener {
            fetchTopicsFromWeb(root.limit.selectedItemPosition + 1, root.interval.selectedItemPosition + 1)
        }

        root.limit.adapter = ArrayAdapter.createFromResource(activity, R.array.limit,
                                                             android.R.layout.simple_spinner_dropdown_item)
        root.interval.adapter = ArrayAdapter.createFromResource(activity, R.array.interval,
                                                                android.R.layout.simple_spinner_dropdown_item)

        val lv = root.findViewById(android.R.id.list) as ListView
        lv.setOnItemClickListener { _, _, position, id ->
            startActivity(ImageFullscreenActivity::class.java, adapter.getItem(position).toBundle())
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        activity.toolbar.setTitle(R.string.title_top)
        showTopTopics()
    }

    private fun fetchTopicsFromWeb(limit: Int, interval: Int) {
        RoumingHttpClient(activity).fetchTop(limit, interval, Response.Listener { processWebResponse(it) },
                                             Response.ErrorListener { toast(R.string.error_load_topics) })
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
        this.adapter = TopicListAdapter(activity, topics)
        list.adapter = this.adapter
    }

}
