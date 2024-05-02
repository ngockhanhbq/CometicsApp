package com.example.cosmeticsapp.view.fragment;

import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.api.ApiClient;
import com.example.cosmeticsapp.databinding.FragmentOrderBinding;
import com.example.cosmeticsapp.databinding.FragmentProfileBinding;
import com.example.cosmeticsapp.databinding.FragmentSplashBinding;
import com.example.cosmeticsapp.view.act.ChangePasswordActivity;
import com.example.cosmeticsapp.view.act.CustomerServiceActivity;
import com.example.cosmeticsapp.view.act.EditUserActivity;
import com.example.cosmeticsapp.view.act.MainActivity;
import com.example.cosmeticsapp.view.act.PromotionActivity;
import com.example.cosmeticsapp.viewmodel.ProfileViewModel;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> {

    @Override
    protected Class<ProfileViewModel> getViewModelClass() {
        return ProfileViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initViews() {
       binding.tvNameProfile.setText(App.getInstance().getUser().getUsername());
       if ( App.getInstance().getUser().getAvatarUrl() == null){
           binding.profileImage.setImageResource(R.drawable.ic_user_receiver);
       } else {
           Glide.with(this).load(""+ApiClient.BASE_URL+"/user/download?filename="+
                   App.getInstance().getUser().getAvatarUrl()).into(binding.profileImage);
       }
       binding.frEditUser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getActivity(), EditUserActivity.class);
               startActivity(intent);
           }
       });
        binding.frPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PromotionActivity.class);
                startActivity(intent);
            }
        });

        binding.frChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        binding.frCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomerServiceActivity.class);
                startActivity(intent);
            }
        });
        binding.btLogout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getActivity(),MainActivity.class);
               startActivity(intent);
               getActivity().finish();
           }
       });
    }
}
