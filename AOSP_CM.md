## Using Android Open Source Project or CyanogenMod

If you are aiming to build this with an AOSP-based ROM (such as AOSP, CyanogenMod, AOKP, PA, Slim, etc) then there are a few steps you need to take to ensure this happens correctly.

### Including the project

To include this in your AOSP based ROM, you need to add it to your room_service.xml

```XML
<project path="packages/apps/OTAUpdates" name="Kryten2k35/OTAUpdates" revision="aosp" />
```

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

#### ByPass

Another one to be added to your room service, this time for ByPass

```XML
<project path="external/bypass" name="Kryten2k35/bypass" revision="aosp" />
```

#### Boost

Finally, you should add the boost libraries to your external folder too, using this entry in your room_service.xml

```XML
<project path="external/boost" name="Kryten2k35/boost" revision="aosp" />
```