package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.content.Context
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

    val PREF_LIMIT = "limit"
    val PREF_INTERVAL = "interval"

    var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_top, container, false)

        root.refreshLayout.setOnRefreshListener {
            val prefs = activity.getPreferences(Context.MODE_PRIVATE)
            fetchTopicsFromWeb(prefs.getInt(PREF_LIMIT, 0), prefs.getInt(PREF_INTERVAL, 0))
        }
        root.refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)

        root.search.setOnClickListener {
            refreshLayout.isRefreshing = true
            fetchTopicsFromWeb(root.limit.selectedItemPosition, root.interval.selectedItemPosition)
        }

        initSpinners(root)

        val lv = root.findViewById(android.R.id.list) as ListView
        lv.setOnItemClickListener { _, _, position, id ->
            startActivity(ImageFullscreenActivity::class.java,
                          (lv.adapter as TopicListAdapter).getItem(position).toBundle())
        }

        return root
    }

    private fun initSpinners(root: View) {
        val prefs = activity.getPreferences(Context.MODE_PRIVATE)

        root.limit.adapter = ArrayAdapter.createFromResource(activity, R.array.limit, R.layout.spinner_item)
        (root.limit.adapter as ArrayAdapter<String>).setDropDownViewResource(R.layout.spinner_dropdown)
        root.limit.setSelection(prefs.getInt(PREF_LIMIT, 0))

        root.interval.adapter = ArrayAdapter.createFromResource(activity, R.array.interval, R.layout.spinner_item)
        (root.interval.adapter as ArrayAdapter<String>).setDropDownViewResource(R.layout.spinner_dropdown)
        root.interval.setSelection(prefs.getInt(PREF_INTERVAL, 0))
    }

    override fun onResume() {
        super.onResume()
        activity.toolbar.setTitle(R.string.title_top)
        refreshLayout.isRefreshing = true
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
            override fun getParams(): Map<String, String> = mapOf(Pair("count", (limit + 1).toString()),
                                                                  Pair("operation", "1"),
                                                                  Pair("interval", (interval + 1).toString()))
        }
        sendHttpRequest(request)
        saveLastSearchParams(limit, interval)
    }

    private fun saveLastSearchParams(limit: Int, interval: Int) {
        val prefs = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(PREF_LIMIT, limit)
        editor.putInt(PREF_INTERVAL, interval)
        editor.commit()
    }

    private fun showTopTopics() {
        val cachedTopics = dao().findAll().filter { it.type == Topic.Type.TOP }
        if (!cachedTopics.isEmpty()) {
            updateList(cachedTopics)
        } else {
            val prefs = activity.getPreferences(Context.MODE_PRIVATE)
            fetchTopicsFromWeb(prefs.getInt(PREF_LIMIT, 0), prefs.getInt(PREF_INTERVAL, 0))
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
