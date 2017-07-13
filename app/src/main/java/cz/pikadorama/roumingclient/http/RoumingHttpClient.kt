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

    enum class RoumingTimeRange(postParamValue: Int) {
        DAY(1),
        WEEK(2),
        MONTH(3)
    }

    fun fetchLatest(onSuccess: Response.Listener<String>, onError: Response.ErrorListener, limit: Int = 20) {
        val url = "http://www.rouming.cz"
        val request = StringRequest(Request.Method.GET, url, onSuccess, onError)
        Volley.newRequestQueue(context, ProxyStack()).add(request)
    }

    fun fetchTop(onSuccess: Response.Listener<String>, onError: Response.ErrorListener, limit: Int = 20, timeRange: RoumingTimeRange = RoumingTimeRange.DAY) {
        val url = "http://www.rouming.cz/top?"
        val request = StringRequest(Request.Method.GET, url, onSuccess, onError)
        Volley.newRequestQueue(context, ProxyStack()).add(request)
    }

    inner class ProxyStack : HurlStack() {
        override fun createConnection(url: URL): HttpURLConnection {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("emea-proxy.uk.oracle.com", 80))
            return url.openConnection(proxy) as HttpURLConnection
        }
    }

}