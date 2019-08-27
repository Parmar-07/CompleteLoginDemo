package com.example.loginneostore.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.loginneostore.data.utils.ISecureStore

class SecureCache private constructor() {

    companion object {
        private const val PREFRENCE_NAME = "SecureCache"
        private var instance: SecureCache? = null
        private lateinit var prefs: SharedPreferences
        private lateinit var store: ISecureStore

        fun getInstance(context: Context, store: ISecureStore): SecureCache {
            if (instance == null) {
                this.store = store
                this.store.loadStore()
                prefs = context.applicationContext.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE)
                instance = SecureCache()
            }
            return instance as SecureCache

        }

    }

    internal fun setStoreKeys(vararg keys: String){
        store.setStoreKeys(*keys)
    }



    fun storeString(prfKey: String, value: String) {
        val encryptedValue = store.getSecureEncoded(value)
        prefs.edit().putString(prfKey, encryptedValue).apply()
    }


    fun storeBoolean(prfKey: String, value: Boolean) {

        val encryptedValue = store.getSecureEncoded(value.toString())
        prefs.edit().putString(prfKey, encryptedValue).apply()
    }



    fun reStoreString(prfKey: String): String {

        var value = prefs.getString(prfKey, "")
        value = if (!value.isNullOrEmpty()) {
            store.getSecureDecoded(value)
        } else {
            ""
        }
        return value
    }

    fun reStoreBoolean(prfKey: String): Boolean {

        var value = prefs.getString(prfKey, "")
        value = if (!value.isNullOrEmpty()) {
            store.getSecureDecoded(value)
        } else {
            "false"
        }
        return value.toBoolean()
    }


    @SuppressLint("CommitPrefEdits")
    fun clearStoreValue(prfKey: String) {
        prefs.edit().remove(prfKey).apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun clearStore() {
        prefs.edit().clear().apply()
    }

}