package cz.pikadorama.roumingclient.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.android.volley.Response
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val bottomNavListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation_home -> message.setText(R.string.title_home)
                R.id.navigation_dashboard -> message.setText(R.string.title_dashboard)
                R.id.navigation_notifications -> message.setText(R.string.title_notifications)
                else -> return false
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(bottomNavListener)

        loadLatest()
    }

    private fun loadLatest() {
        RoumingHttpClient(this).fetchLatest(Response.Listener { println(it) }, Response.ErrorListener { println(it) });
    }

}
