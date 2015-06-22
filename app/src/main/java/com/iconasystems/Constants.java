package com.iconasystems;

/**
 * Created by Raymo on 6/9/2015.
 */
public class Constants {
    public Constants() {
    }

    public static class NameConstants {
        public static final String TAG_SUCCESS = "success";
        public static final String TAG_ITEMS = "items";
        public static final String TAG_ITEM_NAME = "item_name";
        public static final String TAG_ITEM_IMAGE = "item_image";
        public static final String TAG_ITEM_ID = "item_id";
        public static final String TAG_SHOP_NAME = "shop_name";
        public static final String TAG_PRICE = "price";
        public static final String TAG_DESCRIPTION = "description";
        public static final String TAG_SHOP = "shop";
        public static final String TAG_CATEGORY = "category";
        public static final String TAG_CATEGORIES = "categories";
        public static final String TAG_CAT_NAME = "category_name";
        public static final String TAG_CAT_IMAGE = "category_image";
        public static final String TAG_CAT_ID = "category_id";
        public static final String TAG_SHOP_ID = "shop_id";
        public static final String TAG_SUB_CAT_ID = "sub_cat_id";
        public static final String TAG_DETAILS = "item_details";
        public static final String TAG_ITEM_PRICE = "item_price";
        public static final String TAG_DATE_ADDED = "date_added";
        public static final String TAG_USER_ID = "user_id";
        public static final String INDEX = "ArrayListIndex";

        public static final String TAG_LOCATION = "location";
        public static final String TAG_EMAIL = "email";
        public static final String TAG_PHONE = "phone";
        public static final String TAG_SHOP_IMAGE = "shop_photo";
        public static final String TAG_SHOPS = "shops";

        // shared preferences
        public static final String USER_ID = "userId";
        // Email address (make variable public to access from outside)
        public static final String KEY_EMAIL = "email";
        // Email address (make variable public to access from outside)
        public static final String KEY_PASSWORD = "password";
        // User Full name
        public static final String FULL_NAME = "fullname";
        //User profile picture
        public static final String PROFILE_PHOTO = "profile_photo";
    }

    public static class UrlConstants {
        public static final String url_get_all_items = "http://gula.baala-online.netii.net/get_all_items.php";
        public static final String url_get_categories = "http://gula.baala-online.netii.net/get_categories.php";
        public static final String url_get_sub_categories = "http://gula.baala-online.netii.net/get_sub_categories.php";
        public static final String url_cat_image = "http://gula.baala-online.netii.net/images/category";
        public static final String url_items_dir = "http://gula.baala-online.netii.net/images/items";
        public static final String url_get_sub_items = "http://gula.baala-online.netii.net/get_sub_categories.php";
        public static final String url_get_details = "http://gula.baala-online.netii.net/get_item_details.php";
        public static final String url_user_login = "http://baala-online.netii.net/gula/user_login.php";
        public static final String url_register = "http://baala-online.netii.net/gula/register_user.php";
        public static final String url_create_shop = "http://baala-online.netii.net/gula/create_shop.php";
        public static final String url_get_shop_items = "http://gula.baala-online.netii.net/get_shop_items.php";
        public static final String url_shop_images = "http://gula.baala-online.netii.net/images/shops";
        public static final String url_get_my_shops = "http://gula.baala-online.netii.net/get_my_shops.php";
        public static final String url_user_images = "http://gula.baala-online.netii.net/users";
        public static String url_get_by_sub_category = "http://gula.baala-online.netii.net/get_items_by_category.php";
    }

    public static class CloudinaryConstants {
        public static final String API_KEY = "268947264784883";
        public static final String CLOUD_NAME = "dhetftbrl";
        public static final String API_SECRET = "coLwJE28zSHULEsZCwKKdc485p8";

    }
}
