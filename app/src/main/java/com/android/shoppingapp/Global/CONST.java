package com.android.shoppingapp.Global;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by senthil on 23-Feb-18.
 */

public class CONST {
//    http://18.217.220.83/abc.com/api/check_postal_code/{postal_code}
    public static String BASE_URL = "http://18.221.234.140/ecommerce/api/";
    public static String APIKEY = "AIzaSyDlwxHz0Y2zatWPImYziSlEdmf0g-wgJWo";
    public static String GEOFIRE = "https://foodieuser-212211.firebaseio.com/";

    public static String FIREBASE_URL = "https://ecommerce-ace7c.firebaseio.com/Cart/";
    public static String STRIPE_KEY = "pk_test_SX2qwU595p4aBsbX4uCAmSj7";

    public static String login = "Login";

    public static String productIdStr = "";
    public static String categoryIdStr = "";


    public static class Params{
        public static final String CART = "Cart";
        public static final String CartValue = "CartValue";
        public static final String TYPE = "Type";
        public static final String CATEGORY = "Category";
        public static final String DESC_TYPE = "DescriptionType";
        public static final String PROFILE = "Profile";
        public static String UserName = "username";
        public static String Password = "password";
        public static String DeviceToken = "device_token";
        public static String Email = "email";
        public static String Type = "type";

        public static String PROF_NAME = "name";
        public static String PROF_STREET = "street_name";
        public static String PROF_CITY = "city";
        public static String PROF_STATE = "state";
        public static String PROF_PINCODE = "pincode";
        public static String PROF_ADDR_LINE = "address_line";

        //Card Details
        public static String CARD_NUM = "card_number";
        public static String CARD_CVV = "cvv";
        public static String CARD_MONTH = "exp_month";
        public static String CARD_YEAR = "exp_year";
        public static String CARD_ID = "card_id";
        public static String ADDRESS_ID = "address_id";
        //Payment Mode
        public static String PAYMENT_MODE = "payment_mode";

    }

    public static class Keywords{

        //these keywords used in login page to make different visibility of linear inside card
        public static final String sign_in = "signIn";//To display Signin page
        public static final String sign_up = "signUp";//To display Register page
        public static final String verification = "verification";//To show otp verification
        public static final String forgot_pswd = "forgotPswd";// To show forgot password geting phone number screen
        public static final String new_pswd = "newPswd";//To show reset password page


    }
}
