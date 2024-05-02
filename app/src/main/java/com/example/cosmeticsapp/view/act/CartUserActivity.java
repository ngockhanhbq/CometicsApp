package com.example.cosmeticsapp.view.act;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.CartSession;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ActivityCartUserBinding;
import com.example.cosmeticsapp.entity.Products;
import com.example.cosmeticsapp.view.adapter.CartUserAdapter;
import com.example.cosmeticsapp.viewmodel.CartViewModel;

import java.util.Map;

public class CartUserActivity extends BaseActivity<ActivityCartUserBinding, CartViewModel> implements CartUserAdapter.OnItemClick {

    private CartUserAdapter cartUserAdapter;

    private String moneyCart;
    @Override
    protected Class<CartViewModel> getViewModelClass() {
        return CartViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_user;
    }

    @Override
    protected void initViews() {
        cartUserAdapter = new CartUserAdapter(App.getInstance().getStorage().getCartSessionList(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvCartUser.setLayoutManager(linearLayoutManager);
        binding.rcvCartUser.setAdapter(cartUserAdapter);
        cartUserAdapter.setOnItemClick(this);
        double sum = 0;
        for (int i = 0; i < App.getInstance().getStorage().getCartSessionList().size(); i++) {
            double value = calculateTotalPrice(App.getInstance().getStorage().getCartSessionList().get(i))+15000;
            sum += value;
        }
        moneyCart = sum+"";
        binding.tvOrderConfirm.setText("Thanh toán : "+moneyCart);
        binding.tvOrderConfirm.setVisibility(View.GONE);
        binding.ivAllCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvOrderConfirm.setVisibility(View.VISIBLE);
            }
        });
    }

    public double calculateTotalPrice(CartSession data) {
        double totalPrice = 0;

        // Duyệt qua các sản phẩm trong HashMap
        for (Map.Entry<Integer, Integer> entry : data.getProductQuantityMap().entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            // Tìm sản phẩm tương ứng trong danh sách sản phẩm
            for (Products product : data.getProductsList()) {
                if (product.getId() == productId) {
                    // Tính tổng giá tiền bằng cách nhân giá tiền của sản phẩm với số lượng
                    totalPrice += product.getPrice() * quantity;
                    break;
                }
            }
        }

        return totalPrice;
    }

    @Override
    public void callBack(String key, Object data) {

    }

    @Override
    public void onItemClick(CartSession cartSession) {

    }
}