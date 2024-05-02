package com.example.cosmeticsapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosmeticsapp.App;
import com.example.cosmeticsapp.CommonUtils;
import com.example.cosmeticsapp.R;
import com.example.cosmeticsapp.entity.FavoriteShop;
import com.example.cosmeticsapp.entity.Shop;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopHolder> {
    private List<Shop> listShop;
    private final Context mContext;
    private OnItemClick callBack;

    public void setListShop(List<Shop> listShop) {
        this.listShop = listShop;
        notifyDataSetChanged();
    }

    public List<Shop> getListShop() {
        return listShop;
    }

    public ShopAdapter(List<Shop> listShop, Context context) {
        this.listShop = listShop;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_shop, parent, false);

        return new ShopHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHolder holder, int position) {
        Shop data = listShop.get(position); // lấy vị trí gán data tương ứng cho từng data
        holder.tvShopName.setText(data.getName());
        holder.tvAddress.setText(data.getAddress());
        holder.tvPhone.setText(data.getPhone());
        holder.tvShopStar.setText(data.getStar()+"");
        if (checkFavoriteShop(data)) {
            holder.ivFavoriteShop.setImageResource(R.drawable.ic_favorite_tick);
        } else {
            holder.ivFavoriteShop.setImageResource(R.drawable.ic_favorite_shop);
        }
        holder.shop = data;

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

    @Override
    public int getItemCount() {
        return listShop.size();
    }


    public void setOnItemClick(OnItemClick event) {
        callBack = event;
    }

    public interface OnItemClick {
        void onItemClick(Shop shop);
        void onFavoriteShopClick(Shop shop);
    }

    public class ShopHolder extends RecyclerView.ViewHolder {
        private TextView tvShopName;
        private TextView tvShopStar;
        private TextView tvAddress;
        private TextView tvPhone;
        private ImageView ivFavoriteShop;
        private Shop shop;
        public ShopHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            tvAddress= itemView.findViewById(R.id.tv_address_shop);
            tvShopStar= itemView.findViewById(R.id.tv_star_shop);
            tvPhone= itemView.findViewById(R.id.tv_phone_shop);
            ivFavoriteShop= itemView.findViewById(R.id.iv_favorite_shop);
            ivFavoriteShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onFavoriteShopClick(shop);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(shop);
                }

            });
        }
    }
}

