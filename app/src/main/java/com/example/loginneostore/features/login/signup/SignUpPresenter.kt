package com.example.loginneostore.features.login.signup

import com.example.loginneostore.base.BasePresenter
import com.example.loginneostore.base.DataMapper
import com.example.loginneostore.data.network.rest.request.SignUpRequest
import com.example.loginneostore.data.network.rest.response.BaseResponse
import com.example.loginneostore.domain.repository.ILoginRepository
import com.example.loginneostore.util.*
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit


class SignUpPresenter(private val view: SignUpView, private val repo: ILoginRepository) : BasePresenter(),
    SignUpContract {

    private val inputValid = InputValidations.getInputValidation()

    override fun doSignUp(signUpRequest: SignUpRequest) {


        addDisposable(

            repo.signUp(signUpRequest)
                .onApiExecute(
                    onStart = {
                        view.popLoading()
                    },
                    attachThread =
                    {
                        loadSingleNetworkThread(it)
                    },
                    onStop =
                    {
                        view.dismissPopLoading()
                    })

                .onApiDataMapper(
                    object : DataMapper<Response<BaseResponse>, BaseResponse> {
                        override fun mapper(from: Response<BaseResponse>): BaseResponse {
                            return if (from.isSuccessful) {
                                from.body() as BaseResponse
                            } else {
                                Gson().fromJson(from.errorBody()?.charStream(), BaseResponse::class.java)
                            }
                        }
                    }
                )
                .onApiResponseMapper(
                    isSuccess = { res -> (res.status == 200) },
                    mapSuccess = { res -> res.message },
                    mapFail = { res -> Throwable(res.message) }
                )
                .apiSuccess {
                    onSignUpSuccess(it)
                }
                .apiFail {
                    onSignUpFailed(it.message.toString())
                }
                .call()

        )
    }

    override fun onSignUpSuccess(result: String) {
        view.onSignUpSuccess(result)
    }

    override fun onSignUpFailed(error: String) {
        view.onSignUpFailed(error)
    }


    override fun validateSignUpFields(fullName: String?, email: String?, password: String?, cpassword: String?):Boolean {

        var isValid = false
        when {
            fullName.isNullOrEmpty() -> view.popError("Full Name is Blank")
            !inputValid.isValidFullName(fullName) -> view.popWarning("Kindly add surname after space")
            email.isNullOrEmpty() -> view.popError("Email is Blank")
            !inputValid.isValidEmail(email) -> view.popWarning("Email is not valid")
            password.isNullOrEmpty() -> view.popError("Password is Blank")
            !inputValid.isValidLength(
                password,
                5,
                min = false
            ) -> view.popWarning("Password must be at least 5 characters.")
            !inputValid.isValidPassword(password) -> view.popWarning("Special Characters not allowed")

            cpassword.isNullOrEmpty() -> view.popError("Confirm Password is Blank")
            !inputValid.isValidLength(
                cpassword,
                5,
                min = false
            ) -> view.popWarning("Confirm Password must be at least 5 characters.")
            !inputValid.isValidPassword(cpassword) -> view.popWarning("Special Characters not allowed")
            (cpassword != password) -> {
                view.clearPasswords()
                view.popWarning("Confirm Password not Matched!")
            }

            else -> {

                isValid = true

            }
        }

        return isValid

    }

    override fun loadPresenter() {
        view.resetSignUpFields()
    }

}