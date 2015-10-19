package com.ota.updates.activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ota.updates.R;
import com.ota.updates.fragments.AboutFragment;
import com.ota.updates.fragments.AvailableFragment;
import com.ota.updates.fragments.CheckFragment;
import com.ota.updates.json.AddonJSONParser;
import com.ota.updates.json.VersionJSONParser;
import com.ota.updates.tasks.AsyncResponse;
import com.ota.updates.tasks.DownloadJSON;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;
import com.ota.updates.utils.fontawesome.DrawableAwesome;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements Constants, FragmentInteractionListener {
    public static final String TAG = MainActivity.class.getName();

    private Context mContext;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        downloadAndParseJSONManifest(savedInstanceState);

        // Initializing Toolbar and setting it as the actionbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        // Initializing Drawer Layout and ActionBarToggle
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        setupNavigationViewIcons(navigationView.getMenu());

        setupNavigationViewOnItemSelected(navigationView, drawerLayout);
    }

    private void downloadAndParseJSONManifest(final Bundle savedInstanceState) {
        final ProgressDialog loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage(mContext.getResources().getString(R.string.loading));
        loadingDialog.show();
        new DownloadJSON(this, new AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                loadingDialog.cancel();
                if (DEBUGGING) {
                    Log.d(TAG, "Json File finished downloading properly");
                }
                File jsonFile = new File(mContext.getApplicationContext().getFilesDir(), "aosp-jf.json");

                String json = null;
                try {
                    json = Utils.getFileContents(jsonFile);
                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage());
                } finally {
                    if (json != null) {
                        new VersionJSONParser(mContext, json).parse();
                        new AddonJSONParser(mContext, json).parse();
                        if (DEBUGGING) {
                            Log.d(TAG, "JSON data parsed and database updated");
                        }
                        loadFragment(savedInstanceState);
                    }
                }
            }
        }).execute();
    }

    private boolean loadFragment(Bundle savedInstanceState) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return true;
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
        return false;
    }

    /**
     * Sets up the NavigationView (drawer) icons
     * @param menu  The menu item that relates to NavigationView (use getMenu() )
     */
    private void setupNavigationViewIcons(Menu menu) {

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

        otaUpdatesItem.setIcon(getNavigationViewIcon(R.string.fa_refresh));

        otaVersionItem.setIcon(getNavigationViewIcon(R.string.fa_file_archive_o));
        otaAddonsItem.setIcon(getNavigationViewIcon(R.string.fa_puzzle_piece));

        romWebsiteItem.setIcon(getNavigationViewIcon(R.string.fa_globe));
        romDonateItem.setIcon(getNavigationViewIcon(R.string.fa_money));
        romInfoItem.setIcon(getNavigationViewIcon(R.string.fa_info));

        appSettingsItem.setIcon(getNavigationViewIcon(R.string.fa_cog));
        appProItem.setIcon(getNavigationViewIcon(R.string.fa_heart));
        appLicencesItem.setIcon(getNavigationViewIcon(R.string.fa_file_text_o));
        appGithubItem.setIcon(getNavigationViewIcon(R.string.fa_github));
        appAboutItem.setIcon(getNavigationViewIcon(R.string.fa_question));
    }

    /**
     * Creates an DrawableAwesome based on the string input given
     * Will also be coloured as per the drawerIconColors attribute
     * @param icon  the R.string that is requested
     * @return DrawableAwesome
     */
    private DrawableAwesome getNavigationViewIcon(int icon) {
        int[] attrs = {R.attr.drawerIconColors};
        TypedArray typedArray = this.obtainStyledAttributes(attrs);
        DrawableAwesome drawableAwesome = new DrawableAwesome(icon, 28, typedArray.getColor(0, Color.BLACK),
                true, false, 0, 0, 0, 0, mContext);
        typedArray.recycle();
        return drawableAwesome;
    }

    /**
     * Setup listeners for the NavigationView drawer so that when items are selected some actions
     * can be take
     *
     * @param navigationView  The NavigationView we are listening for
     * @param drawerLayout  The Drawer Layout containing the items
     */
    private void setupNavigationViewOnItemSelected(NavigationView navigationView, final DrawerLayout drawerLayout) {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

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
                drawerLayout.closeDrawers();

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

                    case R.id.app_about:
                        AboutFragment aboutFragment = new AboutFragment();
                        FragmentTransaction aboutFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        aboutFragmentTransaction.replace(R.id.fragment, aboutFragment);
                        aboutFragmentTransaction.commit();
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
