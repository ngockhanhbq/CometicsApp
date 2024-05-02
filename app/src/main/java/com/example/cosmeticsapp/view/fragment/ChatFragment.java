package com.example.cosmeticsapp.view.fragment;

import android.net.Uri;
import android.view.View;
import android.widget.ScrollView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.cosmeticsapp.Constants;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ChatFragmentBinding;
import com.example.cosmeticsapp.entity.Message;
import com.example.cosmeticsapp.entity.User;
import com.example.cosmeticsapp.view.adapter.ChatAdapter;
import com.example.cosmeticsapp.viewmodel.ChatViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChatFragment extends BaseFragment<ChatFragmentBinding, ChatViewModel> {
    private User receiverUser;
    private String senderUserUid;
    private ChatAdapter chatAdapter;

    @Override
    protected Class<ChatViewModel> getViewModelClass() {
        return ChatViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.chat_fragment;
    }

    @Override
    protected void initViews() {

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
        binding.scrollView.post(runnable);
        senderUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mViewModel.setChatRoom(senderUserUid, receiverUser.getId()+"");
        mViewModel.loadMessage();
        Glide.with(requireContext()).load(Uri.parse(receiverUser.getAvatarUrl())).into(binding.receiveUserAvatar);
        binding.receiveUserName.setText(receiverUser.getUsername());
        binding.sendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messText = binding.sendingMess.getText().toString();
                if(!messText.equals("")){
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    mViewModel.sendMessage(senderUserUid, receiverUser.getId()+"", messText, date);
                    binding.sendingMess.setText("");
                    scrollToBottom();
                }
            }
        });
        binding.messageRecycleView.setHasFixedSize(true);
        binding.messageRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.messageRecycleView.setNestedScrollingEnabled(false);
        chatAdapter = new ChatAdapter(mViewModel.getMessages(), receiverUser.getAvatarUrl());
        binding.messageRecycleView.setAdapter(chatAdapter);

        binding.sendingMess.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToBottom();
            }
        });

        binding.sendingMess.setOnClickListener(v -> scrollToBottom());


        mViewModel.getMessageLiveData().observe(this, new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                chatAdapter.notifyDataSetChanged();
            }
        });
        binding.arrowBack.setOnClickListener(v -> gotoHomeFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.seenMess();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void scrollToBottom() {
        binding.scrollView.smoothScrollTo(0, binding.scrollView.getBottom());
    }

    public void getUser(User user){
        this.receiverUser = user;
    }

    private void gotoHomeFragment(){
        callBack.callBack(Constants.KEY_SHOW_HOME, null);
    }
}
