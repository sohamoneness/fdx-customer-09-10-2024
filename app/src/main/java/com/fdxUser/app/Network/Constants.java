package com.fdxUser.app.Network;

import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CouponModels.CouponListModel;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.DashboardModels.BannerModel;
import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.Models.DashboardModels.ReferralSettingsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemCategoryModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestReviewModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestWiseCouponModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static String BASE_URL = "http://13.127.50.16/api/";
    //public static String BASE_URL = "http://13.127.50.16/api/";
    //public static String BASE_URL = "https://demo91.co.in/dev/fdx/public/api/";
    //public static String IMAGE_BASE_URL = "http://demo91.co.in/dev/fdx/public/banners/";
    //public static String PRF_IMAGE_URL = "http://demo91.co.in/dev/fdx/public/";
    public static String IMAGE_BASE_URL = "http://13.127.50.16/banners/";
    //public static String PRF_IMAGE_URL = "http://35.154.82.230/public/";
    public static String PRF_IMAGE_URL = "http://13.127.50.16/";
 //public static String PRF_IMAGE_URL = "http://13.127.142.32/";



    public static String token = "";
    public static int TAG = 0;
    public static double cartPrice = 0;
    public static boolean isRevTabSelected = false;
    public static boolean addressSelected = false;
    public static boolean isDefault = false;
    public static boolean isCartEmpty = false;
    public static String restIdFromCart = "";
    public static String restName = "";
    public static String restRating = "";
    public static String restTotalReviews = "";
    public static String restIdForCartUpdate = "";
    public static String isCancelable = "0";
    public static String searchLat = "0";
    public static String searchLon = "0";
    public static String couponAmount = "0";
    public static String couponCode = "0";
    public static int priceWithAddOn = 0;
    public static int isAdded = 0;
    public static int addOnCount = 0;
    public static int isCouponSelected = 0;
    public static boolean isFromHome = false;
    public static boolean isFromAddressSave = false;
    public static boolean isFromHomeToHistory = false;
    public static boolean isFromHomeToOrderStat = false;
    public static boolean isFromHomeToSearch = false;
    public static boolean isFromRestToHist = false;
    public static boolean isTakingOrder = false;
    public static boolean isNewlyOpen = false;
    public static boolean isFromGoogle = false;

    public static String addOnId = "";
    public static String addOnName = "";
    public static String addOnPrice = "";
    public static String addOnQty = "";

    public static String addOnId2 = "";
    public static String addOnName2 = "";
    public static String addOnPrice2 = "";
    public static String addOnQty2 = "";
    public static String addOnCartId = "";
    public static String addOnForId = "";
    public static String addOnForName = "";
    public static String addOnForDescription = "";
    public static String addOnForPrice = "";
    public static String addOnForQty = "";

    public static String orderCount = "";

    public static String locId = "";
    public static String version_code = "";


    public static String offerId = "";




    public static List<RestReviewModel> revList = new ArrayList<>();
    public static List<RestWiseCouponModel> restWiseCoupons = new ArrayList<>();
    public static List<ItemCategoryModel> cateList = new ArrayList<>();
    public static List<CustomCartModel> custCartList = new ArrayList<>();
    public static List<ReferralSettingsModel> referralList = new ArrayList<>();
    //public static RestaurantModel restaurantModel = new RestaurantModel();
    public static RestaurantModel restaurantModel;


    // Prefs Key

    public static String USER_ID = "USER_ID";
    public static String USER_NAME = "USER_NAME";
    public static String USER_MOBILE = "USER_MOBILE";
    public static String USER_EMAIL = "USER_EMAIL";
    public static String USER_DOB = "USER_DOB";
    public static String USER_IMG = "USER_IMG";
    public static String USER_GENDER = "USER_GENDER";
    public static String USER_ADDRESS = "USER_ADDRESS";
    public static String USER_REF_ID = "USER_REF_ID";
    public static String USER_WALLET_ID = "USER_WALLET_ID";
    public static String USER_CITY = "USER_CITY";
    public static String TOTAL_PRICE = "TOTAL_PRICE";
    public static String LOGIN_TOKEN = "LOGIN_TOKEN";
    public static String REST_ID = "REST_ID";
    public static String REST_NAME = "REST_NAME";
    public static String REST_IMG = "REST_IMG";
    public static String DELBOY_NAME = "DELBOY_NAME";
    public static String CART_ID = "CART_ID";
    public static String RestId_FromCart = "restId_FromCart";
    public static String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static String CUISINE_ID = "CUISINE_ID";
    public static String CUISINE_NAME = "CUISINE_NAME";
    public static String ORDER_REF_ID = "ORDER_REF_ID";
    public static String TOTAL_ITEM_PRICE = "TOTAL_ITEM_PRICE";
    public static String CUSTOM_CART_DATA = "CUSTOM_CART_DATA";
    public static String DISCOUNT = "DISCOUNT";
    public static String DISCOUNT_CODE = "DISCOUNT_CODE";
    public static String CART_REST_ID = "CART_REST_ID";
    public static String NEW_USER = "NEW_USER";
    public static String USER_CANCEL_AMOUNT = "USER_CANCEL_AMOUNT";

    //Dialog control
    public static int isDialogOn = 0;

    //Path flow tracker
    public static String isFor = "";

    //Order model
    public static CartItemsResponseModel cartItemsResponseModel = new CartItemsResponseModel();
    public static CouponListModel couponListModel = new CouponListModel();
    public static AddressListModel addressListModel = new AddressListModel();
    public static String cartID = "";

    //Search Page Data
    public static List<RestaurantModel> nearRestList = new ArrayList<>();
    public static List<CuisinesModel> catList = new ArrayList<>();

    public static String home_address = "";

    public static List<RestaurantModel> nearbyRestList = new ArrayList<>();
    public static List<RestaurantModel> pocketFriendlyList = new ArrayList<>();
    public static List<RestaurantModel> popularRestList = new ArrayList<>();
    public static List<RestaurantModel> offerForYouList = new ArrayList<>();
    public static List<RestaurantModel> allTimeFavList = new ArrayList<>();
    public static List<RestaurantModel> cakeList = new ArrayList<>();
    public static List<RestaurantModel> preOrderList = new ArrayList<>();
    public static List<BannerModel> bannerList = new ArrayList<>();
    public static List<CuisinesModel> cuisinesList = new ArrayList<>();

    public static String notes = "";
    public static String delivery_charge = "0";
    public static String tip = "0";
    public static String offer_discount = "0";
    public static String total_order_count = "0";
    public static String isNewUser = "0";

    public static String notification_type = "0";

    public static String restaurant_payment_method = "3";
    public static String delivery_mode = "1";
    public static String order_delivery_type = "1";
    public static int add_on_q = 0;

    public static double lat = 0.0;
    public static double lng = 0.0;
    public static boolean fromAddressSearch = false;


    public static String schedule_date = "";
    public static String schedule_time = "";
}
