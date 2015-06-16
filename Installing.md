# Installing

Placing this in your ROM is actually quite easy.

Firstly, you should have checked out the instructions for building or acquiring this ROM

### Ok, where should I put the APK?

You should put the downloaded apk in the following folder:

##### Lollipop and later

The APK goes in /system/priv-app/OTAUpdates/OTAUpdates.apk

##### Kitkat and earlier

The APK goes in /system/priv-app/OTAUpdates.apk

### Anything else?

Yes! There is also a library to extract from the OTAUpdates.apk and place in the system folder.

##### First you need to know your ARM CPU Architecture

Install ADB and run this command

```shell
adb shell
cat /proc/cpuinfo
```

You may see "armeabi", armeabi-v7a or arm64-v8a

##### Now install the library

Now you you know which ARM architecture to use, you can open the OTAUpdates.apk and head into to OTAUpdates.apk/lib/ folder. Open the folder necessary for your ARM Architecture and extract the libbypass.so.

In your ROM you should now place that lib into /system/lib/libbypass.so
