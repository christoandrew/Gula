package com.iconasystems.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.iconasystems.Constants;
import com.iconasystems.gula.AboutActivity;
import com.iconasystems.gula.CategoryActivity;
import com.iconasystems.gula.MainActivity;
import com.iconasystems.gula.MyShopsActivity;
import com.iconasystems.gula.R;
import com.iconasystems.gula.ShopActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by Raymo on 6/9/2015.
 */
public class NavigationBuilder {
    public static final String FULL_NAME = "fullname";
    public static final String KEY_EMAIL = "email";
    public static final String PROFILE_PHOTO = "profile_photo";
    private Activity _context;
    private Toolbar mToolbar;
    private AccountHeader mAccountHeader;
    private Drawer mDrawer;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;

    public NavigationBuilder(Activity context) {
        this._context = context;
    }

    public NavigationBuilder(Activity context, Toolbar toolbar) {
        this(context);
        this.mToolbar = toolbar;
        sessionManager = new SessionManager(_context);
        userDetails = sessionManager.getUserDetails();
    }

    public NavigationBuilder(Activity context, Toolbar toolbar, AccountHeader accountHeader) {
        this(context, toolbar);
        this.mAccountHeader = accountHeader;
    }

    public Drawer getDrawer(Toolbar toolbar) {
        this.mDrawer = new DrawerBuilder()
                .withActivity(this._context)
                .withToolbar(this.mToolbar)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Home"),
                        new SecondaryDrawerItem().withName("Categories"),
                        new SecondaryDrawerItem().withName("My Shops"),
                        new SecondaryDrawerItem().withName("Logout"),
                        new SecondaryDrawerItem().withName("Feedback"),
                        new SecondaryDrawerItem().withName("About")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .build();

        return this.mDrawer;
    }

    public Drawer getDrawerWithHeader() {
        this.mDrawer = new DrawerBuilder()
                .withActivity(_context)
                .withAccountHeader(getAccountHeader())
                .withToolbar(this.mToolbar)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Home"),
                        new SecondaryDrawerItem().withName("Categories"),
                        new SecondaryDrawerItem().withName("My Shops"),
                        new SecondaryDrawerItem().withName("Logout"),
                        new SecondaryDrawerItem().withName("About")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (position) {
                            case 0:
                                _context.startActivity(new Intent(_context, MainActivity.class));
                                return true;
                            case 1:
                                _context.startActivity(new Intent(_context, CategoryActivity.class));
                                return true;
                            case 2:
                                _context.startActivity(new Intent(_context, MyShopsActivity.class));
                                return true;
                            case 3:
                                sessionManager.logoutUser();
                                return true;
                            case 4:
                                _context.startActivity(new Intent(_context, AboutActivity.class));
                                return true;


                        }
                        return false;
                    }
                })
                .build();


        return this.mDrawer;
    }

    private AccountHeader getAccountHeader() {

        String fullname = userDetails.get(FULL_NAME);
        String email = userDetails.get(KEY_EMAIL);
        String photo = userDetails.get(PROFILE_PHOTO);
        final String image_url = Constants.UrlConstants.url_user_images + "/" + photo;

        // Create the AccountHeader
        this.mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this._context)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(fullname).withEmail(email)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                ImageLoader.getInstance().displayImage(image_url, imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                ImageLoader.getInstance().cancelDisplayTask(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });

        return this.mAccountHeader;
    }
}
