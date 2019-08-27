package com.example.loginneostore.util

import android.graphics.Rect
import android.text.method.TransformationMethod
import android.view.View
import android.widget.EditText

fun EditText.hidePasswordTransForm() {

    transformationMethod = object : TransformationMethod {
        override fun onFocusChanged(
            view: View?,
            sourceText: CharSequence?,
            focused: Boolean,
            direction: Int,
            previouslyFocusedRect: Rect?
        ) {
        }

        override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
            return PassCharSequence(source!!)
        }

        private inner class PassCharSequence(var charSequence: CharSequence) : CharSequence {

            override val length: Int
                get() = charSequence.length

            override fun get(index: Int): Char = '\u2022'

            override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                return PassCharSequence(charSequence.subSequence(startIndex, endIndex))
            }
        }

    }

}