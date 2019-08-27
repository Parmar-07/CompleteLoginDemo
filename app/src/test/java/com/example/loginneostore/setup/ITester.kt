package com.example.loginneostore.setup

interface ITester {
    fun onTestFail(testResult: String, expectedResult: String)
    fun onTestPass(result: String)
    fun onTestError(error: String)
    fun testCaseName(case: String)
}