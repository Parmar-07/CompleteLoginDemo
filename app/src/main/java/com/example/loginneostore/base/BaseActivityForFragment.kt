package com.example.loginneostore.base

import androidx.fragment.app.Fragment
import com.example.loginneostore.R

abstract class BaseActivityForFragment<PRESENTER : BasePresenter> : BaseActivity<PRESENTER>() {

    abstract fun loadFragmentIn(): Int

    fun loadFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false,
        isTransition: Boolean = false,
        isLTR: Boolean = false
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        if (isTransition) {
            if (isLTR) {
                transaction.setCustomAnimations(
                    R.anim.slide_from_left,
                    R.anim.slide_to_right,
                    R.anim.slide_from_right,
                    R.anim.slide_to_left

                )
            } else {
                transaction.setCustomAnimations(
                    R.anim.slide_from_right,
                    R.anim.slide_to_left,
                    R.anim.slide_from_left,
                    R.anim.slide_to_right
                )
            }

        }

        transaction.replace(loadFragmentIn(), fragment, fragment.javaClass.simpleName)

        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commit()

    }


}