package com.example.loginneostore.util

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.AppCompatEditText
import com.example.loginneostore.R
import java.util.regex.Pattern


class EditText : AppCompatEditText {

    private var disableCopy: Boolean = false
    private var prefix: String = ""
    private var mOriginalLeftPadding = -1f
    override fun isSuggestionsEnabled(): Boolean {

        if (disableCopy) {
            return false
        }
        return super.isSuggestionsEnabled()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setAttributes(attrs)
    }

    override fun getSelectionStart(): Int {

        if (disableCopy) {
            for (element in Thread.currentThread().stackTrace) {
                if (
                    element.methodName == "canPaste"
                    || element.methodName == "canCopy"
                    || element.methodName == "canCut"
                    || element.methodName == "canSearch"
                    || element.methodName == "canSelectText"
                    || element.methodName == "canSelectAllText"
                    || element.methodName == "textCanBeSelected"
                    || element.methodName == "canShare"

                ) {
                    return -1
                }
            }
        }

        return super.getSelectionStart()
    }


    private fun setAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditText)
        val type_regex: String

        try {
            prefix = typedArray.getString(R.styleable.EditText_prefix) ?: ""
            type_regex = typedArray.getString(R.styleable.EditText_type_regex) ?: ""
            disableCopy = typedArray.getBoolean(R.styleable.EditText_disableCopy, false)
        } finally {
            typedArray.recycle()
        }


        if (disableCopy) {
            setTextIsSelectable(false)
            this.customSelectionActionModeCallback = ActionModeCallbackInterceptor()
            this.isLongClickable = false
        }


        if (prefix.isNotEmpty()) {

            if (mOriginalLeftPadding == -1f) {
                val widths = FloatArray(prefix.length)
                paint.getTextWidths(prefix, widths)
                var textWidth = 0f
                for (w in widths) {
                    textWidth += w
                }
                mOriginalLeftPadding = compoundPaddingLeft.toFloat()
                setPadding(
                    (textWidth + mOriginalLeftPadding).toInt(),
                    paddingRight, paddingTop,
                    paddingBottom
                )
            }

        }




        if (type_regex.isNotEmpty()) {

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                    val text = s.toString()
                    val length = text.length
                    if (length > 0 && !Pattern.matches(type_regex, text)) {
                        s?.delete(length - 1, length)
                    }

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            })


        }


    }


    private inner class ActionModeCallbackInterceptor : ActionMode.Callback, android.view.ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            menu.clear()
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {}

        override fun onCreateActionMode(mode: android.view.ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: android.view.ActionMode, menu: Menu): Boolean {
            menu.clear()
            return false
        }

        override fun onActionItemClicked(mode: android.view.ActionMode, item: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: android.view.ActionMode) {

        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (prefix.isNotEmpty()) {
            canvas.drawText(
                prefix, mOriginalLeftPadding,
                getLineBounds(0, null).toFloat(), paint
            )
        }


    }


}