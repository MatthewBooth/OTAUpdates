package com.ota.updates.activities;
/*
 * Copyright (C) 2015 Matt Booth.
 *
 * Licensed under the Attribution-NonCommercial-ShareAlike 4.0 International
 * (the "License") you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.ota.updates.R;
import com.ota.updates.callbacks.AsyncResponse;
import com.ota.updates.db.helpers.RomSQLiteHelper;
import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.fragments.AboutFragment;
import com.ota.updates.fragments.AddonsFragment;
import com.ota.updates.fragments.CheckFragment;
import com.ota.updates.fragments.FileDownloadFragment;
import com.ota.updates.fragments.InfoFragment;
import com.ota.updates.fragments.VersionsFragment;
import com.ota.updates.items.RomItem;
import com.ota.updates.tasks.CheckForUpdateTask;
import com.ota.updates.tasks.DownloadJsonTask;
import com.ota.updates.tasks.ParseJsonTask;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Utils;
import com.ota.updates.utils.fontawesome.DrawableAwesome;


public class MainActivity extends AppCompatActivity implements Constants, FragmentInteractionListener {
    public static final String TAG = MainActivity.class.getName();

    private Context mContext;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // All important compatibility check
        // Kills the app if not compatible
        Boolean doesRomSupportApp = checkRomIsCompatible();

        // Download and parse our manifest
        if (doesRomSupportApp) {
            syncManifestWithDatabase();
        }

        // Initializing Toolbar and setting it as the actionbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        // Default selected drawer item
        navigationView.getMenu().getItem(0).setChecked(true);

        // Initializing Drawer Layout and ActionBarToggle
        initialisingDrawerLayout(mToolbar, navigationView);
    }

    private Boolean checkRomIsCompatible() {
        boolean doesRomSupportApp =  Utils.doesPropExist(PROP_MANIFEST);
        if (!doesRomSupportApp) {
            final Activity activity = this;
            final Resources resources = getResources();
            String title = resources.getString(R.string.no_support_title);
            String message = resources.getString(R.string.no_support_message);
            String findOutMore = resources.getString(R.string.no_support_find_out_more);

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle(title);
            alert.setMessage(message);
            alert.setCancelable(false);
            alert.setPositiveButton(findOutMore, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String findOutMoreLink = resources.getString(R.string.no_support_find_out_more_link);
                    Utils.openWebsite(mContext, findOutMoreLink);
                    activity.finish(); // This is very bad. But I need to end the app here
                }
            });
            alert.show();
        }
        return doesRomSupportApp;
    }

    private void initialisingDrawerLayout(final Toolbar mToolbar, NavigationView navigationView) {
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

        disableDrawerItems(navigationView.getMenu());

        setupNavigationViewIcons(navigationView.getMenu());

        setupNavigationViewOnItemSelected(navigationView, drawerLayout);
    }

    private void disableDrawerItems(Menu menu) {

        RomSQLiteHelper romSQLiteHelper = new RomSQLiteHelper(mContext);
        RomItem romItem = romSQLiteHelper.getRom();
        MenuItem item;

        if (romItem != null) {
            if (romItem.getDonateUrl().isEmpty() || romItem.getDonateUrl() == null) {
                item = menu.findItem(R.id.rom_donate);
                item.setVisible(false);

                if (DEBUGGING) {
                    Log.d(TAG, "Removed Donate URL");
                }
            }

            if (romItem.getWebsiteUrl().isEmpty() || romItem.getWebsiteUrl() == null) {
                item = menu.findItem(R.id.rom_website);
                item.setVisible(false);

                if (DEBUGGING) {
                    Log.d(TAG, "Removed Website URL");
                }
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment) != null) {

            // Get the fragment manager
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, fragment).commit();
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

        MenuItem romWebsiteItem = menu.findItem(R.id.rom_website);
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
    private void setupNavigationViewOnItemSelected(final NavigationView navigationView, final DrawerLayout drawerLayout) {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem menuItem1 = menu.getItem(i);

                    if ((menuItem1.getItemId() != menuItem.getItemId())) {
                        menuItem1.setCheckable(false);
                    }
                }

                menuItem.setCheckable(true);

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                RomSQLiteHelper romSQLiteHelper = new RomSQLiteHelper(mContext);
                RomItem romItem = romSQLiteHelper.getRom();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.ota_updates:
                        syncManifestWithDatabase();
                        return true;
                    case R.id.ota_versions:
                        loadFragment(new VersionsFragment());
                        return true;
                    case R.id.ota_addons:
                        loadFragment(new AddonsFragment());
                        return true;
                    case R.id.rom_website:
                        Utils.openWebsite(mContext, romItem.getWebsiteUrl());
                        return true;
                    case R.id.rom_donate:
                        Utils.openWebsite(mContext, romItem.getDonateUrl());
                        return true;
                    case R.id.rom_information:
                        loadFragment(InfoFragment.newInstance());
                        return true;
                    case R.id.app_settings:
                        return true;
                    case R.id.app_pro:
                        return true;
                    case R.id.app_licences:
                        return true;
                    case R.id.app_github:
                        String appGitHubUrl = mContext.getResources().getString(R.string.app_github_url);
                        Utils.openWebsite(mContext, appGitHubUrl);
                        return true;
                    case R.id.app_about:
                        loadFragment(new AboutFragment());
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void downloadJson(final ProgressDialog loadingDialog) {
        new DownloadJsonTask(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                if (output) {
                    parseJson(loadingDialog);
                } else {
                    loadingDialog.cancel();
                }
            }
        }).execute();
    }

    private void parseJson(final ProgressDialog loadingDialog) {
        new ParseJsonTask(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                if (output) {
                    checkForUpdate(loadingDialog);
                } else {
                    loadingDialog.cancel();
                }
            }
        }).execute();
    }

    private void checkForUpdate(final ProgressDialog loadingDialog) {
        new CheckForUpdateTask(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                if (DEBUGGING) {
                    Log.d(TAG, "Update availability is " + output);
                }

                if (output) {
                    VersionSQLiteHelper versionSQLiteHelper = new VersionSQLiteHelper(mContext);
                    int fileId = versionSQLiteHelper.getLastVersionItem().getId();
                    loadFragment(FileDownloadFragment.newInstance(FILE_TYPE_VERSION, fileId));
                } else {
                    loadFragment(new CheckFragment());
                }

                String time = Utils.getTimeNow(mContext);
                Preferences.setUpdateLastChecked(mContext, time);

                loadingDialog.cancel();
            }
        }).execute();
    }

    private void syncManifestWithDatabase() {
        final ProgressDialog loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage(mContext.getResources().getString(R.string.loading));
        loadingDialog.show();

        downloadJson(loadingDialog);
    }

    @Override
    public void onRefreshClickInteraction() {
        syncManifestWithDatabase();
    }

    @Override
    public void onOpenFileDownloadView(int fileType, int fileId) {
        loadFragment(FileDownloadFragment.newInstance(fileType, fileId));
    }
}
