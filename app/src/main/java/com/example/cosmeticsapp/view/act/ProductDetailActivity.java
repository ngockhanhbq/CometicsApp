package com.example.cosmeticsapp.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ActivityProductDetailBinding;
import com.example.cosmeticsapp.entity.Category;
import com.example.cosmeticsapp.entity.ProductReview;
import com.example.cosmeticsapp.entity.Products;
import com.example.cosmeticsapp.entity.Shop;
import com.example.cosmeticsapp.view.adapter.ProductReviewAdapter;
import com.example.cosmeticsapp.view.dialog.FilterOptionDialog;
import com.example.cosmeticsapp.view.dialog.SelectShopDialog;
import com.example.cosmeticsapp.viewmodel.ProductDetailViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends BaseActivity<ActivityProductDetailBinding, ProductDetailViewModel> implements SelectShopDialog.OnItemClick {
    private ProductReviewAdapter productReviewAdapter;
    private List<ProductReview> productReviewList;
    private ArrayList<Shop> listShop;

    @Override
    protected Class<ProductDetailViewModel> getViewModelClass() {
        return ProductDetailViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initViews() {
        listShop = new ArrayList<>();
        viewModel.getListShopServer();
        viewModel.getShopMutableLiveData().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shopList) {
                listShop.clear();
                listShop.addAll(shopList);
            }
        });
        Products data = (Products) getIntent().getSerializableExtra("product_detail");
        binding.tvProductName.setText(data.getName());
        binding.tvProductPrice.setText(data.getPrice()+"Đ");
        binding.tvProductOrigin.setText(data.getOrigin());
        binding.tvProductMaterial.setText(data.getMaterial());
        binding.tvProductDescription.setText(data.getDescription());
        if (!data.getProductReviews().isEmpty()) {
            binding.tvProductVote.setText(data.getProductReviews().get(0).getRating()+"");
            Log.i("KMFG", "initViews:fragment_order ");
            productReviewList = new ArrayList<>();
            productReviewList.addAll(data.getProductReviews());
            productReviewAdapter = new ProductReviewAdapter(productReviewList,this);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            binding.rcvComment.setLayoutManager(manager);
            binding.rcvComment.setAdapter(productReviewAdapter);
        }
        binding.tvProductOrder.setVisibility(View.GONE);
        binding.ivAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvProductOrder.setVisibility(View.VISIBLE);
                binding.tvProductOrder.setText("Đặt đơn : "+data.getPrice()+"Đ");
            }
        });
        binding.tvProductOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key_product", data.getName());
                bundle.putSerializable("shopList", (Serializable) listShop);
                SelectShopDialog selectShopDialog = new SelectShopDialog();
                selectShopDialog.setArguments(bundle);
                selectShopDialog.show(getSupportFragmentManager(), FilterOptionDialog.TAG);
                selectShopDialog.setOnItemClick(ProductDetailActivity.this);
            }
        });
        binding.tvProductComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lnComment.setVisibility(View.VISIBLE);
            }
        });
        binding.ivProductSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ProductReview> reviewList = new ArrayList<>();
                int star = Integer.parseInt(binding.tvRatePromotion.getText().toString());
                if (star>5) star=5;
                reviewList.add(new ProductReview(binding.tvCommentProduct.getText().toString(),star));
                data.setProductReviews(reviewList);
                viewModel.updateProductComment(data);
            }
        });
        viewModel.getIsUpdate().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(App.getInstance(),"Đã gửi",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Glide.with(this).load(data.getImageUrl()).into(binding.ivProduct);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void callBack(String key, Object data) {

    }

    @Override
    public void onItemClick(Shop shop) {
        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra("category", (ArrayList<Category>) shop.getCategories());
        intent.putExtra("shop", shop);
        startActivity(intent);
    }
}
