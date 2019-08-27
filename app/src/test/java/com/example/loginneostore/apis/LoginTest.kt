package com.example.loginneostore.apis

import com.example.loginneostore.data.network.rest.request.SignInRequest
import com.example.loginneostore.data.network.rest.request.SignUpRequest
import org.junit.Test

class LoginTest : APIBuilder() {


    private val signUpRequest = SignUpRequest.apply {

        _firstName = "Unit"
        _lastName = "Test"
        _email = "unittest@demo.com"
        _password = "unit1234"
        _confirm_password = "unit1234"

    }


    private val signInRequest = SignInRequest.apply {

        _email = "unittest@demo.com"
        _password = "unit1234"

    }


    @Test
    override fun testSignUp() {
        super.testSignUp()
        singleAPITester {
            repo.signUp(signUpRequest)
                .matchStatusCode()
                .testAPI()
        }
    }

    @Test
    override fun testSignIn() {
        super.testSignIn()
        singleAPITester {
            repo.signIn(signInRequest)
                .matchStatusCode()
                .testAPI()
        }
    }

}