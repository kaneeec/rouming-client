package cz.pikadorama.roumingclient.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.loadImage
import cz.pikadorama.roumingclient.toBundle
import cz.pikadorama.roumingclient.toTopic
import kotlinx.android.synthetic.main.activity_topic_detail.*


class TopicDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        val topic: Topic = intent.extras.toTopic()
        thumbnail.loadImage(topic)
        upvotes.text = topic.upvotes.toString()
        downvotes.text = topic.downvotes.toString()
        comments.text = topic.comments.toString()

        thumbnail.setOnClickListener {
            val intent = Intent(this, ImageFullscreenActivity::class.java)
            intent.putExtras(topic.toBundle())
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
