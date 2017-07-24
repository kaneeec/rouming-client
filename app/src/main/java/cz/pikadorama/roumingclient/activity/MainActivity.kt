package cz.pikadorama.roumingclient.activity

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.android.volley.Response
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.data.TopicDao
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import cz.pikadorama.roumingclient.toast
import cz.pikadorama.simpleorm.DaoManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val dao = DaoManager.getDao(TopicDao::class.java)
    var activeTab = R.id.tabLatest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener({ bottomNavListener(it) })
        refreshLayout.setOnRefreshListener({ fetchTopicsFromWeb() })

        restoreCachedTopics()
    }

    private fun fetchTopicsFromWeb() {
        when (activeTab) {
            R.id.tabLatest -> RoumingHttpClient(this).fetchLatest(Response.Listener { processWebResponse(it) },
                                                                  Response.ErrorListener {
                                                                      toast(R.string.error_load_topics)
                                                                  })
            R.id.tabTop -> RoumingHttpClient(this).fetchTop(Response.Listener { processWebResponse(it) },
                                                            Response.ErrorListener {
                                                                toast(R.string.error_load_topics)
                                                            })
        }

    }

    private fun processWebResponse(response: String) {
        val topics = Topic.Factory.fromResponse(response)
        list.adapter = TopicListAdapter(this, topics)
        refreshLayout.isRefreshing = false

        saveFetchedDataToDatabase(topics)
    }

    private fun saveFetchedDataToDatabase(topics: List<Topic>) {
        AsyncTask.execute {
            dao.deleteAll()
            topics.forEach { dao.create(it.toTopicDao()) }
        }
    }

    private fun restoreCachedTopics() {
        list.adapter = TopicListAdapter(this, dao.findAll().map { it.toTopic() })
    }

    private fun fetchFavoriteTopicsFromDatabase() {
        list.adapter = TopicListAdapter(this, emptyList())
    }

    private fun bottomNavListener(item: MenuItem): Boolean {
        activeTab = item.itemId
        when (activeTab) {
            R.id.tabFavorites -> fetchFavoriteTopicsFromDatabase()
            else -> fetchTopicsFromWeb()
        }
        return true
    }

}




