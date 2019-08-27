package com.example.loginneostore.data.network.rest.request

import com.google.gson.annotations.SerializedName

object SignUpRequest {

    @SerializedName("first_name")
    var _firstName: String = ""
    @SerializedName("last_name")
    var _lastName: String = ""
    @SerializedName("email")
    var _email: String = ""
    @SerializedName("password")
    var _password: String = ""
    @SerializedName("confirm_password")
    var _confirm_password: String = ""


}