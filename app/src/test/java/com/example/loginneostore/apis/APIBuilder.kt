package com.example.loginneostore.apis

import android.content.Context
import android.net.ConnectivityManager
import com.example.loginneostore.data.network.NetworkServices
import com.example.loginneostore.data.repositories.LoginRepository
import com.example.loginneostore.setup.MockSetup
import com.example.loginneostore.setup.TestAPIs
import com.example.loginneostore.setup.Tester
import com.example.loginneostore.util.NetworkInterceptor
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import retrofit2.Response

@RunWith(PowerMockRunner::class)
@PrepareForTest(
    Context::class,
    ConnectivityManager::class
)
@PowerMockIgnore("org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*")//for SSL Pin
abstract class APIBuilder : Tester(), TestAPIs {


    lateinit var context: Context
    lateinit var networkIntercept: NetworkInterceptor
    lateinit var restService: NetworkServices
    lateinit var repo: LoginRepository
    private val mockSetup = MockSetup()
    protected var apiDisposable: Disposable? = null


    @Before
    open fun setupMock() {
        context = mockSetup.mockContext()
        mockSetup.mockConnectionManger(context)
        networkIntercept = mockSetup.mockNetworkInterceptor(context)
        restService = mockSetup.mockNetworkRestService(networkIntercept)
        repo = LoginRepository(restService)
    }


    override fun testSignUp() {
        testCaseName("Testing API SignUp")
    }

    override fun testSignIn() {
        testCaseName("Testing API SignIn")
    }


    fun <T> Single<Response<T>>.testAPI(): Disposable {


        this.subscribeOn(Schedulers.trampoline())
        this.observeOn(Schedulers.trampoline())

        return this.subscribe({}, {
            onTestError(it.message.toString())
        })
    }

    fun <T> Single<Response<T>>.matchStatusCode(matchStatusCode: Int = 200): Single<Response<T>> {


        return this
            .doAfterSuccess { response ->
                if (response.isSuccessful) {
                    onTestPass("status code ${response.code()}")
                } else {
                    onTestFail("status code ${response.code()}", "status code 200")
                }
            }

    }


    @After
    open fun setupAfterTest() {
        if (apiDisposable?.isDisposed != false) {
            apiDisposable?.dispose()
        }
    }

    fun singleAPITester(apiTester: () -> Disposable) {
        apiDisposable = apiTester()
    }


}