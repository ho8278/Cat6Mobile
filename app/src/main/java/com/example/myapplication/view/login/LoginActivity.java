package com.example.myapplication.view.login;

import com.example.myapplication.data.DataSource;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.view.base.BaseActivity;
import com.example.myapplication.view.base.BaseViewModel;
import com.example.myapplication.view.main.ErrorCode;
import org.jetbrains.annotations.NotNull;
import com.example.myapplication.R;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {
    EditText et_id, et_pw;
    String id, pw;

    @NotNull
    @Override
    public String getTAG() {
        return LoginActivity.class.getSimpleName();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @NotNull
    @Override
    public LoginViewModel getViewModel(@NotNull DataSource dataSource) {
        return new LoginViewModel(dataSource, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = et_id.getText().toString();
                pw = et_pw.getText().toString();
                viewModel.loginButtonClicked(id, pw);
            }
        });
    }

    @Override
    public void OnSuccess() {

    }

    @Override
    public void OnError(ErrorCode code) {

    }
}
