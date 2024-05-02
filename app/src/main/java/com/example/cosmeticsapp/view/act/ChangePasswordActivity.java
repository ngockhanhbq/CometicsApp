package com.example.cosmeticsapp.view.act;

import android.view.View;

import androidx.lifecycle.Observer;

import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ActivityChangePasswordBinding;
import com.example.cosmeticsapp.databinding.ActivityEditUserBinding;
import com.example.cosmeticsapp.viewmodel.UserEditViewModel;

public class ChangePasswordActivity extends BaseActivity<ActivityChangePasswordBinding, UserEditViewModel> {

    @Override
    protected Class<UserEditViewModel> getViewModelClass() {
        return UserEditViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initViews() {

       binding.tvEdit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               viewModel.changePasswordSession(binding.tvOldpassword.getText().toString(),binding.tvNewpassword.getText().toString()
               );
           }
       });
        viewModel.getIsChangePassword().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    finish();
                }
            }
        });
    }

    @Override
    public void callBack(String key, Object data) {

    }
}
