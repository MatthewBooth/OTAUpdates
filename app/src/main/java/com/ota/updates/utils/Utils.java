package com.ota.updates.utils;

public class Utils {

//    /**
//     * This method creates the Material Drawer for us when being called in an Activity's "OnCreate" method.
//     * As every activity would call this method with only slight variations, it was split off into a static method
//     * such that it could be called from anywhere
//     *
//     * @param activity         The activity calling this method
//     * @param context          The context of the activity calling this method
//     * @param toolbar          The toolbar item belonging to the calling method
//     * @param selectedPosition The default selected position (should usually be the activity's position in the list)
//     *//*
//    public static void setupDrawer(Activity activity, Context context, Toolbar toolbar, int selectedPosition) {
//        int[] attrs = {com.mikepenz.materialdrawer.R.attr.material_drawer_icons};
//        TypedArray typedArray = context.obtainStyledAttributes(attrs);
//
//        Drawer result = new DrawerBuilder()
//                .withActivity(activity)
//                .withToolbar(toolbar)
//                .withSelectedItem(selectedPosition)
//                .addDrawerItems(
//                        new SectionDrawerItem().withName(R.string.drawer_section_rom).setDivider(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_primary_updates).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_refresh).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new PrimaryDrawerItem().withName(R.string.drawer_primary_versions).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_file_archive_o).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new PrimaryDrawerItem().withName(R.string.drawer_primary_addons).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_puzzle_piece).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new PrimaryDrawerItem().withName(R.string.drawer_primary_rom_website).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_globe).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new PrimaryDrawerItem().withName(R.string.drawer_primary_rom_donate).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_money).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new PrimaryDrawerItem().withName(R.string.drawer_primary_rom_information).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_info).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new SectionDrawerItem().withName(R.string.drawer_section_app),
//                        new SecondaryDrawerItem().withName(R.string.drawer_secondary_settings).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_cog).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new SecondaryDrawerItem().withName(R.string.drawer_secondary_pro_bought).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_heart).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new SecondaryDrawerItem().withName(R.string.drawer_secondary_licences).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_file_text_o).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new SecondaryDrawerItem().withName(R.string.drawer_secondary_github).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_github).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16)),
//                        new SecondaryDrawerItem().withName(R.string.drawer_secondary_about).withIcon(new IconDrawable(context,
//                                Iconify.IconValue.fa_question).colorRes(typedArray.getResourceId(0, Color.BLACK)).alpha(153)
//                                .sizeDp(16))
//                )
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
//                        // do something with the clicked item :D
//                        return false;
//                    }
//                })
//                .build();
//        typedArray.recycle();
//    }*/
}
