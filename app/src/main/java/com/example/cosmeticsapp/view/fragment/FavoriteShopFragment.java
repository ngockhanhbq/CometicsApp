package com.example.cosmeticsapp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.database.SQLiteHelper;
import com.example.cosmeticsapp.databinding.FragmentFavoriteShopBinding;
import com.example.cosmeticsapp.entity.Category;
import com.example.cosmeticsapp.entity.Shop;
import com.example.cosmeticsapp.view.act.ShopDetailActivity;
import com.example.cosmeticsapp.view.adapter.ShopAdapter;
import com.example.cosmeticsapp.viewmodel.FavoriteShopViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteShopFragment extends BaseFragment<FragmentFavoriteShopBinding, FavoriteShopViewModel> implements ShopAdapter.OnItemClick {
    public static final String KEY_SHOW_SHOP_DETAIL = "KEY_SHOW_SHOP_DETAIL";
    private ShopAdapter shopAdapter;
    private List<Shop> listShop;

    @Override
    protected Class<FavoriteShopViewModel> getViewModelClass() {
        return FavoriteShopViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favorite_shop;
    }

    @Override
    protected void initViews() {
        listShop = new ArrayList<>();
        mViewModel.getListShopServer();
        mViewModel.getShopMutableLiveData().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                listShop.clear();
                listShop.addAll(shops);
                shopAdapter.notifyDataSetChanged();
                Log.i("KMFG", "initViews: " + listShop.toString());
            }
        });
        //  setCallBack((OnActionCallBack) getActivity());
        shopAdapter = new ShopAdapter(listShop, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvFavoriteShop.setLayoutManager(manager);
        binding.rcvFavoriteShop.setAdapter(shopAdapter);
        shopAdapter.setOnItemClick(this);
    }

    @Override
    public void onItemClick(Shop shop) {
        Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
        intent.putExtra("category", (ArrayList<Category>) shop.getCategories());
        intent.putExtra("shop", shop);
        getActivity().startActivity(intent);
    }

    @Override
    public void onFavoriteShopClick(Shop shop) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle("Xóa shop khỏi danh sách yêu thích");
        alert.setMessage("Danh sách yêu thích - " + shop.getName());
        alert.setButton(DialogInterface.BUTTON_POSITIVE,
                "Hoàn thành", (dialog, which) -> deleteShopFavorite(shop));
        alert.show();
    }

    private void deleteShopFavorite(Shop shop) {
        SQLiteHelper db = new SQLiteHelper(App.getInstance());
        db.deleteShopWithUser(App.getInstance().getUser().getId(),shop.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        List<Shop> shopList = mViewModel.getShopMutableLiveData().getValue();
        if (shopList != null) {
            shopAdapter.setListShop(shopList);
        }
        shopAdapter.setListShop(listShop);
        binding.lnShopFavorite.setVisibility(View.VISIBLE);
    }

}


