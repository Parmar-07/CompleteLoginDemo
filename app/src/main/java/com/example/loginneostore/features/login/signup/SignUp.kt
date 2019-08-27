package com.example.loginneostore.features.login.signup

import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.loginneostore.R
import com.example.loginneostore.base.BaseFragment
import com.example.loginneostore.data.network.rest.RestService
import com.example.loginneostore.data.network.rest.request.SignUpRequest
import com.example.loginneostore.data.repositories.LoginRepository
import com.example.loginneostore.features.login.LoginActivity
import com.example.loginneostore.util.NetworkInterceptor
import com.example.loginneostore.util.hidePasswordTransForm
import kotlinx.android.synthetic.main.layout_signup.*

class SignUp : BaseFragment<SignUpPresenter>(), SignUpView {


    override fun onSignUpSuccess(result: String) {
        popSuccess(result)
    }

    override fun onSignUpFailed(error: String) {
        popError(error)
    }

    override fun clearPasswords() {
        password.setText("")
        cpassword.setText("")
        password.clearFocus()
        cpassword.clearFocus()
        password.requestFocus()
        agreeChecked.isChecked = false
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

    override fun resetSignUpFields() {

        fName.setText("")
        email.setText("")
        password.setText("")
        cpassword.setText("")
        agreeChecked.isChecked = false

    }

    override fun loadLogin() {
        parentActivity.openLoginScreen()
    }

    override fun onSignUpClick() {
        val isValid: Boolean = presenterProvider?.validateSignUpFields(
            fName.text.toString(),
            email.text.toString(),
            password.text.toString(),
            cpassword.text.toString()
        ) ?: false

        if (isValid) {


            if (!agreeChecked.isChecked) {
                popWarning("Kindly agree, ${parentActivity.resStr(R.string.terms_condition)}")
            } else {

                val request = SignUpRequest
                    .apply {
                        _email =  email.text.toString()
                        _firstName =  fName.text.toString().split(" ")[0]
                        _lastName =  fName.text.toString().split(" ")[1]
                        _password = password.text.toString()
                        _confirm_password = cpassword.text.toString()

                    }

                presenterProvider?.doSignUp(request)
            }

        }


    }


    override fun onScreenViewListeners() {
        login.setOnClickListener {
            loadLogin()
        }
        signin.setOnClickListener {
            onSignUpClick()
        }

        cpassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSignUpClick()
            }
            false
        }

    }

    override var presenterProvider: SignUpPresenter? = SignUpPresenter(
        this,
        LoginRepository(RestService.invoke(NetworkInterceptor.getNetworkInterceptorInstance(screenContext)))
    )

    override fun loadViewFrom() = R.layout.layout_signup

    override fun onScreenLoad() {
        let {
            parentActivity = it.activity as LoginActivity
        }

    }

    override fun onScreenLoaded(view: View) {
        password.hidePasswordTransForm()
        cpassword.hidePasswordTransForm()
    }

    override fun onScreenCleared() {}
}