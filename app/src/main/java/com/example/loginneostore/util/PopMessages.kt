package com.example.loginneostore.util

import android.annotation.SuppressLint
import android.content.Context

class PopMessages private constructor(context: Context, popTime: Int, isAllowBackgroundTint: Boolean) :
    Pop(context, popTime, isAllowBackgroundTint) {
    override fun popNormal(message: String) {
        pop(DEFAULT, message)
    }

    override fun popWarning(message: String) {
        pop(WARNING, message)
    }

    override fun popError(message: String) {
        pop(ERROR, message)
    }

    override fun popSuccess(message: String) {
        pop(SUCCESS, message)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var pop: PopMessages? = null

        private fun getPopInstance(
            context: Context,
            popTime: Int = SHORT,
            isAllowBackgroundTint: Boolean = true
        ): PopMessages {
            if (pop == null) {
                pop = PopMessages(context, popTime, isAllowBackgroundTint)
            }
            return pop as PopMessages
        }


        fun normal(context: Context, message: String) {
            getPopInstance(context).popNormal(message)
        }

        fun warning(context: Context, message: String) {
            getPopInstance(context).popWarning(message)
        }

        fun error(context: Context, message: String) {
            getPopInstance(context).popError(message)
        }

        fun success(context: Context, message: String) {
            getPopInstance(context).popSuccess(message)
        }

    }


}
