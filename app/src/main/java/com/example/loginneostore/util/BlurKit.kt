package com.example.loginneostore.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View

class BlurKit {

    private var rs: RenderScript? = null

    fun blur(src: Bitmap, radius: Int): Bitmap {
        val input = Allocation.createFromBitmap(rs, src)
        val output = Allocation.createTyped(rs, input.type)
        val script: ScriptIntrinsicBlur
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setRadius(radius.toFloat())
            script.setInput(input)
            script.forEach(output)
        }
        output.copyTo(src)
        return src
    }

    fun blur(src: View, radius: Int): Bitmap {
        val bitmap = getBitmapForView(src, 1f)
        return blur(bitmap, radius)
    }

    fun fastBlur(src: View, radius: Int, downscaleFactor: Float): Bitmap {
        val bitmap = getBitmapForView(src, downscaleFactor)
        return blur(bitmap, radius)
    }

    private fun getBitmapForView(src: View, downscaleFactor: Float): Bitmap {
        val bitmap = Bitmap.createBitmap(
            (src.width * downscaleFactor).toInt(),
            (src.height * downscaleFactor).toInt(),
            Bitmap.Config.ARGB_4444
        )

        val canvas = Canvas(bitmap)
        val matrix = Matrix()
        matrix.preScale(downscaleFactor, downscaleFactor)
        canvas.setMatrix(matrix)
        src.draw(canvas)

        return bitmap
    }

    companion object {

        private var instance: BlurKit? = null

        fun init(context: Context) {
            if (instance != null) {
                return
            }

            instance = BlurKit()
            instance!!.rs = RenderScript.create(context)
        }

        fun getInstance(): BlurKit {
            if (instance == null) {
                throw RuntimeException("BlurKit not initialized!")
            }

            return instance as BlurKit
        }
    }

}