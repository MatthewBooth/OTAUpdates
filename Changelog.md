#### v2.4
##### 26th May 2015
* Added first run message to inform about new features

* Fixed Open Recovery Script on non-AOSP ROMs. Should now work properly

* Added "install after ROM functionality" - Put zips inside "/sdcard/OTAUpdates/InstallAfterFlash/" and they will be automatically flashed after an update, if using Open Recovery Script

* Added delete confirmation on addons

* Change default download directories. Now they go into /sdcard/OTAUpdates

* Translations for Italian and Russian

* Added AOSP makefile functionality for AOSP ROMs built form source

* Open Recovery Script is now on by default

#### v2.3.1
##### 15th March 2015
* Updated to target/be compatible with API v22 (Android 5.1)

* Show a warning dialog when trying to download an addon on a mobile network, but Wi-Fi only is in the settings

* Show a button on the network warning dialogs to take you to settings and adjust if need be 

* Card layout in the About activity

* Changelog viewing (for this app) in the About activity

* Removed lots of unnecessary and unused images from the Holo-based themes

* Fixed links in the Addons description markdown not being able to be clicked

#### v2.3 
##### 13th March 2015
* Fixed cancel button not updating to install when the download finishes

* Important licence changes (to ensure the app cannot be taken and resold by someone -it stays free)

* Added ignore release option (found in the notification for a new release. Can be undone in settings)

* Added notification shortcut to the download page

* More robust update checking code

* Font changes in about page - more legible

* Added BitCoin donations (for ROM developers and the me)

* Removed some debugging code showing for end users

* Theme tweaks to Material theming, more true to proper Material light and dark.

* Fixed layout height being broken on the update available card

* Better method of rechecking for an update

* Advertisements added. A small evil, but you can disable them freely in the app settings. Please consider keeping them for me.
* XML parsing is more robust and should result in fewer FC's or errors

* Ongoing notification for downloading now takes you to the download page

* Added Addons (can be added manually via a new XML, or automatically on RomHut

#### v2.2
##### 9th January 2015
* Fixed with MD5 returning false if you use uppercase MD5 string in the XML

* Fixed OpenRecoveryScript not installing if used (looking for the file in a folder with 0 in it.)

* Added Dutch translation

* Added Portuguese (Brazil) translation

* Added German translation

* Added Spanish translation

#### v2.1
##### 8th January 2015
* Fixed issue with ROMs not seeing updates

* Fixed background service not switching off

* Background service will now correctly check for an update on boot (if on)

* Theme no longer changes to dark after accessing the settings

* New material icons

* New changelog parsing (using Btpass HTML to parse Markdown in changelogs)

* Changed priority of notification to be viewable on the lockscreen

* New notification controls (change the sound and toggle vibrate)

* Integration with major hosting provider (RomHut.com)

* Tapping a notification now takes you to the update page

* General cleaning of the code and making more efficient

#### v2.0
##### 5th December 2014 
* Material UI for 5.0+ devices

* More robust checking of version numbers

* Removed location chooser - Better to store on the internal storage and limit the chance of an error with using an external SD Card

* Fixed MD5 checking

* Default checking on boot time is 12 hours rather than 3 days

#### v1.0
##### 19th July
* Initial release