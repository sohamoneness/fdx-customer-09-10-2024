package com.fdxUser.app.Network;

import com.fdxUser.app.Models.AddressModels.AddAddressRequest;
import com.fdxUser.app.Models.AddressModels.AddAddressResponseModel;
import com.fdxUser.app.Models.AddressModels.AddressResponseModel;
import com.fdxUser.app.Models.CancelOrderModels.CancelOrderRequestModel;
import com.fdxUser.app.Models.CancelOrderModels.CancelOrderResponseModel;
import com.fdxUser.app.Models.CartModels.AddToCartRequest;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CartModels.CartResponseModel;
import com.fdxUser.app.Models.CartModels.ClearCartResponseModel;
import com.fdxUser.app.Models.CartModels.DeleteCartItemResponseModel;
import com.fdxUser.app.Models.CartModels.UpdateCartRequest;
import com.fdxUser.app.Models.CheckGeofenceModels.CheckGeoResponseModel;
import com.fdxUser.app.Models.CheckGeofenceModels.GeoCheckRequestModel;
import com.fdxUser.app.Models.ClaimGiftModels.ClaimGiftRequestModel;
import com.fdxUser.app.Models.ClaimGiftModels.ClaimGiftResponseModel;
import com.fdxUser.app.Models.CouponModels.CheckCouponRequestModel;
import com.fdxUser.app.Models.CouponModels.CheckCouponResponseModel;
import com.fdxUser.app.Models.CouponModels.CouponResponseModel;
import com.fdxUser.app.Models.DashboardModels.AllCuisinesResponseModel;
import com.fdxUser.app.Models.DashboardModels.HomeDataResponseModel;
import com.fdxUser.app.Models.DeliveryChargeModels.DelChargeRequestModel;
import com.fdxUser.app.Models.DeliveryChargeModels.DelChargeResponseModel;
import com.fdxUser.app.Models.FavouriteModels.AddFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.AddFavResponseModel;
import com.fdxUser.app.Models.FavouriteModels.FavouriteResponseModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavResponseModel;
import com.fdxUser.app.Models.FeedbackModels.FeedbackRequestModel;
import com.fdxUser.app.Models.FeedbackModels.FeedbackResponseModel;
import com.fdxUser.app.Models.ForgotPassModels.CheckPhoneRequestModel;
import com.fdxUser.app.Models.ForgotPassModels.CheckPhoneResponseModel;
import com.fdxUser.app.Models.ForgotPassModels.ForgetPassRequestModel;
import com.fdxUser.app.Models.ForgotPassModels.ForgetPassResponseModel;
import com.fdxUser.app.Models.GeoFencingModels.GeoFenceResponseModel;
import com.fdxUser.app.Models.LoginModels.LoginModel;
import com.fdxUser.app.Models.LoginModels.LoginRequest;
import com.fdxUser.app.Models.NotificationModels.NotificationResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDateFilterRequestModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDetailsResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderInvoiceResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.RepeatOrderRequestModel;
import com.fdxUser.app.Models.OrderSummeryModels.RepeatOrderResponseModel;
import com.fdxUser.app.Models.PlaceOrderModels.PlaceOrderRequest;
import com.fdxUser.app.Models.PlaceOrderModels.PlaceOrderResponseModel;
import com.fdxUser.app.Models.ProfileModels.ProfileImageUploadResponseModel;
import com.fdxUser.app.Models.ProfileModels.ProfileUpdateRequestModel;
import com.fdxUser.app.Models.ProfileModels.ProfileUpdateResponseModel;
import com.fdxUser.app.Models.RegisterModels.RegisterModel;
import com.fdxUser.app.Models.RegisterModels.RegisterRequest;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsRequestModel;
import com.fdxUser.app.Models.RestaurantModels.CuisinesWiseRestResponseModel;
import com.fdxUser.app.Models.RestaurantModels.ResponseDataModel;
import com.fdxUser.app.Models.RestaurantModels.SearchItemResponseModel;
import com.fdxUser.app.Models.RestaurantModels.SearchItemsRequestModel;
import com.fdxUser.app.Models.SearchModels.SearchRequestModel;
import com.fdxUser.app.Models.SearchModels.SearchResponseModel;
import com.fdxUser.app.Models.SuggestionModels.SuggestionRequestModel;
import com.fdxUser.app.Models.SuggestionModels.SuggestionResponseModel;
import com.fdxUser.app.Models.TokenUpdateModels.TokenUpdateRequestModel;
import com.fdxUser.app.Models.TokenUpdateModels.TokenUpdateResponseModel;
import com.fdxUser.app.Models.UserDetailsModel;
import com.fdxUser.app.Models.WalletModels.WalletCreditRequestModel;
import com.fdxUser.app.Models.WalletModels.WalletCreditResponseModel;
import com.fdxUser.app.Models.WalletModels.WalletListResponseModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    /*@FormUrlEncoded
    @POST("login")
    Call<LoginModel> loginWithEmail(@Field("email") String email,
                                      @Field("password") String password);*/


    @POST("login")
    Call<LoginModel> loginWithEmail(@Body LoginRequest loginRequest);

    @POST("register")
    Call<RegisterModel> registerNewUser(@Body RegisterRequest registerRequest);

    @GET("user")
    Call<UserDetailsModel> getUserDetails();

    @GET("home")
    Call<HomeDataResponseModel> getDashboardData(@Query("lat") String lat, @Query("lng") String lng);

    @GET("restaurant/list")
    Call<ResponseDataModel> getRestaurants(@Query("lat") String lat, @Query("lng") String lng, @Query("location_id") String location_id);

   // @GET("restaurant/details/{id}")
   // Call<RestaurantDetailsModel> getRestaurantDetails(@Path("id") String id);

    @POST("restaurant/single")
    Call<RestaurantDetailsModel> getRestaurantDetails(@Body RestaurantDetailsRequestModel restaurantDetailsRequestModel,
                                                      @Query("lat") String lat,
                                                      @Query("lng") String lng,
                                                      @Query("is_veg") String is_veg);

    @POST("cart/add")
    Call<CartResponseModel> addItemToCart(@Body AddToCartRequest addToCartRequest);

    @POST("cart/update")
    Call<CartResponseModel> updateCartItem(@Body UpdateCartRequest updateCartRequest);

    @GET("cart/delete/{id}")
    Call<DeleteCartItemResponseModel> deleteCartItem(@Path("id") String id);

    @GET("cart/list/{id}")
    Call<CartItemsResponseModel> getCartList(@Path("id") String id);

    @GET("address/list/{id}")
    Call<AddressResponseModel> getAddressList(@Path("id") String id);

    @GET("address/delete/{id}")
    Call<AddAddressResponseModel> deleteAddress(@Path("id") String id);

    @POST("address/add")
    Call<AddAddressResponseModel> addAddress(@Body AddAddressRequest addAddressRequest);

    @POST("address/update")
    Call<AddAddressResponseModel> updateAddress(@Body AddAddressRequest addAddressRequest);

    @POST("order/create")
    Call<PlaceOrderResponseModel> placeOrder(@Body PlaceOrderRequest placeOrderRequest);

    @GET("order/list/{id}")
    Call<OrderHistResponseModel> getOrderList(@Path("id") String id);

    @GET("order/details/{id}")
    Call<OrderDetailsResponseModel> getOrderDetails(@Path("id") String id);

    @GET("cart/clear/{id}")
    Call<ClearCartResponseModel> clearCartData(@Path("id") String id);

    @GET("cuisine/list")
    Call<AllCuisinesResponseModel> getAllCuisines();

    @GET("cuisine/restaurant/list/{id}")
    Call<CuisinesWiseRestResponseModel> getCuisinesWiseRestaurants(@Path("id") String id,
                                                                   @Query("lat") String lat,
                                                                   @Query("lng") String lng,
                                                                   @Query("location_id") String location_id);

    @POST("search")
    Call<SearchResponseModel> getSearchData(@Body SearchRequestModel searchRequestModel);

    @POST("restaurant/review/add")
    Call<FeedbackResponseModel> getFeedbackFromUser(@Body FeedbackRequestModel feedbackRequestModel);

    @POST("user/device/update")
    Call<TokenUpdateResponseModel> updateDeviceToken(@Body TokenUpdateRequestModel tokenUpdateRequestModel);

    @GET("user/notification/list/{id}")
    Call<NotificationResponseModel> getNotifications(@Path("id") String id);

    @POST("suggestion/add")
    Call<SuggestionResponseModel> submitSuggestion(@Body SuggestionRequestModel suggestionRequestModel);

    @POST("user/order/cancel")
    Call<CancelOrderResponseModel> cancelOrder(@Body CancelOrderRequestModel cancelOrderRequestModel);

    @GET("user/favourite/restaurant/{id}")
    Call<FavouriteResponseModel> getFavRestaurants(@Path("id") String id, @Query("lat") String lat, @Query("lng") String lng);

    @POST("restaurant/favourite/add")
    Call<AddFavResponseModel> addFav(@Body AddFavRequestModel addFavRequestModel);

    @POST("restaurant/favourite/remove")
    Call<RemoveFavResponseModel> removeFav(@Body RemoveFavRequestModel removeFavRequestModel);

    @POST("wallet/credit")
    Call<WalletCreditResponseModel> walletCredit(@Body WalletCreditRequestModel walletCreditRequestModel);

    @GET("wallet/user/{id}")
    Call<WalletListResponseModel> userWallets(@Path("id") String id);

    @GET("restaurant/coupons/{id}/{userId}")
    Call<CouponResponseModel> getCouponList(@Path("id") String id,@Path("userId") String userId);

    @POST("user/forgot/password/check")
    Call<CheckPhoneResponseModel> checkNumber(@Body CheckPhoneRequestModel checkPhoneRequestModel);

    @POST("user/forgot/password/update")
    Call<ForgetPassResponseModel> updateForgotPassword(@Body ForgetPassRequestModel forgetPassRequestModel);

    @POST("delivery/charge")
    Call<DelChargeResponseModel> getDeliveryCharge(@Query("coupon_code") String coupon_code,
                                                   @Body DelChargeRequestModel delChargeRequestModel);

    @POST("profile/update")
    Call<ProfileUpdateResponseModel> updateUserProfile(@Body ProfileUpdateRequestModel profileUpdateRequestModel);

    @Multipart
    @POST("user.php")
    Call<ProfileImageUploadResponseModel> uploadRestaurantProfileImage(@Part MultipartBody.Part file);

    @POST("repeat-order")
    Call<RepeatOrderResponseModel> repeatOrder(@Body RepeatOrderRequestModel repeatOrderRequestModel);

    @POST("search-item")
    Call<SearchItemResponseModel> searchItem(@Body SearchItemsRequestModel searchItemsRequestModel);

    @GET("user/current/orders/{id}")
    Call<OrderHistResponseModel> getCurrentOrders(@Path("id") String id);

    @GET("all/coupons/{id}/{userId}")
    Call<CouponResponseModel> getAllCoupons(@Path("id") String id,@Path("userId") String userId);

    @POST("claim-gift-requests")
    Call<ClaimGiftResponseModel> claimGifts(@Body ClaimGiftRequestModel claimGiftRequestModel);

    @POST("check-active-coupon")
    Call<CheckCouponResponseModel> checkActiveCoupon(@Body CheckCouponRequestModel checkCouponRequestModel);

    @GET("home-screen-data-user")
    Call<HomeDataResponseModel> getDashboardDataForUser(@Query("lat") String lat, @Query("lng") String lng,
                                                        @Query("user_id") String user_id,
                                                        @Query("location_id") String location_id);

    @GET("order/invoice/{orderId}")
    Call<OrderInvoiceResponseModel> getOrderInvoice(@Path("orderId") String orderId);

    @POST("order-date-filter")
    Call<OrderHistResponseModel> orderDateFilter(@Body OrderDateFilterRequestModel orderDateFilterRequestModel);

    @GET("fetch-locations")
    Call<GeoFenceResponseModel> getGeoFenceLocations();

    @POST("check-location-in-geo-fence")
    Call<CheckGeoResponseModel> checkLocation(@Body GeoCheckRequestModel geoCheckRequestModel);
}
