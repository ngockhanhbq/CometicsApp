package com.example.cosmeticsapp.view.fragment;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.Observer;

import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.FragmentLoginBinding;
import com.example.cosmeticsapp.view.act.ShipActivity;
import com.example.cosmeticsapp.viewmodel.LoginViewModel;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> {
    public static final String KEY_SHOW_HOME_FRAGMENT = "KEY_SHOW_HOME_FRAGMENT";
    public static final String KEY_SHOW_SIGN_UP_FRAGMENT = "KEY_SHOW_SIGN_UP_FRAGMENT";
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initViews() {
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.loginUsernameAndPassword(binding.edtUsername.getText().toString(),
                        binding.edtPassword.getText().toString());
            }
        });

        mViewModel.getIsLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    if(mViewModel.getRoleUser().getValue().equals("SHIPPER")) {
                        startActivity(new Intent(getActivity(), ShipActivity.class));
                    } else {
                        callBack.callBack(KEY_SHOW_HOME_FRAGMENT,null);
                    }

                    mViewModel.setIsLogin(false);
                }
            }
        });
        binding.tvCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.callBack(KEY_SHOW_SIGN_UP_FRAGMENT,null);
            }
        });
    }


}
