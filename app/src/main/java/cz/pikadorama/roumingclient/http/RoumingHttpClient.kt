package cz.pikadorama.roumingclient.http

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL


class RoumingHttpClient(val context: Context) {

    enum class RoumingTimeRange(val postParamValue: Int) {
        DAY(1),
        WEEK(2),
        MONTH(3),
        THREE_MONHTS(4),
        SIX_MONTHS(5),
        YEAR(6),
        INFINITY(7)
    }

    fun fetchLatest(onSuccess: Response.Listener<String>, onError: Response.ErrorListener) {
        val url = "http://www.rouming.cz"
        val request = StringRequest(Request.Method.GET, url, onSuccess, onError)
        createRequestQueue().add(request)
    }

    fun fetchTop(onSuccess: Response.Listener<String>, onError: Response.ErrorListener,
                 timeRange: RoumingTimeRange = RoumingTimeRange.DAY) {
        val url = "http://www.rouming.cz/roumingListTop.php"
        val request = object : StringRequest(Request.Method.POST, url, onSuccess, onError) {
            override fun getParams(): Map<String, String> = mapOf(Pair("count", "1"), Pair("operation", "1"),
                                                                  Pair("interval", timeRange.postParamValue.toString()))
        }
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