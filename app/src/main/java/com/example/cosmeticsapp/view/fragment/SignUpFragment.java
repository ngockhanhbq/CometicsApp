package com.example.cosmeticsapp.view.fragment;

import android.view.View;
import android.widget.RadioGroup;

import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.FragmentLoginBinding;
import com.example.cosmeticsapp.databinding.FragmentSignUpBinding;
import com.example.cosmeticsapp.entity.User;
import com.example.cosmeticsapp.viewmodel.SignUpViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SignUpFragment extends BaseFragment<FragmentSignUpBinding, SignUpViewModel> {
    public static final String KEY_BACK_LOGIN_FRAGMENT = "KEY_BACK_LOGIN_FRAGMENT";
    private String selectedText;
    private List<String> roles;

    @Override
    protected Class<SignUpViewModel> getViewModelClass() {
        return SignUpViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected void initViews() {
        roles = new ArrayList<>();
//        int selectedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();
//
//        if (selectedRadioButtonId != -1) {
//            RadioButton radioButton = binding.radioGroup.findViewById(selectedRadioButtonId);
//             selectedText = radioButton.getText().toString();
//
//            // Sử dụng selectedText theo nhu cầu của bạn
//        }
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.role_user) {
                    // Xử lý khi RadioButton "USER" được chọn
                    selectedText = "USER";
                }
//                } else if (checkedId == R.id.role_shipper) {
//                    // Xử lý khi RadioButton "SHIPPER" được chọn
//                    selectedText = "SHIPPER";
//                }
            }
        });
//        if (binding.roleUser.isChecked()) {
//
//        }else if (binding.roleShipper.isChecked()) {
//
//        }
        roles.add(selectedText);
        binding.btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.loginUsernameAndPassword(new User(binding.edtCreateName.getText().toString(),
                        Integer.parseInt(binding.edtCreateAge.getText().toString()),binding.edtCreateUsername.getText().toString()
                ,binding.edtCreatePassword.getText().toString(),binding.edtCreatePhone.getText().toString(),
                        binding.edtCreateHomeAddress.getText().toString(), new ArrayList<>(Arrays.asList(selectedText))));
                callBack.callBack(KEY_BACK_LOGIN_FRAGMENT,null);
            }
        });
    }


}
