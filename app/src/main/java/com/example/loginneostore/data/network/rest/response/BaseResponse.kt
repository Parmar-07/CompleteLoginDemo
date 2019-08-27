package com.example.loginneostore.data.network.rest.response

import com.google.gson.annotations.SerializedName


open class BaseResponse {
    @SerializedName("status")
    open var status: Int = 0

    @SerializedName("message")
    open var message: String = ""

    @SerializedName("user_msg")
    open var user_msg: String = ""
}