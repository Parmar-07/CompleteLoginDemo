package com.example.loginneostore.domain.repository

import com.example.loginneostore.data.network.rest.request.SignInRequest
import com.example.loginneostore.data.network.rest.request.SignUpRequest
import com.example.loginneostore.data.network.rest.response.BaseResponse
import com.example.loginneostore.data.network.rss.RSSResponse
import io.reactivex.Single
import retrofit2.Response

interface ILoginRepository {
    fun signIn(request: SignInRequest,isRemember : Boolean = false): Single<Response<BaseResponse>>
    fun signUp(request: SignUpRequest): Single<Response<BaseResponse>>
    fun loadLatestNews(): Single<Response<RSSResponse>>

}