package com.example.myapplication.view.join;

import com.example.myapplication.view.main.ErrorCode;

public interface JoinNavigator {
    void OnSuccess();
    void OnError(ErrorCode code);
}
