# Installing

Placing this in your ROM is actually quite easy.

Firstly, either compile yourself using the instructions, or head over to [UltimaROM.com](http://ultimarom.com/downloads/ota-updates/) and get a recompiled APK. The pre-compiled version is much quicker.

### Which version of the file do I need?

You need the version of the file that corresponds to the Android version of your ROM, and your ARM CPU architecture.

##### Finding your ARM CPU architecture

Install ADB and run this command

```shell
adb shell
cat /proc/cpuinfo
```

You may see "armeabi", armeabi-v7a or arm64-v8a

##### Finding your Android version 

This APK only supports from Android 4.0 to 5.0+

- If you are on a ROM that is earlier than Kitkat, then you need the "v11" zip

- If you are on a ROM that is Kitkat, then you need the "v19" zip

- If you are on a ROM that is Lollipop, then you need the "v21" zip

##### Ok, now what?

Now you've got the zip, extract it and drag and drop into your system folder