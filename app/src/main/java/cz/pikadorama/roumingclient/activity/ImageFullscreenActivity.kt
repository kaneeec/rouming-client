package cz.pikadorama.roumingclient.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.loadFrom
import cz.pikadorama.roumingclient.toTopic
import kotlinx.android.synthetic.main.activity_image_fullscreen.*

class ImageFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_image_fullscreen)
        fullimage.loadFrom(intent.extras.toTopic())
    }

}
