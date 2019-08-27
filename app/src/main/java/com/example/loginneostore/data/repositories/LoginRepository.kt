package com.example.loginneostore.data.repositories

import com.example.loginneostore.data.local.CacheStorage
import com.example.loginneostore.data.network.NetworkServices
import com.example.loginneostore.data.network.rest.request.SignInRequest
import com.example.loginneostore.data.network.rest.request.SignUpRequest
import com.example.loginneostore.data.network.rest.response.BaseResponse
import com.example.loginneostore.data.network.rss.RSSResponse
import com.example.loginneostore.domain.repository.ILoginRepository
import com.example.loginneostore.util.newsListToString
import io.reactivex.Single
import retrofit2.Response

class LoginRepository(private val networkService: NetworkServices) : ILoginRepository {


    override fun loadLatestNews(): Single<Response<RSSResponse>> {
        return networkService.rssLatestNews().doAfterSuccess { res ->
            res.let {

                if (it.isSuccessful) {

                    val response = it.body() as RSSResponse
                    val cache = CacheStorage.getCache()
                    cache._rssLatestNews = response.newsListToString()
                }
            }

        }
    }

    override fun signUp(request: SignUpRequest): Single<Response<BaseResponse>> {
        return networkService.register(
            request._firstName,
            request._lastName,
            request._email,
            request._password,
            request._confirm_password
        )


    }


    override fun signIn(request: SignInRequest, isRemember: Boolean): Single<Response<BaseResponse>> {


        return networkService.login(request._email, request._password).doAfterSuccess {
            val cache = CacheStorage.getCache()
            cache.clearCache()
            cache._remember = isRemember
            if (isRemember) {
                cache._email = request._email
                cache._passowrd = request._password
            }
        }
    }


}
