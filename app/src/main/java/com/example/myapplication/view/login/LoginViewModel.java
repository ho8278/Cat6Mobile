package com.example.myapplication.view.login;

import android.util.Log;
import com.example.myapplication.data.DataSource;
import com.example.myapplication.data.model.User;
import com.example.myapplication.view.base.BaseViewModel;
import io.reactivex.functions.Consumer;
import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends BaseViewModel {

    private String TAG=LoginViewModel.class.getSimpleName();
    private LoginNavigator navigator;

    public LoginViewModel(@NotNull DataSource dataManager, LoginNavigator navigator) {
        super(dataManager);
        this.navigator = navigator;
    }

    //ViewModel 에서는 subscribe를 통해 데이터를 받아와서 처리
    //getDataManager사용할때 getCompositeDisposable.add()해서 사용할것
    public void loginButtonClicked(String id, String pw) {
        getCompositeDisposable().add(
                getDataManager().login(id, pw)
                        .subscribe(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {
                                Log.e(TAG, user.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, throwable.getMessage());
                            }
                        })
        );
    }
}
