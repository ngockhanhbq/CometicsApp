package com.example.cosmeticsapp.api;

import com.example.cosmeticsapp.entity.Banner;
import com.example.cosmeticsapp.entity.Category;
import com.example.cosmeticsapp.entity.Orders;
import com.example.cosmeticsapp.entity.Products;
import com.example.cosmeticsapp.entity.Promotion;
import com.example.cosmeticsapp.entity.ResponseDTO;
import com.example.cosmeticsapp.entity.Shop;
import com.example.cosmeticsapp.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    @GET("/user/list")
    Call<ResponseDTO<List<User>>> getListUser();

    @FormUrlEncoded
    @POST("/user/get")
    Call<ResponseDTO<User>> getUserNameAndPassword(@Field("username") String username,
                                        @Field("password") String password);

    @GET("/shop/list")
    Call<ResponseDTO<List<Shop>>> getListShop();
    @GET("/promotion/list")
    Call<ResponseDTO<List<Promotion>>> getListPromotion();
    @GET("/orders/list")
    Call<ResponseDTO<List<Orders>>> getListOrders();
    @GET("/categories/list")
    Call<ResponseDTO<List<Category>>> getListCategory();
    @GET("/products/list")
    Call<ResponseDTO<List<Products>>> getListProducts();
    @GET("/banner/list")
    Call<ResponseDTO<List<Banner>>> getListBanner();
    @POST("/orders/updateuser")
    Call<ResponseDTO<Void>> orderUpdateUser(@Body Orders orders);

    @GET("/orders/ship")
    Call<ResponseDTO<List<Orders>>> searchOrderByShipper(@Query("id") int id);

    @GET("/orders/search")
    Call<ResponseDTO<List<Orders>>> searchOrderByUser(@Query("id") int id);

    @GET("/{id}")
    Call<ResponseDTO<User>> getUserById(@Path("id") int id);
    @FormUrlEncoded
    @POST("/user/edit")
    Call<ResponseDTO<Void>> editUser(  @Field("id") int id,
                                       @Field("age") int age,//homeAddress
                                       @Field("name") String name,
                                       @Field("phonenumber") String phonenumber,
                                       @Field("homeAddress") String homeAddress);
    @POST("/promotion/edit")
    Call<ResponseDTO<Void>> updatePromotion(@Body Promotion promotion);

    @POST("/products/edit")
    Call<ResponseDTO<Void>> updateProducts(@Body Products products);

    @POST("/orders/update")
    Call<ResponseDTO<Void>> orderUpdate(@Body Orders orders);


    @FormUrlEncoded
    @POST("/user/new")
    Call<ResponseDTO<Void>> createNewUser(//homeAddress
                                          @Field("name") String name,
                                          @Field("age") int age,
                                          @Field("username") String username,
                                          @Field("password") String password,
                                          @Field("phonenumber") String phonenumber,
                                          @Field("homeAddress") String homeAddress,
                                          @Field("roles") List<String> roles);

    @POST("/orders/customer")
    Call<ResponseDTO<Void>> postOrderData(@Body Orders orders);

    @FormUrlEncoded
    @POST("/user/change_password")
    Call<ResponseDTO<Void>> changePassword(@Field("id") int id,
                                           @Field("password") String password);


}