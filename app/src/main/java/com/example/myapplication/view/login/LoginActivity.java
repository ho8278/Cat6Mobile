package com.example.myapplication.view.login;

import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.data.DataSource;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.view.base.BaseActivity;
import com.example.myapplication.view.join.JoinFragment;
import com.example.myapplication.view.main.ErrorCode;
import com.example.myapplication.view.main.MainActivity;
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

        viewModel.init();

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

        binding.checkboxAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    viewModel.autoLogin(isChecked,et_id.getText().toString(), et_pw.getText().toString());
                else
                    viewModel.autoLogin(isChecked,et_id.getText().toString(), et_pw.getText().toString());
            }
        });


        binding.Join.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragement_join, new JoinFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void OnSuccess() {
        Toast.makeText(this,"로그인 성공",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void OnError(ErrorCode code) {
        Toast.makeText(this,"로그인 실패",Toast.LENGTH_LONG).show();
    }
}
