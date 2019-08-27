package com.example.loginneostore.features.login.signIn

import com.example.loginneostore.base.BaseContract
import com.example.loginneostore.data.network.rest.request.SignInRequest

interface SignInContract : BaseContract {

    fun validateSignInFields(email : String?, password : String?): Boolean
    fun doSignIn(signInRequest : SignInRequest,isRemember : Boolean)
    fun onSignInSuccess(result : String)
    fun onSignInFailed(error : String)


}