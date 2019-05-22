package com.example.myapplication.view.join;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myapplication.data.DataSource;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.view.base.BaseActivity;
import com.example.myapplication.view.base.BaseFragment;
import com.example.myapplication.view.base.BaseViewModel;
import com.example.myapplication.view.main.ErrorCode;
import org.jetbrains.annotations.NotNull;
import com.example.myapplication.R;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;

public class JoinActivity extends BaseFragment<ActivityLoginBinding, JoinViewModel> implements JoinNavigator {
    @NotNull
    @Override
    public String getTAG() {
        return JoinActivity.class.getSimpleName();
    }

    @NotNull
    @Override
    public JoinViewModel getViewModel(@NotNull DataSource dataManager) {
        return new JoinViewModel(dataManager, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_join;
    }

    @Override
    public void OnSuccess() {
        Toast.makeText(getActivity(),"회원 가입 성공",Toast.LENGTH_LONG).show();
    }


    @Override
    public void OnError(ErrorCode code) {
        Toast.makeText(getActivity(),"회원 가입 실패",Toast.LENGTH_LONG).show();
    }
}
