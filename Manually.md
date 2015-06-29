## Manually

If you want to, you can host the ROM zip and ota.xml manifest yourself! You can use direct links to the ROM or input a HTTP link into the manifest, which will prompt the OTA app to open a web browser in order to download the ROM. Useful if you just want the app to be a notification of an update, and drive traffic somewhere in particular

#### Include this in your ROM:

- Download the APK from [here](http://ultimarom.com/downloads/ota-updates/) OR compile this project using Eclipse ([Check the build instructions](Building.md)). 

  You can also use this in your AOSP ROM. You should know how to add this to your build, if so. A makefile is included.
  
- Open the .apk file using an archive manager like 7zip and extract the necessary lib file for your device. Most devices will use armeabi, armeabi-v7a or arm64-v8a. Please the libbypass.so file in your system folder as follows:

For Lollipop devices put the apk and lib file like this: 

- system/app/OTAUpdates/base.apk
- system/app/OTAUpdates/lib/arm/libbypass.so
														
For Pre-Lollipop devices, like this:

- system/app/OTAUpdates.apk
- system/lib/libbypass.so

#### Create an ota.xml

The OTA app needs an XML document stored online somewhere to periodically check and compare OTA version values to see if there is an update. You must create this and store it somewhere you can have a direct link access to it. You can use a personal website, or somewhere like dropbox's public folder or private shares.

The XML should look like this:-

``` XML
<?xml version="1.0" encoding="UTF-8"?>
<ROM>
  <!-- Your ROM Name -->
  <RomName>NAME OF THE ROM</RomName>
  <VersionName>ROM VERSION</VersionName>
  <!-- Your ROM version. MUST be incrementally larger than previous versions -->
  <!-- Do NOT use letters in your version number. Integers only -->
  <!-- Previous version will be read from the build.prop -->
  <VersionNumber type="integer">20150105</VersionNumber>
  <!-- Linking to your files -->
    <!-- <DirectUrl><![CDATA[http://www.example.com?file=file.zip?somethingelse&anotherthing]]></DirectUrl> -->
    <!-- Use the above format if your URL has special characters in then, like an (&) ampersand -->
    <!-- Enter <DirectUrl nil="true /> or <HttpUrl nil="true /> to indicate you won't be using a type of URL -->
    <!-- HTTP link will open a web page -->
    <!-- Direct Link overrides HTTP link -->
  <DirectUrl>DIRECT LINKS HERE</DirectUrl>
  <HttpUrl>HTTP WEBSITE LINK HERE</HttpUrl>
  <!-- What version are you issuing? -->
  <Android>5.0.1</Android>
  <!-- Providing File Checking for direct downloads. You SHOULD offer this for your users whether using HTTP or Direct -->
  <!-- OPTIONAL: Enter <CheckMD5 nil="true" /> offer MD5 checking -->
  <CheckMD5>MD5 HERE</CheckMD5>
  <!-- The filesize of your ROM -->
  <!-- Please enter this in BYTES only. Otherwise an incorrect value will be shown -->
  <FileSize type="integer">420147739</FileSize>
  <!-- Developer or Team name. You can put anything here, even a couple of names -->
  <!-- OPTIONAL: Enter <Developer nil="true" /> to hide this -->
  <Developer>DEVELOPER NAMES HERE</Developer>
  <!-- Your ROM or Developer Website. Can be a personal site or your XDA/forum thread -->
  <!-- OPTIONAL: Enter <Website nil="true" /> to hide this -->
  <WebsiteURL>WEBSITE LINK HERE</WebsiteURL>
  <!-- Your Donate URL -->
  <!-- OPTIONAL: Enter <DonateURL nil="true" /> to hide this -->
  <DonateURL>DONATE URL HERE</DonateURL>
  <!-- Put the changelog here... Use Markdown to format it -->
  <!-- Again, surround special characters with the CDATA tags or your XML will NOT parse -->
  <Changelog>### Changelog 20150105
* Stuff
* Updated this, or that
* Fixed all the things
* We even fixed this
* Don't forget all the awesome features you added</Changelog>
</ROM>
```
Please be aware of the rules regarding [special characters in XML documents](http://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references#Predefined_entities_in_XML). If your text needs to use them (for example, writing AT&T in the changelog. Or an & symbol in a URL) then you NEED to wrap that text in CDATA tags like this:
```XML
<![CDATA[http://www.example.com?file=file.zip?somethingelse&anotherthing]]>
```
You should also use Markdown for the changelog. This allows easy formatting for you and it renders really well in the app. You can use full Markdown for this, including links and images (be aware of screen sizes, though). The example above shows some basic Markdown in the changelog tags. 

Here are some useful links to help with Markdown:-
- [http://dillinger.io/](http://dillinger.io/) -  to help checking how it looks visually
- [Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/markdown-Cheatsheet)

#### Using an optional Addons XML

You may use an addition XML file for providing addon and extra files for your ROM. For instance, kernel files, or quick fixes.

You should format it like this:

``` XML
<?xml version="1.0" encoding="utf-8"?>
<hash>
    <addons>
        <addon>
            <id>1</id> <!-- Use integers only, make them unique -->
            <name>Accuweather</name> <!-- The name, simples -->
            <description>Accuweather Daemon and Widgets</description> <!-- You can use markdown here if you want -->
            <updated-at>2014-12-24</updated-at> <!-- Must be in yyyy-mm-dd format -->
            <size>9001027</size> <!-- filesize in bytes -->
            <download-link>
            https://romhut.com/addons/accuweather-53b5c4b3242f2/download
            </download-link>
        </addon>
        <addon>
            <id>2</id> <!-- Use integers only, make them unique -->
            <name>Samsung Wallet</name> <!-- The name, simples -->
            <description>Samsung's Wallet technology</description> <!-- You can use markdown here if you want -->
            <updated-at>2014-10-14</updated-at> <!-- Must be in yyyy-mm-dd format -->
            <size>5427814</size> <!-- filesize in bytes -->
            <download-link>
            https://romhut.com/addons/samsung-wallet-53b5c482ce61c/download
            </download-link>
        </addon>
    </addons>
</hash>
```

Doing this, you should add these following elements to the bottom of your OTA XML

```XML
<AddonCount>2</AddonCount>
<AddonsURL>
https://romhut.com/roms/danvdh-googleedition/addons.xml
</AddonsURL>
```
Taking care to provide a direct link to your XML in the AddonsUrl tag, and providing an accurate count in the AddonCount tag.


#### Editing your build.prop

At the bottom of your build.prop, add the following values editing them to suit your needs, for example:

```
# OTA Updates
ro.ota.romname=JFLTE-GPE
ro.ota.version=20150105
ro.ota.manifest=https://example.com/path/to/ota.xml
```
  
Please pay CLOSE attention to the ro.ota.version entry. This is not your particular ROM version (v6.5 or v1.2.5, for example) this is a value for the OTA app to determine if an update is available. Your NEXT version should be numerically higher than this. You may use value you like, so long as it is an integer and successive updates are larger.

This example is using Universal Standard time, which is YYYYMMDD. It is a good version scheme to use for the OTA app if you do not plan on release more than one version per ROM per day

#### Publish

With the above done. Zip up your ROM and publish to your favourite website. 

Your ROM is now ready to receive OTA updates when you release an update

## Releasing an update

Ok, now the above is done, releasing an update is very easy!

- Edit your build.prop so that your version is incrementally higher than before

```
  # Before
  ro.ota.version=20150105
  
  # After
  ro.ota.version=20150108 
```
  
- Zip your ROM

- Replace/overwrite your ota.xml whereever you happen to have stored it

- Publish your ROM on your favourite website

Now all your users will get an OTA update notification at some point, whenever their device checks for one!

# Notes
## Direct or HTTP links
Direct links must be exactly that. They cannot include any kind of gateway or web page that requires you to click a button. Generally, if you can just paste the link into an address bar and it starts to download, it's direct. Not everyone has access to to this kind of service, especially for ROMs as it can be bandwidth intensive, but for those that do, this is there for them.

This is why, alternatively, you can use HTTP links. In this case, the user's web browser will be opened instead.