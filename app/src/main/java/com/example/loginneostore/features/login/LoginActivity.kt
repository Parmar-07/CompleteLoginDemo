package com.example.loginneostore.features.login

import android.graphics.Color
import android.view.View
import android.webkit.WebView
import com.example.loginneostore.R
import com.example.loginneostore.base.BaseActivityForFragment
import com.example.loginneostore.data.network.rss.XmlService
import com.example.loginneostore.data.repositories.LoginRepository
import com.example.loginneostore.features.login.signIn.SignIn
import com.example.loginneostore.features.login.signup.SignUp
import com.example.loginneostore.util.NetworkInterceptor
import com.example.loginneostore.util.htmlNewsPreview
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.news_view.*

class LoginActivity : BaseActivityForFragment<LoginPresenter>(), LoginView {
    override fun onLoadedLatestNews(news: String) {

        val config = resources.configuration
        val textSize = if (config.smallestScreenWidthDp >= 600) 5 else 3
        val htmlNews = news.htmlNewsPreview(textSize)
        rssWeb.loadData(htmlNews, "text/html", "UTF-8")
        rssWeb.setBackgroundColor(Color.TRANSPARENT)
        rssWeb.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        rssWeb.visibility = View.VISIBLE

    }


    override fun onScreenViewListeners() {
        toolBarBack.setOnClickListener {
            openLoginScreen()

        }
    }


    override fun setScreenBack(visible: Boolean) {
        toolBarBack.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }


    override fun setScreenTitle(title: String) {
        toolBarTitle.text = title
    }


    override fun openLoginScreen() {
        setScreenBack(false)
        setScreenTitle(resStr(R.string.txt_login))
        loadFragment(
            SignIn(),
            isTransition = true
        )
    }

    override fun openSignUpScreen() {

        setScreenTitle(resStr(R.string.txt_register))
        setScreenBack(true)
        loadFragment(
            SignUp(),
            isTransition = true,
            isLTR = true
        )
    }


    override var presenterProvider = LoginPresenter(
        this,
        LoginRepository(XmlService.invoke(NetworkInterceptor.getNetworkInterceptorInstance(this)))
    )


    override fun loadFragmentIn(): Int = R.id.container

    override fun loadViewFrom(): Int = R.layout.activity_login

    override fun onScreenCreated() {

        presenterProvider.loadLatestNews()

    }


}
