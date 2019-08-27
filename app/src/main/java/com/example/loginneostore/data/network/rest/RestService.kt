package com.example.loginneostore.data.network.rest

import com.example.loginneostore.BuildConfig
import com.example.loginneostore.data.network.NetworkServices
import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val REST_URL = "http://staging.php-dev.in:8844/trainingapp/api/users/"
const val AUTH_KEY = "someAuthString"

interface RestService : NetworkServices {


    companion object {

        const val LOGIN: String = "login"
        const val REGISTER: String = "register"


        operator fun invoke(networkInterceptor: Interceptor): NetworkServices {

            val requestInterceptor = Interceptor { chain ->

                val url = chain.request().url()

                val request = chain.request().newBuilder()
                if (
                    url.toString().endsWith("api/$LOGIN", false) ||
                    url.toString().endsWith("api/$REGISTER", false)
                ) {
                    request.addHeader("Authorization", AUTH_KEY)
                    request.addHeader("Content_Type", "applicaton/form-data")
                }

                try {
                    chain.proceed(request.build())
                } catch (e: Exception) {
                    e.printStackTrace()
                    val eResBody = ResponseBody.create(
                        MediaType.parse("text/plain; charset=utf-8")
                        , e.message.toString()
                    )

                    Response.Builder()
                        .request(request.build())
                        .protocol(Protocol.HTTP_1_1)
                        .body(eResBody)
                        .message(e.message.toString())
                        .code(505)
                        .build()


                }

            }


            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .addInterceptor(requestInterceptor)
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
                .baseUrl(REST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(NetworkServices::class.java)
        }

    }


}