## Building

Building from source is quite easy, but will require a little bit of extra work to get going.

This guide will assume you have Eclipse installed already, along with the Android-SDK. 

It will also assume you are running Linux, because I do not know how to install the Android-NDK on Windows (I use Linux exclusively). There are some useful guides on this on the internet though.

#### OTA Updates

- Clone this repo to somewhere on your computer. I use ~/git

```shell
cd ~/git && git clone https://github.com/Kryten2k35/OTAUpdates
```
- Open up Eclipse and import "Existing Android code into workspace" and follow the instructions during the wizard. Things will not compile properly at the moment, because we're not finished

#### CardView

You also need to import the cardview library from Google's support code. Make sure your Android SDK is up to date and import form the following location:
```shell 
$SDK_ROOT/extras/android/support/v7/cardview
```
Now right click on the OTAUpdates project in Eclipse and choose properties and add cardview as library (or edit the missing library that most likely exists)

#### Google Play Services

You also need to import the google_play_services library from Google's extras code. Make sure your Android SDK is up to date and import form the following location:
```shell 
$SDK_ROOT/extras/google/google_play_services
```
Now right click on the OTAUpdates project in Eclipse and choose properties and add google_play_services as library (or edit the missing library that most likely exists).

## Try a build

Now, try building and installing the apk on your device. It should now work.

Bare in mind, that having manually compiled the apk when placing in your ROM, you should extract the libraries in the lib/ folder of the apk and place them in:

- 4.4.4 or earlier = /system/lib
- 5.0 or later = /system/app/OTAUpdates/lib/arm 

Put the apk in:

- 4.4.4 or earlier = /system/app
- 5.0 or later = /system/app/OTAUpdates/ (and call it base.apk)

