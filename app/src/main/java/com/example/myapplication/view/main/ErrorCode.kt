package com.example.myapplication.view.main

enum class ErrorCode(val code: Int, val description: String) {
    WRONG_PARAMETER(100, "Wrong Parameter Error"),
    LATE_START_DATE(101, "StartDate is later than EndDate"),
    EMPTY_TEXT(102,"Text is empty"),
    NOT_PATTERN_MATCH(103,"Date Pattern is not matched"),
    NOT_SELECTED(104, "검색 된 유저가 없습니다."),
    EMPTY_TEAM_LIST(105,"Team List is Empty. Please Enter the team."),
    LOGIN_DENY(106,"아이디 비밀번호가 틀렸습니다."),
    SUCCESS(200, "Success");

    companion object {
        fun fromCode(code: Int): ErrorCode = ErrorCode.values().first { code == it.code }
    }
}