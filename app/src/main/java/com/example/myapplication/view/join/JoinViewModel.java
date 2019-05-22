package com.example.myapplication.view.join;

import android.util.Log;
import com.example.myapplication.data.DataSource;
import com.example.myapplication.data.model.User;
import com.example.myapplication.view.base.BaseViewModel;
import com.example.myapplication.view.main.ErrorCode;
import io.reactivex.functions.Consumer;
import org.jetbrains.annotations.NotNull;

public class JoinViewModel extends BaseViewModel {

    private String TAG=JoinViewModel.class.getSimpleName();
    private JoinNavigator navigator;

    public JoinViewModel(@NotNull DataSource dataManager, JoinNavigator navigator) {
        super(dataManager);
        this.navigator = navigator;
    }

    //ViewModel 에서는 subscribe를 통해 데이터를 받아와서 처리
    //getDataManager사용할때 getCompositeDisposable.add()해서 사용할것
    public void OKButtonClicked(String id, String pw,String name, String nickname) {
        getCompositeDisposable().add(
                getDataManager().join(id, pw,name,nickname)
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer data) throws Exception {
                                if(data==200){
                                    navigator.OnSuccess();
                                }
                                else {
                                    navigator.OnError(ErrorCode.WRONG_PARAMETER);
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
}
