package com.example.loginneostore.features.login

import com.example.loginneostore.base.BaseView

interface LoginView : BaseView {

    fun setScreenTitle(title : String)
    fun setScreenBack(visible : Boolean)
    fun openLoginScreen()
    fun openSignUpScreen()
    fun onLoadedLatestNews(news : String)

}