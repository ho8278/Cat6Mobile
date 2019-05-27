package com.example.myapplication.view.login;

import android.util.Log;
import com.example.myapplication.data.DataSource;
import com.example.myapplication.data.local.pref.PreferenceHelperImpl;
import com.example.myapplication.data.model.ServerResponse;
import com.example.myapplication.data.model.Team;
import com.example.myapplication.data.model.User;
import com.example.myapplication.view.base.BaseViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import io.reactivex.functions.Consumer;
import com.example.myapplication.view.main.ErrorCode;
import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends BaseViewModel {

    private String TAG=LoginViewModel.class.getSimpleName();
    private LoginNavigator navigator;

    public LoginViewModel(@NotNull DataSource dataManager, LoginNavigator navigator) {
        super(dataManager);
        this.navigator = navigator;
    }

    public void init(){
        String id = getDataManager().getItem(PreferenceHelperImpl.Companion.getAUTO_LOGIN_ID());
        String pw = getDataManager().getItem(PreferenceHelperImpl.Companion.getAUTO_LOGIN_PW());
        Log.e(TAG, id+", "+pw);
        if(id.isEmpty()||pw.isEmpty()) {
            return;
        }else{
            loginButtonClicked(id,pw);
        }
    }

    //ViewModel 에서는 subscribe를 통해 데이터를 받아와서 처리
    //getDataManager사용할때 getCompositeDisposable.add()해서 사용할것
    public void loginButtonClicked(String id, String pw) {
        getCompositeDisposable().add(
                getDataManager().login(id, pw)
                        .subscribe(new Consumer<ServerResponse<Team>>() {
                            @Override
                            public void accept(ServerResponse<Team> teamServerResponse) throws Exception {
                                if(teamServerResponse.getData().size()==0){
                                    navigator.OnSuccess();
                                    return;
                                }
                                if(teamServerResponse.getResponseCode().equals("200")){
                                    navigator.OnSuccess();
                                    return;
                                }
                                else {
                                    navigator.OnError(ErrorCode.WRONG_PARAMETER);
                                    return;
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, throwable.getMessage());
                            }
                        })
        );
    }

    public void autoLogin(boolean checked, String id, String pw){
        if(checked){
            getDataManager().saveItem(PreferenceHelperImpl.Companion.getAUTO_LOGIN_ID(),id);
            getDataManager().saveItem(PreferenceHelperImpl.Companion.getAUTO_LOGIN_PW(),pw);
        }else{
            getDataManager().saveItem(PreferenceHelperImpl.Companion.getAUTO_LOGIN_ID(),"");
            getDataManager().saveItem(PreferenceHelperImpl.Companion.getAUTO_LOGIN_PW(),"");
        }
    }

}
