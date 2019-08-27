package com.example.loginneostore.features.login.signIn

import com.example.loginneostore.base.BasePresenter
import com.example.loginneostore.base.DataMapper
import com.example.loginneostore.data.network.rest.request.SignInRequest
import com.example.loginneostore.data.network.rest.response.BaseResponse
import com.example.loginneostore.domain.repository.ILoginRepository
import com.example.loginneostore.util.*
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response
import java.sql.Time
import java.util.concurrent.TimeUnit

class SignInPresenter(private val view: SignInView, private val repo: ILoginRepository) : BasePresenter(),
    SignInContract {

    private val inputValid = InputValidations.getInputValidation()


    override fun onSignInFailed(error: String) {
        view.onSignInFailed(error)
    }


    override fun onSignInSuccess(result: String) {
        view.onSignInSuccess(result)
    }





    override fun doSignIn(signInRequest: SignInRequest, isRemember: Boolean) {

        addDisposable(

            repo.signIn(signInRequest, isRemember)
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
                .onApiResponseMapper(2,{ res -> (res.status == 200) },
                    { res ->
                        res.message
                    }, { res ->
                        Throwable(res.message)
                    }

                )

                .apiSuccess {
                    onSignInSuccess(it)
                }
                .apiFail {
                    onSignInFailed(it.message.toString())
                }
                .call()



        )
    }


    override fun validateSignInFields(email: String?, password: String?) : Boolean{

        var isValid = false
        when {
            email.isNullOrEmpty() -> view.popError("Email is Blank")
            !inputValid.isValidEmail(email) -> view.popWarning("Email is not valid")
            password.isNullOrEmpty() -> view.popError("Password is Blank")
            !inputValid.isValidLength(
                password,
                5,
                min = false
            ) -> view.popWarning("Password must be at least 5 characters.")
            !inputValid.isValidPassword(password) -> view.popWarning("Special Characters not allowed")
            else -> {isValid =true}

        }

        return isValid
    }

    override fun loadPresenter() {
        view.resetSignInFields()
    }

}