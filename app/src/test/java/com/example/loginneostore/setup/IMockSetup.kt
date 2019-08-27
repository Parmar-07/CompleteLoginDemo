package com.example.loginneostore.setup

import android.content.Context
import com.example.loginneostore.data.network.NetworkServices
import com.example.loginneostore.util.NetworkInterceptor

interface IMockSetup {
    fun mockContext(): Context
    fun mockConnectionManger(context: Context)
    fun mockNetworkInterceptor(context: Context): NetworkInterceptor
    fun mockNetworkRestService(interceptor: NetworkInterceptor): NetworkServices

}