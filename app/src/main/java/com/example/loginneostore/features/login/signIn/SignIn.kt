package com.example.loginneostore.features.login.signIn

import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.loginneostore.R
import com.example.loginneostore.base.BaseFragment
import com.example.loginneostore.data.local.CacheStorage
import com.example.loginneostore.data.network.rest.RestService
import com.example.loginneostore.data.network.rest.request.SignInRequest
import com.example.loginneostore.data.repositories.LoginRepository
import com.example.loginneostore.features.login.LoginActivity
import com.example.loginneostore.util.NetworkInterceptor
import com.example.loginneostore.util.hidePasswordTransForm
import kotlinx.android.synthetic.main.layout_login.*

class SignIn : BaseFragment<SignInPresenter>(), SignInView {


    override fun onSignInSuccess(result: String) {
        popSuccess(result)
        rememberCheck.isChecked = false
        email.setText("")
        password.setText("")
    }

    override fun onSignInFailed(error: String) {
        popError(error)
    }

    private lateinit var parentActivity: LoginActivity


    override fun popError(message: String) {

        parentActivity.runWithUI {

            if (message == "NetworkException")
                parentActivity.popNoInternetError()
            else
                parentActivity.popError(message)
        }


    }


    override fun popWarning(message: String) {
        parentActivity.runWithUI {
            parentActivity.popWarning(message)
        }
    }

    override fun popSuccess(message: String) {
        parentActivity.runWithUI {
            parentActivity.popSuccess(message)
        }
    }

    override fun popLoading(pTitle: String, pMessage: String) {
        parentActivity.runWithUI {
            parentActivity.popStopLoading(pTitle, pMessage)
        }
    }

    override fun popLoading() {
        parentActivity.runWithUI {
            parentActivity.popStartLoading()
        }
    }

    override fun dismissPopLoading() {
        parentActivity.runWithUI {
            parentActivity.dismissPopErrorLoading()
        }
    }

    override fun onScreenViewListeners() {

        signup.setOnClickListener {
            loadSignUp()
        }

        signin.setOnClickListener {
            onSignInClick()
        }


        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSignInClick()
            }
            false
        }
    }

    override fun resetSignInFields() {


        val cache = CacheStorage.getCache()
        rememberCheck.isChecked = cache._remember
        if (cache._remember) {
            email.setText(cache._email)
            password.setText(cache._passowrd)
        } else {
            email.setText("")
            password.setText("")
        }


    }

    override fun loadSignUp() {

        parentActivity.openSignUpScreen()


    }

    override fun onSignInClick() {
        val isValid: Boolean =
            presenterProvider?.validateSignInFields(
                email.text.toString(),
                password.text.toString()
            ) ?: false

        if (isValid) {
            presenterProvider?.doSignIn(SignInRequest.apply {
                _email = email.text.toString()
                _password = password.text.toString()
            }, rememberCheck.isChecked)
        }

    }


    override
    var presenterProvider: SignInPresenter? = SignInPresenter(
        this,
        LoginRepository(RestService.invoke(NetworkInterceptor.getNetworkInterceptorInstance(screenContext)))
    )


    override fun loadViewFrom() = R.layout.layout_login

    override fun onScreenLoad() {
        let {
            parentActivity = it.activity as LoginActivity

        }

    }

    override fun onScreenLoaded(view: View) {
        password.hidePasswordTransForm()
    }

    override fun onScreenCleared() {

    }

}