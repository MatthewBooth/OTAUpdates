package com.ota.updates.activities;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.ota.updates.R;
import com.ota.updates.fragments.AvailableFragment;
import com.ota.updates.fragments.CheckFragment;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;


public class MainActivity extends AppCompatActivity implements Constants, FragmentInteractionListener {

    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // Initializing Toolbar and setting it as the actionbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Initializing NavigationView
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        setupNavigationViewOnItemSelected();

        setupNavigationViewIcons();


        // Initializing Drawer Layout and ActionBarToggle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Get the fragment manager
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Create a new Fragment to be placed in the activity layout
            AvailableFragment availableFragment = new AvailableFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            availableFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentManager.beginTransaction()
                    .add(R.id.fragment, availableFragment).commit();
        }
    }

    /**
     * Sets up the navigation icons for the drawer
     */
    private void setupNavigationViewIcons() {
        Menu menu = mNavigationView.getMenu();

        int[] attrs = {R.attr.drawerIconColors};
        TypedArray typedArray = this.obtainStyledAttributes(attrs);

        MenuItem otaUpdatesItem = menu.findItem(R.id.ota_updates);
        MenuItem otaVersionItem = menu.findItem(R.id.ota_versions);
        MenuItem otaAddonsItem = menu.findItem(R.id.ota_addons);

        MenuItem romWebsiteItem = menu.findItem(R.id.rom_webite);
        MenuItem romDonateItem = menu.findItem(R.id.rom_donate);
        MenuItem romInfoItem = menu.findItem(R.id.rom_information);

        MenuItem appSettingsItem = menu.findItem(R.id.app_settings);
        MenuItem appProItem = menu.findItem(R.id.app_pro);
        MenuItem appLicencesItem = menu.findItem(R.id.app_licences);
        MenuItem appGithubItem = menu.findItem(R.id.app_github);
        MenuItem appAboutItem = menu.findItem(R.id.app_about);

        otaUpdatesItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_refresh)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        otaVersionItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_file_archive_o)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        otaAddonsItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_puzzle_piece)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));

        romWebsiteItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_globe)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        romDonateItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_money)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        romInfoItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_info)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));

        appSettingsItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_cog)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        appProItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_heart)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        appLicencesItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_file_text_o)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        appGithubItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_github)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));
        appAboutItem.setIcon(new IconDrawable(this, Iconify.IconValue.fa_question)
                .colorRes(typedArray.getResourceId(0, Color.BLACK))
                .sizeDp(16));

        typedArray.recycle();
    }

    /**
     * Creates the onItemSelected logic for the Navigation Icon
     */
    private void setupNavigationViewOnItemSelected() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.ota_updates:
                        Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
                        AvailableFragment AvailableFragment = new AvailableFragment();
                        FragmentTransaction availableFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        availableFragmentTransaction.replace(R.id.fragment, AvailableFragment);
                        availableFragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.ota_versions:
                        Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
                        CheckFragment checkFragment = new CheckFragment();
                        FragmentTransaction checkFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        checkFragmentTransaction.replace(R.id.fragment, checkFragment);
                        checkFragmentTransaction.commit();
                        return true;
                    case R.id.ota_addons:
                        Toast.makeText(getApplicationContext(), "Send Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.rom_donate:
                        Toast.makeText(getApplicationContext(), "Drafts Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
