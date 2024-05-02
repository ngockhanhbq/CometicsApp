package com.example.cosmeticsapp.view.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.OnMapCallBack;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ActivityShippingOrderBinding;
import com.example.cosmeticsapp.entity.Orders;
import com.example.cosmeticsapp.view.adapter.OrderAdapter;
import com.example.cosmeticsapp.viewmodel.ShippingOrderViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ShippingOrderActivity extends BaseActivity<ActivityShippingOrderBinding, ShippingOrderViewModel> implements OrderAdapter.OnItemClick, OnMapCallBack {
    private OrderAdapter orderAdapter;
    private List<Orders> listOrder;
    @Override
    protected Class<ShippingOrderViewModel> getViewModelClass() {
        return ShippingOrderViewModel.class;
    }

    @Override
    protected void initViews() {
        listOrder = new ArrayList<>();
        viewModel.getOrderByShipper(App.getInstance().getUser().getId());
        viewModel.getOrderShippingMutableLiveData().observe(this, new Observer<List<Orders>>() {
            @Override
            public void onChanged(List<Orders> shops) {
                listOrder.clear();
                listOrder.addAll(shops);
                orderAdapter.notifyDataSetChanged();
                Log.i("KMFG", "initViews: "+listOrder.toString());
            }
        });
        orderAdapter = new OrderAdapter(listOrder,this);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.rcvShipping.setLayoutManager(manager);
        binding.rcvShipping.setAdapter(orderAdapter);
        orderAdapter.setOnItemClick(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shipping_order;
    }


    @Override
    public void callBack(String key, Object data) {

    }

    @Override
    public void onItemClick(Orders orders) {
        AlertDialog alert=new AlertDialog.Builder(this).create();
        alert.setTitle("Hoàn thành đơn hàng này");
        alert.setButton(DialogInterface.BUTTON_POSITIVE,
                "Hoàn thành", (dialog, which) -> {
                    orders.setStates("Done");
                    viewModel.updateOrderShipping(orders);
                    viewModel.getOrderByShipper(App.getInstance().getUser().getId());
                    viewModel.getOrderShippingMutableLiveData().observe(this, new Observer<List<Orders>>() {
                        @Override
                        public void onChanged(List<Orders> shops) {
                            listOrder.clear();
                            listOrder.addAll(shops);
                            orderAdapter.notifyDataSetChanged();
                            Log.i("KMFG", "initViews: "+listOrder.toString());
                        }
                    });
                    alert.cancel();
                });
        // Button "Chỉ đường"
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Giao tới", (dialog, which) -> {
            // Xử lý logic khi nhấn nút "Chỉ đường" ở đây
            handleUserAddress(orders);
            alert.cancel();
        });

        alert.show();

    }

    private void handleUserAddress(Orders orders) {
        Intent intent = new Intent(ShippingOrderActivity.this,MapActivity.class);
        intent.putExtra("user",orders.getUser());
        startActivity(intent);
    }

    @Override
    public void showAlert(String distance, LatLng start, LatLng end, String key) {

    }
}