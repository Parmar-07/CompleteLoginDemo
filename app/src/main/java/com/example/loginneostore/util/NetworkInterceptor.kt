package com.example.loginneostore.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.example.loginneostore.exceptions.NetworkException
import okhttp3.Interceptor
import okhttp3.Response

@Suppress("DEPRECATION")
class NetworkInterceptor private constructor(private val context: Context?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        context?.let {
            val connectivityManager = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo
            if (info == null || !info.isConnected)
                throw NetworkException()
        }



        return chain.proceed(chain.request())
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: NetworkInterceptor? = null

        fun getNetworkInterceptorInstance(context: Context?): NetworkInterceptor {

            if (instance == null) {
                instance =
                    NetworkInterceptor(context)

            }
            return instance as NetworkInterceptor
        }

    }
}