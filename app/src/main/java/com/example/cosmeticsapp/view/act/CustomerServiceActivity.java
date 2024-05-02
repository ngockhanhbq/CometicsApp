package com.example.cosmeticsapp.view.act;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ActivityCustomerServiceBinding;
import com.example.cosmeticsapp.entity.User;
import com.example.cosmeticsapp.view.adapter.ListUserAdapter;
import com.example.cosmeticsapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceActivity extends BaseActivity<ActivityCustomerServiceBinding, UserViewModel> implements ListUserAdapter.OnItemClick {
    private ListUserAdapter usersAdapter;
    private List<User> listUser;
    @Override
    protected Class<UserViewModel> getViewModelClass() {
        return UserViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_service;
    }

    @Override
    protected void initViews() {
        listUser = new ArrayList<>();
        viewModel.getListUserServer();
        viewModel.getUserMutableLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                listUser.clear();
                listUser.addAll(users);
                usersAdapter.notifyDataSetChanged();
                Log.i("KMFG", "initViews: "+listUser.toString());
            }
        });
        //  setCallBack((OnActionCallBack) getActivity());
        usersAdapter = new ListUserAdapter(listUser,this);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.rcvUsers.setLayoutManager(manager);
        binding.rcvUsers.setAdapter(usersAdapter);
        usersAdapter.setOnItemClick(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        List<User> usersList = viewModel.getUserMutableLiveData().getValue();
        if (usersList != null) {
            usersAdapter.setListUser(usersList);
        }
        usersAdapter.setListUser(listUser);
        binding.lnUserList.setVisibility(View.VISIBLE);
    }

    @Override
    public void callBack(String key, Object data) {

    }

    @Override
    public void onItemClick(User user) {
        String phoneNumber = user.getPhonenumber();
        Intent intent = new Intent(Intent.ACTION_DIAL);

        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng điện thoại.", Toast.LENGTH_SHORT).show();
        }
    }
}
