package com.example.loginneostore.features.login.signup

import com.example.loginneostore.base.BaseContract
import com.example.loginneostore.data.network.rest.request.SignUpRequest

interface SignUpContract : BaseContract {


    fun validateSignUpFields(fullName: String?, email: String?, password: String?, cpassword: String?):Boolean
    fun doSignUp(signUpRequest: SignUpRequest)
    fun onSignUpSuccess(result: String)
    fun onSignUpFailed(error: String)
}