package com.example.loginneostore.common

interface PopView {

    fun popError(message : String)
    fun popWarning(message : String)
    fun popSuccess(message : String)
    fun popLoading()
    fun popLoading(pTitle:String,pMessage:String)
    fun dismissPopLoading()

}