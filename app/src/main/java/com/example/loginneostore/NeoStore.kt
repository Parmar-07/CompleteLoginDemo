package com.example.loginneostore

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.example.loginneostore.base.BaseActivity
import com.example.loginneostore.data.local.SecureCache
import com.example.loginneostore.data.utils.SecureStore
import com.example.loginneostore.util.NetworkChangeReceiver

const val PREF_KEY = "YERVqHS8QtxMvwbe4j3Lmaj8HHvNHGtH"

class NeoStore : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    private var baseActivity: BaseActivity<*>? = null

    override fun onActivityPaused(p0: Activity) {
        baseActivity = null
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {


    }

    override fun onActivityResumed(p0: Activity) {
        try {
            baseActivity = p0 as BaseActivity<*>
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val networkChangeReceiver =
        NetworkChangeReceiver.getNetworkChangeInstance()

    override fun onCreate() {
        super.onCreate()

        appInstance = this

        this.registerActivityLifecycleCallbacks(this)
        networkChangeReceiver.setCallBack(object :
            NetworkChangeReceiver.NetworkChangeReceiverCallBack {
            override fun isConnected(status: Boolean) {
                if (!status)
                    baseActivity?.popNoInternetError()
                else
                    baseActivity?.dismissPopErrorLoading()
            }

        })
        registerNetworkBroadcastForNougat(networkChangeReceiver)

        secureCache = SecureCache.getInstance(this, SecureStore.getStore())
        secureCache.setStoreKeys(PREF_KEY)
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterNetworkChanges(networkChangeReceiver)
    }

    @Suppress("DEPRECATION")
    private fun registerNetworkBroadcastForNougat(receiver: BroadcastReceiver) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    private fun unregisterNetworkChanges(receiver: BroadcastReceiver) {
        try {
            unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private lateinit var appInstance: NeoStore
        private lateinit var secureCache: SecureCache

        fun get(): NeoStore {
            return appInstance
        }


    }

    fun getCache(): SecureCache {
        return secureCache
    }

}