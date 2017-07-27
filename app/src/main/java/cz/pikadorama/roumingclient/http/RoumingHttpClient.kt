package cz.pikadorama.roumingclient.http

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import cz.pikadorama.roumingclient.data.Topic
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL


class RoumingHttpClient(val context: Context) {

    fun fetchLatest(onSuccess: Response.Listener<String>, onError: Response.ErrorListener) {
        val url = "http://www.rouming.cz"
        val request = StringRequest(Request.Method.GET, url, onSuccess, onError)
        createRequestQueue().add(request)
    }

    fun fetchTop(limit: Int, interval: Int, onSuccess: Response.Listener<String>, onError: Response.ErrorListener) {
        val url = "http://www.rouming.cz/roumingListTop.php"
        val request = object : StringRequest(Request.Method.POST, url, onSuccess, onError) {
            override fun getParams(): Map<String, String> = mapOf(Pair("count", limit.toString()),
                                                                  Pair("operation", "1"),
                                                                  Pair("interval", interval.toString()))
        }
        createRequestQueue().add(request)
    }

    fun fetchDetails(topic: Topic, onSuccess: Response.Listener<String>, onError: Response.ErrorListener) {
        val request = StringRequest(Request.Method.GET, topic.link, onSuccess, onError)  // FIXME: encoding issue
        createRequestQueue().add(request)
    }

//    private fun createRequestQueue() = Volley.newRequestQueue(context, ProxyStack())
    private fun createRequestQueue() = Volley.newRequestQueue(context)

    inner class ProxyStack : HurlStack() {
        override fun createConnection(url: URL): HttpURLConnection {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("emea-proxy.uk.oracle.com", 80))
            return url.openConnection(proxy) as HttpURLConnection
        }
    }

}