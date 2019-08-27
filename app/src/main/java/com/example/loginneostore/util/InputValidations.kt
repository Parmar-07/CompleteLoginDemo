package com.example.loginneostore.util

import java.util.regex.Pattern

class InputValidations private constructor() {

    companion object {
        private var inputValidations: InputValidations? = null
        fun getInputValidation(): InputValidations {
            if (inputValidations == null) {
                inputValidations = InputValidations()
            }
            return inputValidations as InputValidations
        }

        const val EMAIL_PATTERN: String =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        const val SPECIAL_CHAR_PASSWORD_PATTERN: String = "^[a-zA-Z@#\$%]\\\\w{5,19}\$"
        const val PASSWORD_PATTERN: String = "^[a-zA-Z0-9]\\w{5,19}$"
//        const val FULLNAME_PATTERN: String = "^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+\$"
        const val FULLNAME_PATTERN: String = "^[a-zA-z]+([\\s][a-zA-Z]+)+\$"
    }

    fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isValidFullName(fullName: String): Boolean {
        val pattern = Pattern.compile(FULLNAME_PATTERN)
        val matcher = pattern.matcher(fullName)
        return matcher.matches()
    }


    fun isValidPassword(password: String, isSpecialChar: Boolean = false): Boolean {

        val PATTERN: String = if (isSpecialChar)
            SPECIAL_CHAR_PASSWORD_PATTERN
        else PASSWORD_PATTERN

        val pattern = Pattern.compile(PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()


    }

    fun isValidRangeLength(str: String, xlen: Int, ylen: Int, isEqualTo: Boolean = true): Boolean {

        val len = str.length
        return if (isEqualTo) {
            (len in ylen..xlen)
        } else {
            (len in (ylen + 1) until xlen)
        }

    }

    fun isValidLength(str: String, vlen: Int, min: Boolean = true): Boolean {
        val len = str.length
        return if (min) {
            (len <= vlen)
        } else {
            (len >= vlen)
        }
    }



}