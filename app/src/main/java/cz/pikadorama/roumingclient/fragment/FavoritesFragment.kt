package cz.pikadorama.roumingclient.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.dao
import cz.pikadorama.roumingclient.data.Topic
import kotlinx.android.synthetic.main.favorites.*

class FavoritesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.favorites, container, false)
    }

    override fun onResume() {
        super.onResume()
        showFavoriteTopics()
    }

    private fun showFavoriteTopics() {
        list.adapter = TopicListAdapter(activity, dao().findAll().filter { it.type == Topic.Type.FAVORITES })
    }

}
