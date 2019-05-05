package com.example.myapplication.view.main

enum class ErrorCode(val code: Int, val description: String) {
    WRONG_PARAMETER(100, "Wrong Parameter Error"),
    LATE_START_DATE(101, "StartDate is later than EndDate"),
    EMPTY_TEXT(102,"Text is empty"),
    SUCCESS(200, "Success");

    companion object {
        fun fromCode(code: Int): ErrorCode = ErrorCode.values().first { code == it.code }
    }
}