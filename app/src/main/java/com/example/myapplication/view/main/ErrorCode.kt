package com.example.myapplication.view.main

enum class ErrorCode(val errorCode: Int, val description:String){
    WRONG_PARAMETER(100,"Wrong Parameter Error"),
    LATE_START_DATE(101,"StartDate is later than EndDate")
}