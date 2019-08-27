package com.example.loginneostore.data.network

import com.example.loginneostore.data.network.rest.RestService
import com.example.loginneostore.data.network.rest.response.BaseResponse
import com.example.loginneostore.data.network.rss.RSSResponse
import com.example.loginneostore.data.network.rss.XmlService
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface NetworkServices {
    @POST(RestService.LOGIN)
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Single<Response<BaseResponse>>

    @POST(RestService.REGISTER)
    @FormUrlEncoded
    fun register(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String,
        @Field("gender") gender: String = "M",
        @Field("phone_no") phone_no: String = "9833149457"
    ): Single<Response<BaseResponse>>

    @GET(XmlService.RSS)
    fun rssLatestNews(): Single<Response<RSSResponse>>
}