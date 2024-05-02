package com.example.cosmeticsapp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.CommonUtils;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.database.SQLiteHelper;
import com.example.cosmeticsapp.databinding.FragmentHomeShoeBinding;
import com.example.cosmeticsapp.entity.Banner;
import com.example.cosmeticsapp.entity.Category;
import com.example.cosmeticsapp.entity.FavoriteShop;
import com.example.cosmeticsapp.entity.Shop;
import com.example.cosmeticsapp.view.act.CartUserActivity;
import com.example.cosmeticsapp.view.act.ShopDetailActivity;
import com.example.cosmeticsapp.view.adapter.BannerAdapter;
import com.example.cosmeticsapp.view.adapter.ShopAdapter;
import com.example.cosmeticsapp.viewmodel.HomeShoeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeShoeFragment extends BaseFragment<FragmentHomeShoeBinding, HomeShoeViewModel> implements ShopAdapter.OnItemClick {
    private ShopAdapter shopAdapter;
    private BannerAdapter bannerAdapter;
    private List<Shop> listShop;
    private List<Banner> listBanner;
    private SQLiteHelper db;
    @Override
    protected Class<HomeShoeViewModel> getViewModelClass() {
        return HomeShoeViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_shoe;
    }

    @Override
    protected void initViews() {
        Log.i("KMFG", "initViews:fragment_order ");
        db = new SQLiteHelper(App.getInstance());
        listShop = new ArrayList<>();
        listBanner = new ArrayList<>();
        mViewModel.getListBanner();
        mViewModel.getListShopServer();
        mViewModel.getBannerMutableLiveData().observe(this, new Observer<List<Banner>>() {
            @Override
            public void onChanged(List<Banner> categories) {
                listBanner.clear();
                listBanner.addAll(categories);
                bannerAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.getShopMutableLiveData().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> products) {
                listShop.clear();
                listShop.addAll(products);
                shopAdapter.notifyDataSetChanged();
            }
        });
        bannerAdapter = new BannerAdapter(listBanner,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),  RecyclerView.HORIZONTAL,false);
        binding.rcvBannerShoe.setLayoutManager(manager);
        binding.rcvBannerShoe.setAdapter(bannerAdapter);
       // bannerAdapter.setOnItemClick(this);
//        shopMasterAdapter = new ShopAdapter(listMaster,getContext());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        binding.rcvShopMaster.setLayoutManager(layoutManager);
//        binding.rcvShopMaster.setAdapter(shopMasterAdapter);
//        shopMasterAdapter.setOnItemClick(this);
        shopAdapter = new ShopAdapter(listShop,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rcvShopBranch.setLayoutManager(linearLayoutManager);
        binding.rcvShopBranch.setAdapter(shopAdapter);
        shopAdapter.setOnItemClick(this);
        binding.topBarAction.ivCartUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartUserActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getListBanner();
        mViewModel.getListShopServer();
        mViewModel.getBannerMutableLiveData().observe(this, new Observer<List<Banner>>() {
            @Override
            public void onChanged(List<Banner> categories) {
                listBanner.clear();
                listBanner.addAll(categories);
                bannerAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.getShopMutableLiveData().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> products) {
                listShop.clear();
                listShop.addAll(products);
                shopAdapter.notifyDataSetChanged();
            }
        });
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
        if (checkFavoriteShop(shop)) {
            Toast.makeText(App.getInstance(),"Shop đã có trong danh sách yêu thích",Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
            alert.setTitle("Thêm shop vào danh sách yêu thích");
            alert.setMessage("Danh sách yêu thích + " + shop.getName());
            alert.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Hoàn thành", (dialog, which) -> addShopFavorite(shop));
            alert.show();
        }
    }

    private boolean checkFavoriteShop(Shop shop) {
        boolean isFavorite = false;
        for (FavoriteShop favoriteShop : CommonUtils.getInstance().getListFavoriteWithUserSession(App.getInstance().getUser().getId())) {
            if (shop.getId() == favoriteShop.getShopId()) {
                isFavorite = true;
            }
        }
        return isFavorite;
    }

    private void addShopFavorite(Shop shop) {
        db.addShop(new FavoriteShop(App.getInstance().getUser().getId(),shop.getId()));
        Log.i("KMFG", "addShopFavorite: "+ CommonUtils.getInstance()
                .getListFavoriteWithUserSession(App.getInstance().getUser().getId()));
    }
}
