package com.example.cosmeticsapp.view.act;

import android.view.View;

import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.api.ApiClient;
import com.example.cosmeticsapp.databinding.ActivityEditUserBinding;
import com.example.cosmeticsapp.viewmodel.UserEditViewModel;

public class EditUserActivity extends BaseActivity<ActivityEditUserBinding, UserEditViewModel> {

    @Override
    protected Class<UserEditViewModel> getViewModelClass() {
        return UserEditViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_user;
    }

    @Override
    protected void initViews() {
       binding.tvUserName.setText(App.getInstance().getUser().getUsername());
       binding.tvUserHome.setText(App.getInstance().getUser().getHomeAddress());
       binding.tvUserPhone.setText(App.getInstance().getUser().getPhonenumber());
       binding.tvUserBirthdate.setText(App.getInstance().getUser().getBirthdate());
       binding.tvName.setText(App.getInstance().getUser().getName());
       binding.tvUserAge.setText(App.getInstance().getUser().getAge()+"");
        if ( App.getInstance().getUser().getAvatarUrl() == null){
            binding.profileImage.setImageResource(R.drawable.ic_user_receiver);
        } else {
            Glide.with(this).load(""+ApiClient.BASE_URL+"/user/download?filename="+
                    App.getInstance().getUser().getAvatarUrl()).into(binding.profileImage);
        }
       binding.tvEdit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               viewModel.editUserSession(binding.tvName.getText().toString(),Integer.parseInt(binding.tvUserAge.getText().toString()),binding.tvUserPhone.getText().toString()
               ,binding.tvUserHome.getText().toString());
           }
       });
        viewModel.getIsEdit().observe(this, new Observer<Boolean>() {
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
