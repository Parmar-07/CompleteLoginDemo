package com.example.loginneostore.data.local

import android.annotation.SuppressLint
import com.example.loginneostore.NeoStore

class CacheStorage private constructor() {


    companion object {
        private var IS_USER_REMEMBER = "isUserRemember"
        private var USER_CREDENTIAL1 = "userCredential1"
        private var USER_CREDENTIAL2 = "userCredential2"
        private var RSS_LATEST_NEWS = "rssLatestNews"


        @SuppressLint("StaticFieldLeak")
        private var cache: CacheStorage? = null

        fun getCache(): CacheStorage {
            if (cache == null) {
                cache = CacheStorage()
            }
            return cache as CacheStorage
        }


    }


    var _remember: Boolean
        get() = NeoStore.get().getCache().reStoreBoolean(IS_USER_REMEMBER)
        set(isRemember) {
            NeoStore.get().getCache().storeBoolean(IS_USER_REMEMBER, isRemember)
        }

    var _email: String
        get() = NeoStore.get().getCache().reStoreString(USER_CREDENTIAL1)
        set(credential1) {
            NeoStore.get().getCache().storeString(USER_CREDENTIAL1, credential1)
        }

    var _passowrd: String
        get() = NeoStore.get().getCache().reStoreString(USER_CREDENTIAL2)
        set(credential2) {
            NeoStore.get().getCache().storeString(USER_CREDENTIAL2, credential2)
        }

    var _rssLatestNews: String
        get() = NeoStore.get().getCache().reStoreString(RSS_LATEST_NEWS)
        set(rssLatestNews) {
            NeoStore.get().getCache().storeString(RSS_LATEST_NEWS, rssLatestNews)
        }

    /*   fun clearCacheData(key: String) {
           NeoStore.get().getCache().clearStoreValue(key)
       }*/

    fun clearCache() {
        NeoStore.get().getCache().clearStore()
    }


}