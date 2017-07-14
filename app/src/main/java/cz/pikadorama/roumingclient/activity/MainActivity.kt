package cz.pikadorama.roumingclient.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.android.volley.Response
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener({ bottomNavListener(it) })

        loadLatest()
    }

    private fun loadLatest() {
        RoumingHttpClient(this).fetchLatest(Response.Listener { parseResponse(it) },
                                            Response.ErrorListener { println(it) });
//        RoumingHttpClient(this).fetchTop(Response.Listener { parseResponse(it) },
//                                            Response.ErrorListener { println(it) });
    }

    private fun bottomNavListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> message.setText(R.string.title_home)
            R.id.navigation_dashboard -> message.setText(R.string.title_dashboard)
            R.id.navigation_notifications -> message.setText(R.string.title_notifications)
            else -> return false
        }
        return true
    }

    private fun parseResponse(response: String) {
        val topics = Topic.Factory.fromResponse(response)
        println(topics)
    }

}
