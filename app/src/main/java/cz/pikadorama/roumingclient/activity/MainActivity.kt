package cz.pikadorama.roumingclient.activity

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.fragment.FavoritesFragment
import cz.pikadorama.roumingclient.fragment.LatestFragment
import cz.pikadorama.roumingclient.fragment.TopFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActionBar()
        initNavigation()
        showFragment(LatestFragment())
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initNavigation() {
        navigation.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.latest -> showFragment(LatestFragment())
            R.id.top -> showFragment(TopFragment())
            R.id.favorites -> showFragment(FavoritesFragment())
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    }
}
