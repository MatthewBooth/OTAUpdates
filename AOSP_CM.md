## Using Android Open Source Project or CyanogenMod

If you are aiming to build this with an AOSP-based ROM (such as AOSP, CyanogenMod, AOKP, PA, Slim, etc) then there are a few steps you need to take to ensure this happens correctly.

### Including the project

To include this in your AOSP based ROM, you need to add it to your room_service.xml

```XML
<project path="packages/apps/OTAUpdates" name="Kryten2k35/OTAUpdates" revision="stable" />
```

You also need to include an extra build.prop line to help me determine that you are using an AOSP variant, as more better options are available if you are (such as rebooting without needing SU).

Please add
```shell
com.ota.aosp=true
```
to the build.prop along with the other values mentioned in the Building section.

### Extras

#### Pre-Lollipop

If you're building for a source tree earlier than Lollipop, then you will need to include the cardview support libraries in your source. Edit your room_services.xml to include this

```XML
<remove-project name="platform/frameworks/support" />
<project path="frameworks/support" name="platform/frameworks/support" revision="android-5.1.0_r1" />
```

To ensure you have the latest version of the support libraries

#### Google Play Services

Next you'll need to add the following Google libraries to your room_service.xml from CyanogenMod:

```XML
<project path="external/google" name="CyanogenMod/android_external_google" revision="cm-12.0" />
```