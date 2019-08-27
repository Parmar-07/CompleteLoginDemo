package com.example.loginneostore.setup

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.loginneostore.data.network.NetworkServices
import com.example.loginneostore.data.network.rest.RestService
import com.example.loginneostore.util.NetworkInterceptor
import org.powermock.api.mockito.PowerMockito

class MockSetup : IMockSetup {


    override fun mockContext(): Context {
        println("Context....")
        val context = PowerMockito.mock(Context::class.java)
        println("Context....Mocked!!")
        return context
    }

   override fun mockConnectionManger(context: Context) {
        println("ConnectivityManager....")
        val connectivityManager = PowerMockito.mock(ConnectivityManager::class.java)
        PowerMockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        val networkInfo = PowerMockito.mock(NetworkInfo::class.java)
        PowerMockito.`when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        PowerMockito.`when`(networkInfo.isConnected).thenReturn(true)
        println("ConnectivityManager....Mocked!")
    }


    override fun mockNetworkInterceptor(context: Context): NetworkInterceptor {
        println("NetworkInterceptor....")
        val networkIntercept = NetworkInterceptor.getNetworkInterceptorInstance(context)
        println("NetworkInterceptor....Mocked!")
        return networkIntercept
    }

    override fun mockNetworkRestService(interceptor: NetworkInterceptor): NetworkServices {
        println("NetworkRestServices....")
        val restService = RestService.invoke(interceptor)
        println("NetworkRestServices....Mocked!")
        return restService
    }


}