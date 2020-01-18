package com.android.shoppingapp.Global;


import com.android.shoppingapp.Model.AddressListPojo;
import com.android.shoppingapp.Model.CardListPojo;
import com.android.shoppingapp.Model.CartListPojo;
import com.android.shoppingapp.Model.CategoryPojo;
import com.android.shoppingapp.Model.DefaultPojo;
import com.android.shoppingapp.Model.DescriptionPojo;
import com.android.shoppingapp.Model.HistoryPojo;
import com.android.shoppingapp.Model.LatestCollectionPojo;
import com.android.shoppingapp.Model.LoginPojo;
import com.android.shoppingapp.Model.ProdReviewPojo;
import com.android.shoppingapp.Model.ProductDescPojo;
import com.android.shoppingapp.Model.ProductListPojo;
import com.android.shoppingapp.Model.SliderPojo;
import com.android.shoppingapp.Model.SuccessPojo;
import com.android.shoppingapp.Model.WishListPojo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIInterface {

    @FormUrlEncoded
    @POST("register")
    Call<LoginPojo> loginSignup(@FieldMap Map<String, String> fields);

    @GET("get-slider-image")
    Call<SliderPojo> getSlider();

    @GET("get-category")
    Call<CategoryPojo> getCategory();

    @GET("get-product")
    Call<LatestCollectionPojo> getCollection(@HeaderMap Map<String, String> fields, @Query("type") String type);

    @GET("{url_path}")
    Call<ProductListPojo> getProductList(@Path (value = "url_path", encoded = true) String url_path,@HeaderMap Map<String, String> fields, @QueryMap Map<String, String> options);

    @GET("get-product-sigle")
    Call<ProductDescPojo> getProductDescription(@HeaderMap Map<String, String> fields, @Query("product_id") String type);

    @GET("product-rating?")
    Call<ProdReviewPojo> getProductReview(@HeaderMap Map<String, String> fields, @Query("product_id") String type);

    @GET("related-product")
    Call<ProductListPojo> getRelatedProd(@HeaderMap Map<String, String> fields, @Query("product_id") String type);

    @GET("{url_path}")
    Call<DescriptionPojo> getDescription(@Path (value = "url_path", encoded = true) String url_path,@HeaderMap Map<String, String> fields);

    @GET("add-to-wish-list")
    Call<SuccessPojo> homeLike(@HeaderMap Map<String,String> header, @QueryMap Map<String, String> options);

    @GET("add-to-cart")
    Call<SuccessPojo> addCart(@HeaderMap Map<String,String> header, @QueryMap Map<String,String> map);

    @GET("add-cart-quantity")
    Call<SuccessPojo> addCartQuantity(@HeaderMap Map<String,String> header, @QueryMap Map<String,String> map);

    @GET("get-wishlist-items")
    Call<WishListPojo> getWishList(@HeaderMap Map<String, String> fields);

    @GET("get-cart-items")
    Call<CartListPojo> getCartList(@HeaderMap Map<String, String> fields);

    @GET("my-orders")
    Call<HistoryPojo> getHistory(@HeaderMap Map<String, String> fields);

    @GET("check-out")
    Call<SuccessPojo> setCheckout(@HeaderMap Map<String, String> fields, @QueryMap Map<String,String> map);

    @GET("get-address")
    Call<AddressListPojo> getAddressList(@HeaderMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("{url_path}")
    Call<SuccessPojo> addAddress(@Path (value = "url_path", encoded = true) String url_path,@HeaderMap Map<String, String> header,@FieldMap Map<String, String> fields);


    @GET("get-card")
    Call<CardListPojo> getCardList(@HeaderMap Map<String, String> fields);


    @GET("{url_path}")
    Call<SuccessPojo> deletCardAddress(@Path (value = "url_path", encoded = true) String url_path,@HeaderMap Map<String, String> fields,@QueryMap Map<String,String> map);


    @GET("get-defaults")
    Call<DefaultPojo> getDefault(@HeaderMap Map<String, String> fields, @QueryMap Map<String,String> map);

}