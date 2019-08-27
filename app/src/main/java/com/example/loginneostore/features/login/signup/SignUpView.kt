package com.example.loginneostore.features.login.signup

import com.example.loginneostore.base.BaseView
import com.example.loginneostore.common.PopView

interface SignUpView : BaseView,PopView {

    fun resetSignUpFields()
    fun clearPasswords()
    fun loadLogin()
    fun onSignUpClick()
    fun onSignUpSuccess(result: String)
    fun onSignUpFailed(error: String)


}