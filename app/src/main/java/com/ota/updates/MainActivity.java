package com.ota.updates;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        int[] attrs = { com.mikepenz.materialdrawer.R.attr.material_drawer_icons };
        TypedArray typedArray = obtainStyledAttributes(attrs);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.drawer_section_rom).setDivider(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_primary_updates).withIcon(new IconDrawable(this,
                                Iconify.IconValue.fa_refresh).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
                                .sizeDp(16)),
                        new PrimaryDrawerItem().withName(R.string.drawer_primary_addons).withIcon(new IconDrawable(this,
                                Iconify.IconValue.fa_puzzle_piece).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
                                .sizeDp(16)),
                        new PrimaryDrawerItem().withName(R.string.drawer_primary_rom_website).withIcon(new IconDrawable(this,
                                Iconify.IconValue.fa_globe).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
                                .sizeDp(16)),
                        new PrimaryDrawerItem().withName(R.string.drawer_primary_rom_donate).withIcon(new IconDrawable(this,
                                Iconify.IconValue.fa_money).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
                                .sizeDp(16)),
                        new PrimaryDrawerItem().withName(R.string.drawer_primary_rom_information).withIcon(new IconDrawable(this,
                                Iconify.IconValue.fa_info).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
                                .sizeDp(16)),
                        new SectionDrawerItem().withName(R.string.drawer_section_app),
                        new SecondaryDrawerItem().withName(R.string.drawer_secondary_settings).withIcon(new IconDrawable(this,
                                Iconify.IconValue.fa_cog).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
                                .sizeDp(16)),
                        new SecondaryDrawerItem().withName(R.string.drawer_secondary_about).withIcon(new IconDrawable(this,
                                Iconify.IconValue.fa_question).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
                                .sizeDp(16))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return false;
                    }
                })
                .build();
    }
}
