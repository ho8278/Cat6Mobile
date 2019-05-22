package com.example.myapplication.view.join;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myapplication.data.DataSource;
import com.example.myapplication.databinding.ActivityJoinBinding;
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

public class JoinActivity extends BaseFragment<ActivityJoinBinding, JoinViewModel> implements JoinNavigator {
    EditText et_id, et_pw,et_name,et_nickname;
    String id, pw,name,nickname;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        et_id = getActivity().findViewById(R.id.et_id);
        et_pw = getActivity().findViewById(R.id.et_pw);
        et_name = getActivity().findViewById(R.id.et_id);
        et_nickname = getActivity().findViewById(R.id.et_pw);
        binding.joinOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = et_id.getText().toString();
                pw = et_pw.getText().toString();
                name = et_id.getText().toString();
                nickname = et_pw.getText().toString();
                viewModel.OKButtonClicked(id,pw,name,nickname);
            }
        });
    }

}
