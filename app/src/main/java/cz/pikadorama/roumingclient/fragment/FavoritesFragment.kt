package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.activity.ImageFullscreenActivity
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.dao
import cz.pikadorama.roumingclient.startActivity
import cz.pikadorama.roumingclient.toBundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.view.*

class FavoritesFragment : Fragment() {

    var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        root.refreshLayout.setOnRefreshListener({ updateList() })
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
        activity.toolbar.setTitle(R.string.title_favorites)
        refreshLayout.isRefreshing = true
        updateList()
        listState?.let { list.onRestoreInstanceState(listState) }
    }

    override fun onPause() {
        super.onPause()
        listState = list.onSaveInstanceState()
    }

    private fun updateList() {
        list.adapter = TopicListAdapter(activity, dao().findAll().filter { it.faved })
        refreshLayout.isRefreshing = false
    }

}
