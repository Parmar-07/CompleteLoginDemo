package com.example.loginneostore.setup

import android.content.Context
import com.example.loginneostore.data.network.NetworkServices
import com.example.loginneostore.util.NetworkInterceptor

interface TestAPIs {
    fun testSignUp()
    fun testSignIn()

}