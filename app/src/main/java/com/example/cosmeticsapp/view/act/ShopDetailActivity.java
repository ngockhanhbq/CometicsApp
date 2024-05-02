package com.example.cosmeticsapp.view.act;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.CartSession;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.databinding.ActivityShopDetailBinding;
import com.example.cosmeticsapp.entity.Category;
import com.example.cosmeticsapp.entity.Products;
import com.example.cosmeticsapp.entity.Shop;
import com.example.cosmeticsapp.view.adapter.ShopDetailAdapter;
import com.example.cosmeticsapp.viewmodel.ShopDetailViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopDetailActivity extends BaseActivity<ActivityShopDetailBinding, ShopDetailViewModel> implements ShopDetailAdapter.OnItemClick {
    public static final String DATA_ORDER_FRAGMENT = "DATA_ORDER_FRAGMENT";
    public static final String LIST_PRODUCT = "LIST_PRODUCT";
    public static final String SHOP_NAME = "SHOP_NAME";
    private ShopDetailAdapter shopDetailAdapter;
    private ArrayList<Object> items;
    private List<Category> categoryList = new ArrayList<>();
    private ArrayList<Products> productsList = new ArrayList<>();
    private HashMap<Integer, Integer> productQuantityMap = new HashMap<>();
    private Shop shop;

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<Object> getItems() {
        return items;
    }

    public void setItems(ArrayList<Object> items) {
        this.items = items;
    }

    @Override
    protected Class<ShopDetailViewModel> getViewModelClass() {
        return ShopDetailViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void initViews() {
        List<Category> data = (List<Category>) getIntent().getSerializableExtra("category");
        shop = (Shop) getIntent().getSerializableExtra("shop");
        categoryList.addAll(data);
        if (!productsList.isEmpty()) {
            binding.lnShoppingCart.setVisibility(View.VISIBLE);
            binding.tvCartPrice.setText(calculateTotalPrice()+"");
        } else {
            binding.lnShoppingCart.setVisibility(View.GONE);
        }

        List<Object> items = createItems();
        shopDetailAdapter = new ShopDetailAdapter(items,this);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.rcvShopDetail.setLayoutManager(manager);
        binding.rcvShopDetail.setAdapter(shopDetailAdapter);
        shopDetailAdapter.setCallBack(this);
        Log.i("KMFG", "initViews: ShopDetail "+items.toString());
        binding.tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopDetailActivity.this,CartActivity.class);
                intent.putExtra(SHOP_NAME,shop);
                intent.putExtra(DATA_ORDER_FRAGMENT,productQuantityMap);
                intent.putExtra(LIST_PRODUCT,productsList);
                startActivity(intent);
               // callBack.callBack(KEY_SHOW_ORDER_FRAGMENT, productQuantityMap);
            }
        });
        binding.ivCartUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopDetailActivity.this,CartUserActivity.class);
                intent.putExtra(SHOP_NAME,shop);
                intent.putExtra(DATA_ORDER_FRAGMENT,productQuantityMap);
                intent.putExtra(LIST_PRODUCT,productsList);
                startActivity(intent);
                App.getInstance().getStorage().getCartSessionList().add(new CartSession(shop,productQuantityMap,productsList));
                finish();
            }
        });
    }

    private List<Object> createItems() {
        items = new ArrayList<>();

        // Thêm danh mục
        for (int i = 0; i < categoryList.size(); i++) {
            items.add(categoryList.get(i));
            for (int j = 0; j < categoryList.get(i).getProducts().size(); j++) {
               items.add(categoryList.get(i).getProducts().get(j));
            }
        }

        return items;
    }

    @Override
    public void onItemClick(Products products) {
        int productId = products.getId();

        productsList.add(products);
        if (!productsList.isEmpty()) {
            binding.lnShoppingCart.setVisibility(View.VISIBLE);
        }
        // Kiểm tra xem sản phẩm đã có trong HashMap hay chưa
        if (productQuantityMap.containsKey(productId)) {
            // Sản phẩm đã tồn tại, tăng số lượng lên 1
            int currentQuantity = productQuantityMap.get(productId);
            productQuantityMap.put(productId, currentQuantity + 1);
        } else {
            // Sản phẩm chưa tồn tại, thêm vào HashMap với số lượng ban đầu là 1
            productQuantityMap.put(productId, 1);
        }
        binding.tvCartPrice.setText(calculateTotalPrice()+"");
        Log.i("KMFG", "onItemClick: "+productsList.toString());
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;

        // Duyệt qua các sản phẩm trong HashMap
        for (Map.Entry<Integer, Integer> entry : productQuantityMap.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            // Tìm sản phẩm tương ứng trong danh sách sản phẩm
            for (Products product : productsList) {
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
    protected void onResume() {
        super.onResume();
        if (!App.getInstance().isOrder()) {
            productsList.clear();
            productQuantityMap.clear();
            binding.lnShoppingCart.setVisibility(View.GONE);
        }
    }

    @Override
    public void callBack(String key, Object data) {

    }
}
