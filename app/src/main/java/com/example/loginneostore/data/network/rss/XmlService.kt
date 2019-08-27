package com.example.loginneostore.data.network.rss

import com.example.loginneostore.BuildConfig
import com.example.loginneostore.data.network.NetworkServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


const val RSS_URL = "https://www.moneycontrol.com/rss/"

interface XmlService : NetworkServices {


    @Suppress("DEPRECATION")
    companion object {
        const val RSS: String = "latestnews.xml"


        operator fun invoke(networkInterceptor: Interceptor): NetworkServices {


            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .addInterceptor(HttpLoggingInterceptor()
                    .apply {
                        level = if (BuildConfig.DEBUG)
                            HttpLoggingInterceptor.Level.BODY
                        else
                            HttpLoggingInterceptor.Level.NONE
                    })
                .build()


            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RSS_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(NetworkServices::class.java)
        }

    }


}