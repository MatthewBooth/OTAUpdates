# OTAUpdates


A useful tool to help ROM developers provide OTA updates to their users. It's free to use, you can build from source or use the APK provided

# Requirements


1. Root - not essential, but you will most likely have this anyway
2. A place to store a static manifest xml. Dropbox will work fine, and any webserver.
3. Some build.prop extries

# How-to

## To use this in your ROM:

1. Download the APK from [here](http://ultimarom.com/downloads/ota-updates/) OR compile this project using Eclipse. 

  You can also use this in your AOSP ROM. You should know how to add this to your build, if so. A makefile is included.
  
2. Place the compiled file in /system/app/

## Include this in your build.prop

3. Edit your build.prop

Include these values:

    ro.ota.romname=UltimaROM
    ro.ota.version=1.0
    ro.ota.codename=Diablos
    ro.ota.device=i9300
    ro.ota.manifest=http://www.ultimarom.com/rom/update/example/update_manifest.xml

Codename is not essential and can be put as "null" without the quotation marks if you don't use them. I like to give my ROMs a name, but you could also use this to place "Stable" or "Beta" instead.

  You can use a Dropbox link for the manifest.
  
## Create your manifest

Next you need create your manifest.xml

Here is an [example](http://www.ultimarom.com/rom/update/example/update_manifest.xml) and you can see it below:

    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <ROM>
    <!-- Your ROM Name -->
    <Name>UltimaROM</Name>
    <!-- Your ROM version. MUST be incrementally larger than previous versions -->
    <!-- Do NOT use letters in your version number. decimals or straight integers only -->
    <!-- Previous version will be read from the build.prop -->
    <Version>v17.0</Version>
    <!-- Do you use Codenames? I do -->
    <!-- OPTIONAL: Enter null to hide this -->
    <Code>Diablos</Code>
    <!-- Linking to your files -->
    <!-- <DirectUrl><![CDATA[http://www.example.com?file=file.zip?somethingelse&anotherthing]]></DirectUrl> -->
    <!-- Use the above format if your URL has special characters in then, like an (&) ampersand -->
    <!-- Enter null to indicate you won't be using a type of URL -->
    <!-- HTTP link will open a web page -->
    <!-- Direct Link trumps HTTP link -->
    <DirectUrl><![CDATA[http://www.example.com?file=file.zip?somethingelse&anotherthing]]></DirectUrl>
    <HTTPUrl>null</HTTPUrl>
    <!-- Providing File Checking for direct downloads. You SHOULD offer this for your users whether using HTTP or Direct -->
    <!-- OPTIONAL: Enter null to not offer MD5 checking -->
    <CheckMD5>0ab70f4d039b507c2b76ff02ac8d0e3d</CheckMD5>
    <!-- What version are you issuing? -->
    <Android>4.4.2</Android>
    <!-- Developer or Team name. You can put anything here, even a couple of names -->
    <!-- OPTIONAL: Enter null to hide this -->
    <Developer>Kryten2k35</Developer>
    <!-- Your ROM or Developer Website. Can be a personal site or your XDA/forum thread -->
    <!-- OPTIONAL: Enter null to hide this -->
    <Website>http://www.ultimarom.com</Website>
    <!-- Your Donate URL -->
    <!-- OPTIONAL: Enter null to hide this -->
    <DonateURL>http://goo.gl/ZKSY4</DonateURL>
    <!-- Put the changelog here... a single ; will be interpreted as a new line -->
    <!-- New lines will be a new bullet point -->
    <!-- Again, surround special characters with the CDATA tags or your XML will NOT parse -->
    <Changelog>
    Changed this for that; Then I changed another thing ; I changed lots of things ; I cannot imagien the number of changes I made, but they are here, and now you can see them
    </Changelog>
    </ROM>

Upload this somewhere in the internet that you can access easily. That could be your webserver or dropbox, and place the link in your build.prop

## Issuing an update
Really easy. 

Edit the manifest to include the new information. Make sure you increment your version number so that it is at least higher than the previous. So, 1.0 to 2.0. You can inform your users in your thread that there is an update, and they can open the app and receive it. 

The app also has a background checking service and notification that runs, by default, every 3 days. This can be switched off by users for their convenience.

# Notes
## Direct or HTTP links
Direct links must be exactly that. They cannot include any kind of gateway or web page that requires you to click a button. Generally, if you can just paste the link into an address bar and it starts to download, it's direct. Not everyone has access to to this kind of service, especially for ROMs as it can be bandwidth intensive, but for those that do, this is there for them.

This is why, alternatively, you can use HTTP links. In this case, the user's web browser will be opened instead.

# Usage

Anyone is free to use this project in their ROM. I only request that you keep the about page in-tact leaving my credits in there. You don't have to, but it'd be nice if you did.

# Contributions

If you feel like you can contribute to this project, don't hesitate to fork and send me some pull requests.