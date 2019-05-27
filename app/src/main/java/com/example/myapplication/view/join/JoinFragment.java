package com.example.myapplication.view.join;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.R;
import com.example.myapplication.data.DataSource;
import com.example.myapplication.databinding.ActivityJoinBinding;
import com.example.myapplication.view.base.BaseFragment;
import com.example.myapplication.view.main.ErrorCode;
import org.jetbrains.annotations.NotNull;

public class JoinFragment extends BaseFragment<ActivityJoinBinding, JoinViewModel> implements JoinNavigator {
    String id, pw,name,nickname;
    @NotNull
    @Override
    public String getTAG() {
        return JoinFragment.class.getSimpleName();
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();
    }


    @Override
    public void OnError(ErrorCode code) {
        Toast.makeText(getActivity(),"회원 가입 실패",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.joinOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = binding.etId.getText().toString();
                pw = binding.etPw.getText().toString();
                name = binding.etName.getText().toString();
                nickname = binding.etNickname.getText().toString();
                Log.e("TEST",id+", "+pw +", "+name+", "+nickname);
                viewModel.OKButtonClicked(id,pw,name,nickname);
            }
        });
    }

}
