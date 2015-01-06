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

#### Bypass

This is where things get interesting:-

##### Android NDK
- Head over to the [Android NDK page](https://developer.android.com/tools/sdk/ndk/index.html) and download the latest version for your OS. Again, this is assuming you're running Linux.

- Once it's downloaded, open up a terminal and find the file on your computer. Once you have, type
```shell
./nandroid-ndk-$VERSION-linux-$ARCH.bin
```
Replacing $VERSION and $ARCH with your NDK version and OS Architecture.

This will extract to a location on your computer. When this has finished, type this into a terminal
```shell
nano ~/.bashrc
```
To edit your .bashrc file and add these values to the bottom
```shell
export ANDROID_NDK_HOME=/home/kryten2k35/Android-NDK/Linux
export PATH=$PATH:$ANDROID_NDK_HOME
```
Change the ANDROID_NDK_HOME= path to your installation of the NDK

That's the NDK set up!

##### Boost C++ Libraries
Now we need some C++ libraries in order to compile Bypass.

- Head over to the [Boost website](http://www.boost.org/) and download the latest version to your computer

- Extract them to your computer. I used 
```shell
~/boost/boost
```

It's actually important to put them in folders like that. Maybe you cna use
```shell
~/cpp_libraries/boost
```

But most definitely put the path like that. Make sure the boost folder is inside another. If i doubt, use my first suggestion

##### Cloning Bypass

Now you can clone the bypass library to your computer. Again, I used ~/git
```shell
cd ~/git && git clone https://github.com/Kryten2k35/bypass
```

Now, head into the Bypass directory:
```shell
bypass/platform/android/library/jni
```

- Open up the Android.mk file in your favourite editor and look for this text:
```shell
/home/kryten2k35/boost
```

It should be on this line
```shell
LOCAL_C_INCLUDES:= ../../../dep/libsoldout ../../../src /opt/local/include /usr/local/include /home/kryten2k35/boost $(BYPASS_INCLUDE_PATH)
```

- Edit that text to point to YOUR boost directory that we setup earlier

- Save and exit

##### Compiling

Now we can compile Bypass

Open a terminal at this directory:-
```shell
bypass/platform/android/library
```
Type:
```shell
ndk-build
```
Hopefully, if you've done everything right, there will be no errors!

##### Importing into Eclipse
Now you can go ahead and import Bypass into eclipse

You want to import the library folder form above:
```shell
bypass/platform/android/library
```
Into your workspace.

Then, add Bypass as a library to the OTAUpdates project, like we did previously with the cardview library!

## Try a build

Now, try building and installing the apk on your device. It should now work.

Bare in mind, that having manually compiled the apk when placing in your ROM, you should extract the libraries in the lib/ folder of the apk and place them in:

- 4.4.4 or earlier = /system/lib
- 5.0 or later = /system/app/OTAUpdates/lib/arm 

Put the apk in:

- 4.4.4 or earlier = /system/app
- 5.0 or later = /system/app/OTAUpdates/ (and call it base.apk)

