package com.example.loginneostore.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.loginneostore.R


const val SHORT = 0
/*
const val LONG = 1
*/
const val DEFAULT = 1
const val WARNING = 2
const val ERROR = 3
const val SUCCESS = 4

abstract class Pop(private val context: Context, private val popTime: Int, private val isAllowBackgroundTint: Boolean) :
    Toast(context) {

    abstract fun popNormal(message: String)
    abstract fun popWarning(message: String)
    abstract fun popError(message: String)
    abstract fun popSuccess(message: String)

    protected fun pop(POP_TYPE: Int, message: String) {

        setGravity(Gravity.CENTER, 0, 0)

        if (POP_TYPE == DEFAULT) {
            makeText(context, message, popTime).show()
            return
        }

        val icon: Int = when (POP_TYPE) {
            WARNING -> R.drawable.ic_pop_warning
            ERROR -> R.drawable.ic_pop_error
            SUCCESS -> R.drawable.ic_pop_success
            else -> 0
        }

        val tintColor: Int = if (!isAllowBackgroundTint) {
            0
        } else {
            when (POP_TYPE) {
                WARNING -> Color.parseColor("#FFE84C")
                ERROR -> Color.parseColor("#EE2121")
                SUCCESS -> Color.parseColor("#38EB20")
                else -> 0
            }
        }

        view = getPopView(context, message, icon, tintColor)
        show()

    }


    @Suppress("DEPRECATION")
    companion object {

        @SuppressLint("InflateParams")
        fun getPopView(context: Context, pop_message: String, popIcon: Int, bgTintColor: Int): View {
            val popView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.pop_messages, null)

            val popLogo = popView.findViewById<ImageView>(R.id.popLogo)
            val popText = popView.findViewById<TextView>(R.id.popText)

            val drawableFrame =
                if (bgTintColor != 0) {
                    val tintDrawable =
                        AppCompatResources.getDrawable(context, R.drawable.toast_frame) as NinePatchDrawable
                    tintDrawable.setColorFilter(bgTintColor, PorterDuff.Mode.SRC_IN)
                    tintDrawable
                } else {
                    AppCompatResources.getDrawable(context, R.drawable.toast_frame)
                }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                popView.background = drawableFrame
            else
                popView.setBackgroundDrawable(drawableFrame)

            popText.text = pop_message
            if (popIcon == 0)
                popLogo.visibility = View.INVISIBLE
            else {
                val drawableIcon = AppCompatResources.getDrawable(context, popIcon)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    popLogo.background = drawableIcon
                else
                    popLogo.setBackgroundDrawable(drawableIcon)
            }


            return popView

        }
    }


}