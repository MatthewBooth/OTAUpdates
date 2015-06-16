## RomHut

RomHut will handle most of the work for you. You can use it to host your files and manifest. It's a great ROM hosting website. It's free for developers and users, with no insane wait times or download restrictions.

#### Include this in your ROM:

You can use this application in a few different ways:

- Download the APK from [here](http://ultimarom.com/downloads/ota-updates/)
- Compile this project using Eclipse ([Check the build instructions](Building.md))
- Build from source with your AOSP ROM ([Check the AOSP build instructions](AOSP_CM.md))

#### Installing the app

If you have compiled or downloaded the APK, head over to the instructions for [installing](Installing.md).

If you compiled with AOSP, this is already done for you.

#### Head over to RomHut

- Head over to Romhut.com and [apply for a developer account](https://romhut.com/developers/new). It's free for all

- Create a new ROM by going [here](https://romhut.com/dashboard/roms/new)

- Now, on your ROM Page, click "OTA" on the left-hand side bar. At the bottom of that page are some values for your build.prop

#### Editing your build.prop

At the bottom of your build.prop, add the following values from the OTA page, for example:

```
# OTA Updates
ro.ota.romname=JFLTE-GPE
ro.ota.version=20150105
ro.ota.manifest=https://romhut.com/roms/danvdh-googleedition/ota.xml
```

Please pay CLOSE attention to the ro.ota.version entry. This is not your particular ROM version (v6.5 or v1.2.5, for example) this is a value for the OTA app to determine if an update is available. Your NEXT version should be numerically higher than this. You may use value you like, so long as it is an integer and successive updates are larger.

This example is using Universal Standard time, which is YYYYMMDD. It is a good version scheme to use for the OTA app if you do not plan on release more than one version per ROM per day.

#### Back to Romhut

- Head back to Romhut and create a new version. For your first ROM the version you entered in the build.prop and the version number you enter when creating on Romhut should match

- You're done! You can publish your ROM. You can offer a Romhut website link to XDA for any new users, or those that prefer to download manually

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

- Create a new version on Romhut (make sure you use the same version number)

- Publish that version

Now all your users will get an OTA update notification at some point, whenever their device checks for one!
