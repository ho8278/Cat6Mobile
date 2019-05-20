package com.example.myapplication.view.login;

import com.example.myapplication.view.main.ErrorCode;

public interface LoginNavigator {
    void OnSuccess();
    void OnError(ErrorCode code);
}