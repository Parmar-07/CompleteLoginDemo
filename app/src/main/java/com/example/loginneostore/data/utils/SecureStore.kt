package com.example.loginneostore.data.utils

import android.annotation.SuppressLint
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class SecureStore : ISecureStore {


    @SuppressLint("GetInstance")
    override fun loadStore() {
        cipher = Cipher.getInstance(ALOGORITHM_TRANSFORMATION)
    }

    override fun setStoreKeys(vararg keys: String) {
        if (keys.isNotEmpty()) {
            val sKey = keys[0].toByteArray()
            keySpec = SecretKeySpec(sKey, ALOGORITHM)

        }
    }

    override fun getSecureEncoded(input: String): String {
        return try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val plainTextByte = input.toByteArray()
            val encryptedByte = cipher.doFinal(plainTextByte)
            Base64.encodeToString(encryptedByte, 0)

        } catch (e: Exception) {
            e.printStackTrace()
            input
        }
    }

    override fun getSecureDecoded(input: String): String {
        return try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            var decodeTextByte = input.toByteArray()
            decodeTextByte = Base64.decode(decodeTextByte, 0)
            val decryptedByte = cipher.doFinal(decodeTextByte)
            String(decryptedByte)

        } catch (e: Exception) {
            e.printStackTrace()
            input
        }
    }


    private lateinit var keySpec: SecretKeySpec
    private lateinit var cipher: Cipher

    companion object {
        private const val ALOGORITHM_TRANSFORMATION = "AES"
        private const val ALOGORITHM = "AES"

        private var instance: SecureStore? = null

        fun getStore(): SecureStore {
            if (instance == null) {
                instance = SecureStore()
            }
            return instance as SecureStore
        }

    }

}