package com.example.cosmeticsapp.view.fragment;

import android.os.Handler;

import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.FragmentSplashBinding;
import com.example.cosmeticsapp.viewmodel.SplashViewModel;

public class SplashFragment extends BaseFragment<FragmentSplashBinding, SplashViewModel> {
    public static final String KEY_SHOW_LOGIN_FRAGMENT = "KEY_SHOW_LOGIN_FRAGMENT";
    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void initViews() {
        new Handler().postDelayed(this::gotoSignIn, 2000);

    }

    private void gotoSignIn(){
        callBack.callBack(KEY_SHOW_LOGIN_FRAGMENT, null);
    }
}
