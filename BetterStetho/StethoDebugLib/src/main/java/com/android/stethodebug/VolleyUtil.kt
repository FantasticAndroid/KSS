package com.android.stethodebug

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.toolbox.BaseHttpStack
import com.android.volley.toolbox.HttpResponse
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class VolleyUtil {

    companion object {

        fun initStethoAccordingly(context: Context) {
            Stetho.initializeWithDefaults(context)
        }

        fun getHttpTask():BaseHttpStack{
            return VolleyOkHttp3StackInterceptors()
        }

        /*fun getVolley(context: Context):RequestQueue {
            return Volley.newRequestQueue(context, VolleyOkHttp3StackInterceptors())
        }

        fun getVolley(cacheDir:File, threadPoolSize:Int):RequestQueue {
            val network = BasicNetwork(VolleyOkHttp3StackInterceptors())
            return RequestQueue(DiskBasedCache(cacheDir), network, threadPoolSize)
        }*/
    }

    private class VolleyOkHttp3StackInterceptors : BaseHttpStack() {

        @Throws(IOException::class, AuthFailureError::class)
        override fun executeRequest(
            request: Request<*>,
            additionalHeaders: Map<String, String>?
        ): HttpResponse {
            val clientBuilder = OkHttpClient.Builder()
            val timeoutMs = request.timeoutMs

            clientBuilder.connectTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
            clientBuilder.readTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
            clientBuilder.writeTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)

            val okHttpRequestBuilder = okhttp3.Request.Builder()
            okHttpRequestBuilder.url(request.url)

            val headers = request.headers
            if (headers != null) {
                for (name in headers.keys) {
                    val value = headers[name]
                    if (value != null)
                        okHttpRequestBuilder.addHeader(name, value)
                }
            }
            if (additionalHeaders != null) {
                for (name in additionalHeaders.keys) {
                    val value = additionalHeaders[name]
                    if (value != null)
                        okHttpRequestBuilder.addHeader(name, value)
                }
            }

            setConnectionParametersForRequest(okHttpRequestBuilder, request)

            val stethoInterceptor = StethoInterceptor()
            clientBuilder.addNetworkInterceptor(stethoInterceptor)
            Log.d("VolleyUtil","StethoInterceptor Add")

            val client = clientBuilder.build()
            val okHttpRequest = okHttpRequestBuilder.build()
            val okHttpCall = client.newCall(okHttpRequest)
            val okHttpResponse = okHttpCall.execute()

            val code = okHttpResponse.code()
            val body = okHttpResponse.body()
            val content = body?.byteStream()
            val contentLength = body?.contentLength()?.toInt() ?: 0
            val responseHeaders = mapHeaders(okHttpResponse.headers())
            return HttpResponse(code, responseHeaders, contentLength, content)
        }

        @Throws(AuthFailureError::class)
        private fun setConnectionParametersForRequest(
            builder: okhttp3.Request.Builder,
            request: Request<*>
        ) {
            when (request.method) {
                Request.Method.DEPRECATED_GET_OR_POST -> {
                    // Ensure backwards compatibility.  Volley assumes a request with a null body is a GET.
                    val postBody = request.body
                    if (postBody != null) {
                        builder.post(RequestBody.create(
                                MediaType.parse(request.bodyContentType), postBody))
                    }
                }
                Request.Method.GET -> builder.get()
                Request.Method.DELETE -> builder.delete(createRequestBody(request))
                Request.Method.POST -> builder.post(createRequestBody(request)!!)
                Request.Method.PUT -> builder.put(createRequestBody(request)!!)
                Request.Method.HEAD -> builder.head()
                Request.Method.OPTIONS -> builder.method("OPTIONS", null)
                Request.Method.TRACE -> builder.method("TRACE", null)
                Request.Method.PATCH -> builder.patch(createRequestBody(request)!!)
                else -> throw IllegalStateException("Unknown method type.")
            }
        }

        @Throws(AuthFailureError::class)
        private fun createRequestBody(r: Request<*>): RequestBody? {
            val body = r.body ?: return null
            return RequestBody.create(MediaType.parse(r.bodyContentType), body)
        }

        private fun mapHeaders(responseHeaders: Headers): List<Header> {
            val headers = ArrayList<Header>()
            var i = 0
            val len = responseHeaders.size()
            while (i < len) {
                val name = responseHeaders.name(i)
                val value = responseHeaders.value(i)
                headers.add(Header(name, value))
                i++
            }
            return headers
        }
    }
}