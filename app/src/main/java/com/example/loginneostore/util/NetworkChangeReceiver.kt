package com.example.loginneostore.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkChangeReceiver private constructor(): BroadcastReceiver() {


    private lateinit var callback: NetworkChangeReceiverCallBack
    fun setCallBack(callback: NetworkChangeReceiverCallBack){
        this.callback=callback
    }


    companion object {

        private var instance: NetworkChangeReceiver? = null

        fun getNetworkChangeInstance(): NetworkChangeReceiver {

            if (instance == null) {
                instance =
                    NetworkChangeReceiver()

            }
            return instance as NetworkChangeReceiver
        }

    }


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {


        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        if (info != null && info.isConnected)
            callback.isConnected(true)
        else
            callback.isConnected(false)

    }


    interface NetworkChangeReceiverCallBack {
        fun isConnected(status: Boolean)
    }


}