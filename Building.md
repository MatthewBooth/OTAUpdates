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

#### ByPass

Once you've done the above, you need to edit the project one more final time:

- In Eclipse, right click on the project and choose "Properties"
- Choose "Java Build Path"
- Click the "Source" tab
- Choose "Add folder"
- Check "src_bypass"
- Done!

## Try a build

Now, try building and installing the apk on your device. It should now work.

For including in your ROM you need to head over to the instructions for [installing](Installing.md).

