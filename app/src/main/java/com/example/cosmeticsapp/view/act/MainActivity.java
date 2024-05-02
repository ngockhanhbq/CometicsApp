package com.example.cosmeticsapp.view.act;

import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ActivityMainBinding;
import com.example.cosmeticsapp.view.fragment.HomeFragment;
import com.example.cosmeticsapp.view.fragment.LoginFragment;
import com.example.cosmeticsapp.view.fragment.SignUpFragment;
import com.example.cosmeticsapp.view.fragment.SplashFragment;
import com.example.cosmeticsapp.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void initViews() {
        SplashFragment splashFragment = new SplashFragment();
        splashFragment.setCallBack(this);
        showFragment(R.id.container_view, splashFragment, false);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void callBack(String key, Object data) {
        switch (key) {
            case SplashFragment.KEY_SHOW_LOGIN_FRAGMENT:
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.setCallBack(this);
                showFragment(R.id.container_view, loginFragment, false);
                break;
            case LoginFragment.KEY_SHOW_HOME_FRAGMENT:
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setCallBack(this);
                showFragment(R.id.container_view, homeFragment, true);
                break;
            case LoginFragment.KEY_SHOW_SIGN_UP_FRAGMENT:
                SignUpFragment signUpFragment = new SignUpFragment();
                signUpFragment.setCallBack(this);
                showFragment(R.id.container_view, signUpFragment, true);
                break;
            case SignUpFragment.KEY_BACK_LOGIN_FRAGMENT:
                LoginFragment lgFragment = new LoginFragment();
                lgFragment.setCallBack(this);
                showFragment(R.id.container_view, lgFragment, true);
                break;
        }
    }
}