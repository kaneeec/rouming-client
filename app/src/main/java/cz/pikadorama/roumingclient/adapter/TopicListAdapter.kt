package cz.pikadorama.roumingclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.loadImage
import kotlinx.android.synthetic.main.item.view.*

class TopicListAdapter : ArrayAdapter<Topic> {

    constructor(context: Context) : super(context, 0, emptyList())
    constructor(context: Context, items: List<Topic>) : super(context, 0, items)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        val topic = getItem(position)

        view.thumbnail.loadImage(topic)
        view.title.text = topic.title
        view.upvotes.text = topic.upvotes.toString()
        view.downvotes.text = topic.downvotes.toString()
        view.comments.text = topic.comments.toString()

        return view
    }


}