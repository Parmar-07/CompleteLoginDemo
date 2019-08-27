package com.example.loginneostore.util


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.example.loginneostore.R
import java.lang.ref.WeakReference

class BlurLayout : FrameLayout {

    // Customizable attributes

    /** Factor to scale the view bitmap with before blurring.  */
    private var mDownscaleFactor: Float = 0.toFloat()

    /** Blur radius passed directly to stackblur library.  */
    private var mBlurRadius: Int = 0

    /** Number of blur invalidations to do per second.   */
    private var mFPS: Int = 0

    // Calculated class dependencies

    /** Reference to View for top-parent. For retrieval see [getActivityView][.getActivityView].  */
    private var mActivityView: WeakReference<View>? = null

    /** Choreographer callback that re-draws the blur and schedules another callback.  */
    private val invalidationLoop = @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            invalidate()
            Choreographer.getInstance().postFrameCallbackDelayed(this, (1000 / mFPS).toLong())
        }
    }
    /**
     * Casts context to Activity and attempts to create a view reference using the window decor view.
     * @return View reference for whole activity.
     */
    private val activityView: View?
        get() {
            val activity: Activity
            try {
                activity = context as Activity
            } catch (e: ClassCastException) {
                return null
            }

            return activity.window.decorView.findViewById(android.R.id.content)
        }

    /**
     * Returns the position in screen. Left abstract to allow for specific implementations such as
     * caching behavior.
     */
    private val positionInScreen: Point
        get() = getPositionInScreen(this)

    constructor(context: Context) : super(context, null) {}

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        BlurKit.init(context)

        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BlurLayout,
            0, 0
        )

        try {
            mDownscaleFactor = a.getFloat(R.styleable.BlurLayout_downscaleFactor, DEFAULT_DOWNSCALE_FACTOR)
            mBlurRadius = a.getInteger(R.styleable.BlurLayout_blurRadiusNew, DEFAULT_BLUR_RADIUS)
            mFPS = a.getInteger(R.styleable.BlurLayout_fps, DEFAULT_FPS)
        } finally {
            a.recycle()
        }

        if (mFPS > 0) {
            Choreographer.getInstance().postFrameCallback(invalidationLoop)
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun invalidate() {
        super.invalidate()
        val bitmap = blur()
        if (bitmap != null) {
            background = BitmapDrawable(bitmap)
        }
    }

    /**
     * Recreates blur for content and sets it as the background.
     */
    private fun blur(): Bitmap? {
        if (context == null) {
            return null
        }

        // Check the reference to the parent view.
        // If not available, attempt to make it.
        if (mActivityView == null || mActivityView!!.get() == null) {
            mActivityView = WeakReference<View>(activityView)
            if (mActivityView!!.get() == null) {
                return null
            }
        }

        // Calculate the relative point to the parent view.
        val pointRelativeToActivityView = positionInScreen

        // Set alpha to 0 before creating the parent view bitmap.
        // The blur view shouldn't be visible in the created bitmap.
        alpha = 0f

        // Screen sizes for bound checks
        val screenWidth = mActivityView!!.get()?.width
        val screenHeight = mActivityView!!.get()?.height

        // The final dimensions of the blurred bitmap.
        val width = (width * mDownscaleFactor).toInt()
        val height = (height * mDownscaleFactor).toInt()

        // The X/Y position of where to crop the bitmap.
        val x = (pointRelativeToActivityView.x * mDownscaleFactor).toInt()
        val y = (pointRelativeToActivityView.y * mDownscaleFactor).toInt()

        // Padding to add to crop pre-blur.
        // Blurring straight to edges has side-effects so padding is added.
        val xPadding = getWidth() / 8
        val yPadding = getHeight() / 8

        // Calculate padding independently for each side, checking edges.
        var leftOffset = -xPadding
        leftOffset = if (x + leftOffset >= 0) leftOffset else 0

        var rightOffset = xPadding
        rightOffset = if (x + getWidth() + rightOffset <= screenWidth!!) rightOffset else screenWidth - getWidth() - x

        var topOffset = -yPadding
        topOffset = if (y + topOffset >= 0) topOffset else 0

        var bottomOffset = yPadding
        bottomOffset = if (y + height + bottomOffset <= screenHeight!!) bottomOffset else 0

        // Create parent view bitmap, cropped to the BlurLayout area with above padding.
        var bitmap: Bitmap
        try {
            bitmap = mActivityView!!.get()?.let {
                getDownscaledBitmapForView(
                    it,
                    Rect(
                        pointRelativeToActivityView.x + leftOffset,
                        pointRelativeToActivityView.y + topOffset,
                        pointRelativeToActivityView.x + getWidth() + Math.abs(leftOffset) + rightOffset,
                        pointRelativeToActivityView.y + getHeight() + Math.abs(topOffset) + bottomOffset
                    ),
                    mDownscaleFactor
                )
            }!!
        } catch (e: NullPointerException) {
            return null
        }

        // Blur the bitmap.
        bitmap = BlurKit.getInstance().blur(bitmap, mBlurRadius)

        //Crop the bitmap again to remove the padding.
        bitmap = Bitmap.createBitmap(
            bitmap,
            (Math.abs(leftOffset) * mDownscaleFactor).toInt(),
            (Math.abs(topOffset) * mDownscaleFactor).toInt(),
            width,
            height
        )

        // Make self visible again.
        alpha = 1f

        // Set background as blurred bitmap.
        // return bitmap;
        return getRoundedCornerBitmap(bitmap)
    }

    /**
     * Finds the Point of the parent view, and offsets result by self getX() and getY().
     * @return Point determining position of the passed in view inside all of its ViewParents.
     */
    private fun getPositionInScreen(view: View): Point {
        if (parent == null) {
            return Point()
        }


        val parent: ViewGroup?
        try {
            parent = view.parent as ViewGroup
        } catch (e: Exception) {
            return Point()
        }

        if (parent == null) {
            return Point()
        }

        val point = getPositionInScreen(parent)
        point.offset(view.x.toInt(), view.y.toInt())
        return point
    }

    /**
     * Users a View reference to create a bitmap, and downscales it using the passed in factor.
     * Uses a Rect to crop the view into the bitmap.
     * @return Bitmap made from view, downscaled by downscaleFactor.
     * @throws NullPointerException
     */
    @Throws(NullPointerException::class)
    private fun getDownscaledBitmapForView(view: View, crop: Rect, downscaleFactor: Float): Bitmap {
        val screenView = view.rootView

        val width = (crop.width() * downscaleFactor).toInt()
        val height = (crop.height() * downscaleFactor).toInt()

        if (screenView.width <= 0 || screenView.height <= 0 || width <= 0 || height <= 0) {
            throw NullPointerException()
        }

        val dx = -crop.left * downscaleFactor
        val dy = -crop.top * downscaleFactor

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        val matrix = Matrix()
        matrix.preScale(downscaleFactor, downscaleFactor)
        matrix.postTranslate(dx, dy)
        canvas.setMatrix(matrix)
        screenView.draw(canvas)

        return bitmap
    }

    /**
     * Sets downscale factor to use pre-blur.
     * See [.mDownscaleFactor].
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setDownscaleFactor(downscaleFactor: Float) {
        this.mDownscaleFactor = downscaleFactor
        invalidate()
    }

    /**
     * Sets blur radius to use on downscaled bitmap.
     * See [.mBlurRadius].
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setBlurRadius(blurRadius: Int) {
        this.mBlurRadius = blurRadius
        invalidate()
    }

    /**
     * Sets FPS to invalidate blur with.
     * See [.mFPS].
     */
    fun setFPS(fps: Int) {
        this.mFPS = fps
    }

    companion object {

        val DEFAULT_DOWNSCALE_FACTOR = 0.12f
        val DEFAULT_BLUR_RADIUS = 12
        val DEFAULT_FPS = 60
        fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap {
            val output = Bitmap.createBitmap(
                bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)

            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = 3f

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)

            return output
        }
    }

}
