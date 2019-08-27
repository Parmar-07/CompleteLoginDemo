package com.example.loginneostore.base

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import com.example.loginneostore.R
import com.example.loginneostore.util.PopMessages
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.layout_progress_error.*

abstract class BaseActivity<PRESENTER : BasePresenter> : AppCompatActivity() {

    lateinit var screenContext: Context
    abstract fun loadViewFrom(): Int
    abstract fun onScreenCreated()
    abstract fun onScreenViewListeners()

    private fun onScreenDestroy() {}
    abstract var presenterProvider: PRESENTER
    private var animLoader: AnimationDrawable? = null
    private var container: FrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenContext = this

        setContentView(R.layout.activity_base)
        if (loadViewFrom() != 0) {

            container = this.findViewById<FrameLayout>(R.id.base_container)
            val subView = LayoutInflater.from(this).inflate(loadViewFrom(), null, true)
            container?.removeAllViews()
            container?.addView(subView)

            // setContentView(loadViewFrom())
            onScreenCreated()

            presenterProvider.loadPresenter()
            onScreenViewListeners()


        }

        closeError.setOnClickListener {
            dismissPopErrorLoading()
        }

    }

    override fun onResume() {
        super.onResume()
        presenterProvider.onResumePresenter()
    }

    override fun onDestroy() {
        presenterProvider.destroyPresenter()
        onScreenDestroy()
        container = null
        super.onDestroy()
    }

    fun popToast(toastMessage: String, toastLength: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(screenContext, toastMessage, toastLength).show()
    }

    fun popMessage(message: String) {
        PopMessages.normal(screenContext, message)
    }

    fun popWarning(message: String) {
        PopMessages.warning(screenContext, message)
    }

    fun popError(message: String) {
        PopMessages.error(screenContext, message)
    }


     fun runWithUI(runUiAction : () -> Unit){
        runOnUiThread {
            runUiAction()
        }
    }


    fun popSuccess(message: String) {
        PopMessages.success(screenContext, message)
    }

    fun resStr(resId: Int): String {
        return if (resId == 0) "" else getString(resId)
    }

    fun popNoInternetError() {


        i_error.visibility = View.VISIBLE
        val drawable = AppCompatResources.getDrawable(screenContext, R.drawable.ic_no_internet)
        imgError.setImageDrawable(drawable)
        imgError.setPadding(0)
        imgError.scaleType = ImageView.ScaleType.FIT_CENTER

        errorTitle.text = resStr(R.string.network_error_title)
        errorMessage.text = resStr(R.string.network_error_message)
        closeError.visibility = View.VISIBLE

    }


    fun popStartLoading() {


            i_error.visibility = View.VISIBLE
            imgError.scaleType = ImageView.ScaleType.CENTER_INSIDE
            val paddingDp = 80
            val density = resources.displayMetrics.density
            val paddingPixel = (paddingDp * density).toInt()
            imgError.setPadding(paddingPixel)
            val drawable = AppCompatResources.getDrawable(screenContext, R.drawable.loader)
            imgError.setImageDrawable(drawable)
            animLoader = drawable as? AnimationDrawable
            animLoader?.start()

            errorTitle.text = resStr(R.string.loading_title)
            errorMessage.text = resStr(R.string.loading_message)
            closeError.visibility = View.GONE



    }

    fun popStopLoading(eTitle: String, eMessage: String) {

            i_error.visibility = View.VISIBLE
            imgError.scaleType = ImageView.ScaleType.CENTER_INSIDE
            val paddingDp = 80
            val density = resources.displayMetrics.density
            val paddingPixel = (paddingDp * density).toInt()
            imgError.setPadding(paddingPixel)
            val drawable = AppCompatResources.getDrawable(screenContext, R.drawable.loader)
            imgError.setImageDrawable(drawable)
            animLoader = drawable as? AnimationDrawable
            animLoader?.stop()
            errorTitle.text = eTitle
            errorMessage.text = eMessage
            closeError.visibility = View.VISIBLE


    }

    fun dismissPopErrorLoading() {
            i_error.visibility = View.GONE

    }


}