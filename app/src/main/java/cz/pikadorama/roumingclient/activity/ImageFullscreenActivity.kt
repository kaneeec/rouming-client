package cz.pikadorama.roumingclient.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.loadFrom
import cz.pikadorama.roumingclient.toTopic
import kotlinx.android.synthetic.main.activity_image_fullscreen.*

class ImageFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_fullscreen)
        fullimage.loadFrom(intent.extras.toTopic())
    }

}
