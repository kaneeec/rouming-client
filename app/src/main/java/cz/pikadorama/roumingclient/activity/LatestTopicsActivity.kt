package cz.pikadorama.roumingclient.activity

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.volley.Response
import cz.pikadorama.roumingclient.R
import cz.pikadorama.roumingclient.adapter.TopicListAdapter
import cz.pikadorama.roumingclient.data.Topic
import cz.pikadorama.roumingclient.http.RoumingHttpClient
import cz.pikadorama.roumingclient.toast
import cz.pikadorama.simpleorm.DaoManager
import kotlinx.android.synthetic.main.latest.*


class LatestTopicsActivity : AppCompatActivity() {

    val dao = DaoManager.getDao(Topic::class.java)
    var activeTab = R.id.latest



}




