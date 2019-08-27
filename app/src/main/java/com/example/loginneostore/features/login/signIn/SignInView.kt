package com.example.loginneostore.features.login.signIn

import com.example.loginneostore.base.BaseView
import com.example.loginneostore.common.PopView

interface SignInView : BaseView, PopView {

    fun resetSignInFields()
    fun loadSignUp()
    fun onSignInClick()
    fun onSignInSuccess(result: String)
    fun onSignInFailed(error: String)


}