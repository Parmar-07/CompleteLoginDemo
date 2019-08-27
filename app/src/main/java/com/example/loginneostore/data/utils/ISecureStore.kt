package com.example.loginneostore.data.utils

interface ISecureStore {

    fun loadStore()
    fun setStoreKeys(vararg keys: String)
    fun getSecureEncoded(input: String): String
    fun getSecureDecoded(input: String): String

}