# OTAUpdates


A useful tool to help ROM developers provide OTA updates to their users. It's free to use, you can build from source or use the APK provided.

# Issues
Please report and track any issues using the following button: [![Stories in Ready](https://badge.waffle.io/MatthewBooth/OTAUpdates.svg?label=ready&title=Ready)](http://waffle.io/MatthewBooth/OTAUpdates)
[![Throughput Graph](https://graphs.waffle.io/MatthewBooth/OTAUpdates/throughput.svg)](https://waffle.io/MatthewBooth/OTAUpdates/metrics)

# Requirements


1. Root - Not essential, but you will most likely have this anyway
2. A place to store a static manifest XML
3. A place to host your ROM
4. Some build.prop entries

# Building or Downloading
## Downloading

You can get the latest version of the APK [here](http://ultimarom.com/ota-updates/).

## Eclipse

To build from source via Eclipse, you need to checkout the [instructions on building](Building.md)

## AOSP

To build from source in your AOSP build, you need to checkout the [instructions on building with AOSP](AOSP_CM.md). There are some advantages of this method, being that this method does not need root/SU permissions throughout the app.

# Using

There are two ways you can use this in your ROM. Manually, or automatically with [romhut.com](https://www.romhut.com). 

RomHut is highly recommended, as it will host your files for you, automate the process somewhat and it's free for users and developers:
- [Using with romhut.com](RomHut.md)
- [Using manually](Manually.md)

# Installing

Be aware, this application is meant for ROM developers. If you are a user wishing to use this application, get in touch with your developer and point them towards this page!

For ROM developers, [see here for installing instructions](Installing.md)

# Usage

Anyone is free to use this project in their ROM. I only request that you keep the about page in-tact leaving my credits in there. You don't have to, but it'd be nice if you did.

I'd also appreciate a tag in your post, wherever you publish :)

# Contributions

If you feel like you can contribute to this project, don't hesitate to fork and send me some pull requests.

# Licencing

This project is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0). A copy of the licence can be obtained [here](http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode).

This licence is chosen because it gives people the rights to clone this project and freely use it in their ROM or application, but only so long as they are sharing it in the same manner, and not going to publish it for commercial uses. I.E. This is free to use and modify (so long as you share any changes under the same licence), but you can't take it and sell it.

Other parts of this project (Bypass, cardview) have their own licences and are not affected by this one.
