package com.example.loginneostore.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment<PRESENTER : BasePresenter> : Fragment() {


    var screenContext: Context? = null
    var mView: View? = null
    abstract fun loadViewFrom(): Int
    abstract fun onScreenLoad()
    abstract fun onScreenLoaded(view: View)
    abstract fun onScreenViewListeners()
    abstract fun onScreenCleared()
    private fun onScreenDestroy() {}
    abstract var presenterProvider: PRESENTER?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onScreenLoad()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(loadViewFrom(), container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onScreenLoaded(view)
        presenterProvider?.loadPresenter()
        onScreenViewListeners()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let {
            screenContext = it
        }
    }


    override fun onDestroyView() {
        presenterProvider?.destroyPresenter()
        onScreenDestroy()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenterProvider = null
        screenContext = null
        mView = null
        Runtime.getRuntime().gc()
        System.gc()
        super.onDestroy()
        onScreenCleared()

    }


}