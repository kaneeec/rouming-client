package cz.pikadorama.roumingclient.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cz.pikadorama.roumingclient.*
import cz.pikadorama.roumingclient.data.Topic
import kotlinx.android.synthetic.main.image_with_info.*


class TopicDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_detail)

        val topic: Topic = intent.extras.toTopic()
        upvotes.text = topic.upvotes.toString()
        downvotes.text = topic.downvotes.toString()
        comments.text = topic.comments.toString()

        thumbnail.loadImage(topic)
        thumbnail.setOnClickListener { startActivity(ImageFullscreenActivity::class.java, topic.toBundle()) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = topic.title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
