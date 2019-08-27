package com.example.loginneostore.setup

open class Tester : ITester {

    override fun testCaseName(case: String) {
        println("\nCase : $case")

    }

    override fun onTestFail(testResult: String, expectedResult: String) {
        println("..................FAILED..................")
        println("Expected : $expectedResult")
        println("Actual : $testResult")
        try {
            assert(false)
        } catch (e: java.lang.AssertionError) {

        }
    }

    override fun onTestPass(result: String) {
        println("PASSED : $result")
        assert(true)
    }

    override fun onTestError(error: String) {
        println("ERROR : $error")
    }


}