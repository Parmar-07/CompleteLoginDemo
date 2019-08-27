package com.example.loginneostore.features.login

import com.example.loginneostore.base.BasePresenter
import com.example.loginneostore.data.local.CacheStorage
import com.example.loginneostore.domain.repository.ILoginRepository

class LoginPresenter(private val view: LoginView, private val repo: ILoginRepository) : BasePresenter(),
    LoginContract {
    override fun loadLatestNews() {

        val latestNews = repo.loadLatestNews()
            .compose {
                loadSingleNetworkThread(it)
            }
            .subscribe({
                if (it.isSuccessful) {
                    view.onLoadedLatestNews(CacheStorage.getCache()._rssLatestNews)
                }
            }, {})


        addDisposable(latestNews)

    }


    override fun loadPresenter() {
        view.setScreenBack(false)
        view.openLoginScreen()
    }
}